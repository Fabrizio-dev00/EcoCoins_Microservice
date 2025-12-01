package com.ecocoins.ecocoins_microservice.service;

import com.ecocoins.ecocoins_microservice.exception.ResourceNotFoundException;
import com.ecocoins.ecocoins_microservice.model.Reciclaje;
import com.ecocoins.ecocoins_microservice.model.Usuario;
import com.ecocoins.ecocoins_microservice.repository.ReciclajeRepository;
import com.ecocoins.ecocoins_microservice.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EstadisticasDetalladasService {

    private final UsuarioRepository usuarioRepository;
    private final ReciclajeRepository reciclajeRepository;

    // Factores de conversión para impacto ambiental
    private static final Map<String, Double> FACTOR_CO2_KG = Map.of(
            "Plástico", 1.8,  // kg CO2 ahorrado por kg reciclado
            "Papel", 0.9,
            "Vidrio", 0.3,
            "Metal", 3.5,
            "Cartón", 0.8
    );

    public EstadisticasDetalladasService(UsuarioRepository usuarioRepository,
                                         ReciclajeRepository reciclajeRepository) {
        this.usuarioRepository = usuarioRepository;
        this.reciclajeRepository = reciclajeRepository;
    }

    /**
     * Obtener todas las estadísticas del usuario
     */
    public Map<String, Object> obtenerEstadisticasCompletas(String usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", usuarioId));

        List<Reciclaje> reciclajes = reciclajeRepository.findByUsuarioId(usuarioId);

        Map<String, Object> estadisticas = new HashMap<>();

        estadisticas.put("resumenGeneral", obtenerResumenGeneral(usuarioId));
        estadisticas.put("distribucionMateriales", obtenerDistribucionMateriales(usuarioId));
        estadisticas.put("tendenciaSemanal", obtenerTendenciaSemanal(reciclajes));
        estadisticas.put("impactoAmbiental", calcularImpactoAmbiental(usuarioId));
        estadisticas.put("comparativas", obtenerComparativas(usuario, reciclajes));
        estadisticas.put("rachas", calcularRachas(reciclajes));

        return estadisticas;
    }

    /**
     * Resumen general
     */
    public Map<String, Object> obtenerResumenGeneral(String usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", usuarioId));

        List<Reciclaje> reciclajes = reciclajeRepository.findByUsuarioId(usuarioId);

        int totalEcoCoinsGanados = reciclajes.stream()
                .mapToInt(Reciclaje::getEcoCoinsGanadas)
                .sum();

        return Map.of(
                "totalReciclajes", usuario.getTotalReciclajes(),
                "totalKgReciclados", usuario.getTotalKgReciclados(),
                "ecoCoinsActuales", usuario.getEcoCoins(),
                "totalEcoCoinsGanados", totalEcoCoinsGanados,
                "nivel", usuario.getNivel(),
                "promedioKgPorReciclaje", usuario.getTotalReciclajes() > 0
                        ? usuario.getTotalKgReciclados() / usuario.getTotalReciclajes()
                        : 0
        );
    }

    /**
     * Distribución por materiales
     */
    public Map<String, Object> obtenerDistribucionMateriales(String usuarioId) {
        List<Reciclaje> reciclajes = reciclajeRepository.findByUsuarioId(usuarioId);

        Map<String, Map<String, Object>> distribucion = new HashMap<>();

        for (Reciclaje reciclaje : reciclajes) {
            String material = reciclaje.getTipoMaterial();

            distribucion.putIfAbsent(material, new HashMap<>(Map.of(
                    "cantidad", 0,
                    "kgTotales", 0.0,
                    "ecoCoins", 0,
                    "porcentaje", 0.0
            )));

            Map<String, Object> datos = distribucion.get(material);
            datos.put("cantidad", (Integer) datos.get("cantidad") + 1);
            datos.put("kgTotales", (Double) datos.get("kgTotales") + reciclaje.getPesoKg());
            datos.put("ecoCoins", (Integer) datos.get("ecoCoins") + reciclaje.getEcoCoinsGanadas());
        }

        // Calcular porcentajes
        int totalReciclajes = reciclajes.size();
        for (Map.Entry<String, Map<String, Object>> entry : distribucion.entrySet()) {
            Map<String, Object> datos = entry.getValue();
            int cantidad = (Integer) datos.get("cantidad");
            datos.put("porcentaje", totalReciclajes > 0
                    ? (double) cantidad / totalReciclajes * 100
                    : 0);
        }

        return Map.of("distribucion", distribucion);
    }

    /**
     * Tendencia semanal (últimos 7 días)
     */
    private Map<String, Object> obtenerTendenciaSemanal(List<Reciclaje> reciclajes) {
        LocalDate hoy = LocalDate.now();
        Map<String, Integer> reciclajesPorDia = new LinkedHashMap<>();

        // Inicializar con los últimos 7 días
        for (int i = 6; i >= 0; i--) {
            LocalDate dia = hoy.minusDays(i);
            reciclajesPorDia.put(formatearDiaSemana(dia.getDayOfWeek()), 0);
        }

        // Contar reciclajes por día
        for (Reciclaje reciclaje : reciclajes) {
            LocalDate diaReciclaje = reciclaje.getFecha().toLocalDate();
            long diasAtras = ChronoUnit.DAYS.between(diaReciclaje, hoy);

            if (diasAtras >= 0 && diasAtras < 7) {
                String nombreDia = formatearDiaSemana(diaReciclaje.getDayOfWeek());
                reciclajesPorDia.put(nombreDia, reciclajesPorDia.get(nombreDia) + 1);
            }
        }

        return Map.of("tendencia", reciclajesPorDia);
    }

    /**
     * Calcular impacto ambiental
     */
    public Map<String, Object> calcularImpactoAmbiental(String usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", usuarioId));

        List<Reciclaje> reciclajes = reciclajeRepository.findByUsuarioId(usuarioId);

        double totalCo2Ahorrado = 0;

        for (Reciclaje reciclaje : reciclajes) {
            String material = reciclaje.getTipoMaterial();
            double factor = FACTOR_CO2_KG.getOrDefault(material, 1.0);
            totalCo2Ahorrado += reciclaje.getPesoKg() * factor;
        }

        // Equivalencias
        int arbolesEquivalentes = (int) (totalCo2Ahorrado / 20); // ~20kg CO2 por árbol/año
        double energiaAhorrada = totalCo2Ahorrado * 3.5; // kWh aproximados
        double aguaAhorrada = usuario.getTotalKgReciclados() * 15; // litros aproximados

        return Map.of(
                "co2Ahorrado", Math.round(totalCo2Ahorrado * 100.0) / 100.0,
                "arbolesEquivalentes", arbolesEquivalentes,
                "energiaAhorrada", Math.round(energiaAhorrada * 100.0) / 100.0,
                "aguaAhorrada", Math.round(aguaAhorrada * 100.0) / 100.0
        );
    }

    /**
     * Comparativas con otros usuarios
     */
    private Map<String, Object> obtenerComparativas(Usuario usuario, List<Reciclaje> reciclajes) {
        List<Usuario> todosUsuarios = usuarioRepository.findAll();

        double promedioGeneral = todosUsuarios.stream()
                .mapToDouble(Usuario::getTotalKgReciclados)
                .average()
                .orElse(0);

        int usuariosSupera = (int) todosUsuarios.stream()
                .filter(u -> u.getTotalKgReciclados() < usuario.getTotalKgReciclados())
                .count();

        return Map.of(
                "promedioGeneral", Math.round(promedioGeneral * 100.0) / 100.0,
                "posicionGeneral", todosUsuarios.size() - usuariosSupera + 1,
                "totalUsuarios", todosUsuarios.size(),
                "porcentajeSuperior", todosUsuarios.size() > 0
                        ? (double) usuariosSupera / todosUsuarios.size() * 100
                        : 0
        );
    }

    /**
     * Calcular rachas
     */
    private Map<String, Object> calcularRachas(List<Reciclaje> reciclajes) {
        if (reciclajes.isEmpty()) {
            return Map.of("rachaActual", 0, "mejorRacha", 0);
        }

        List<LocalDate> diasConReciclaje = reciclajes.stream()
                .map(r -> r.getFecha().toLocalDate())
                .distinct()
                .sorted()
                .collect(Collectors.toList());

        int rachaActual = 0;
        int mejorRacha = 0;
        int rachaTemp = 1;

        LocalDate ultimoDia = diasConReciclaje.get(0);

        for (int i = 1; i < diasConReciclaje.size(); i++) {
            LocalDate diaActual = diasConReciclaje.get(i);

            if (ChronoUnit.DAYS.between(ultimoDia, diaActual) == 1) {
                rachaTemp++;
            } else {
                mejorRacha = Math.max(mejorRacha, rachaTemp);
                rachaTemp = 1;
            }

            ultimoDia = diaActual;
        }

        mejorRacha = Math.max(mejorRacha, rachaTemp);

        // Calcular racha actual
        LocalDate hoy = LocalDate.now();
        if (diasConReciclaje.contains(hoy) || diasConReciclaje.contains(hoy.minusDays(1))) {
            rachaActual = rachaTemp;
        }

        return Map.of(
                "rachaActual", rachaActual,
                "mejorRacha", mejorRacha
        );
    }

    /**
     * Formatear día de la semana
     */
    private String formatearDiaSemana(DayOfWeek dia) {
        return switch (dia) {
            case MONDAY -> "Lun";
            case TUESDAY -> "Mar";
            case WEDNESDAY -> "Mié";
            case THURSDAY -> "Jue";
            case FRIDAY -> "Vie";
            case SATURDAY -> "Sáb";
            case SUNDAY -> "Dom";
        };
    }
}