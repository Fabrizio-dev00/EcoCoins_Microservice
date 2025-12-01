package com.ecocoins.ecocoins_microservice.service;

import com.ecocoins.ecocoins_microservice.exception.ResourceNotFoundException;
import com.ecocoins.ecocoins_microservice.model.Reciclaje;
import com.ecocoins.ecocoins_microservice.model.Usuario;
import com.ecocoins.ecocoins_microservice.repository.ReciclajeRepository;
import com.ecocoins.ecocoins_microservice.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class LogrosService {

    private final UsuarioRepository usuarioRepository;
    private final ReciclajeRepository reciclajeRepository;

    // Definición de todos los logros del sistema
    private static final List<Map<String, Object>> LOGROS_SISTEMA = Arrays.asList(
            // RECICLAJE
            Map.of("id", "L001", "titulo", "Primer Paso", "descripcion", "Realiza tu primer reciclaje",
                    "categoria", "RECICLAJE", "rareza", "COMUN", "icono", "🌱", "recompensaEcoCoins", 10,
                    "criterio", "totalReciclajes", "meta", 1),
            Map.of("id", "L002", "titulo", "Eco Guerrero", "descripcion", "Completa 10 reciclajes",
                    "categoria", "RECICLAJE", "rareza", "RARO", "icono", "♻️", "recompensaEcoCoins", 50,
                    "criterio", "totalReciclajes", "meta", 10),
            Map.of("id", "L003", "titulo", "Maestro Verde", "descripcion", "Completa 50 reciclajes",
                    "categoria", "RECICLAJE", "rareza", "EPICO", "icono", "🏆", "recompensaEcoCoins", 200,
                    "criterio", "totalReciclajes", "meta", 50),

            // ECOCOINS
            Map.of("id", "L004", "titulo", "Ahorrando", "descripcion", "Acumula 100 EcoCoins",
                    "categoria", "ECOCOINS", "rareza", "COMUN", "icono", "💰", "recompensaEcoCoins", 20,
                    "criterio", "ecoCoins", "meta", 100),
            Map.of("id", "L005", "titulo", "Rico en Verde", "descripcion", "Acumula 500 EcoCoins",
                    "categoria", "ECOCOINS", "rareza", "RARO", "icono", "💎", "recompensaEcoCoins", 100,
                    "criterio", "ecoCoins", "meta", 500),

            // SOCIAL
            Map.of("id", "L006", "titulo", "Influencer Verde", "descripcion", "Refiere a 3 amigos",
                    "categoria", "SOCIAL", "rareza", "RARO", "icono", "👥", "recompensaEcoCoins", 75,
                    "criterio", "referidos", "meta", 3),

            // RACHA
            Map.of("id", "L007", "titulo", "Constante", "descripcion", "Recicla 3 días seguidos",
                    "categoria", "RACHA", "rareza", "COMUN", "icono", "🔥", "recompensaEcoCoins", 30,
                    "criterio", "rachaActual", "meta", 3),
            Map.of("id", "L008", "titulo", "Imparable", "descripcion", "Recicla 7 días seguidos",
                    "categoria", "RACHA", "rareza", "EPICO", "icono", "⚡", "recompensaEcoCoins", 150,
                    "criterio", "rachaActual", "meta", 7),

            // ESPECIAL
            Map.of("id", "L009", "titulo", "Leyenda Eco", "descripcion", "Alcanza el nivel Platino",
                    "categoria", "ESPECIAL", "rareza", "LEGENDARIO", "icono", "👑", "recompensaEcoCoins", 500,
                    "criterio", "nivel", "meta", 4)
    );

    public LogrosService(UsuarioRepository usuarioRepository, ReciclajeRepository reciclajeRepository) {
        this.usuarioRepository = usuarioRepository;
        this.reciclajeRepository = reciclajeRepository;
    }

    /**
     * Obtener todos los logros del sistema
     */
    public List<Map<String, Object>> obtenerTodosLosLogros() {
        return new ArrayList<>(LOGROS_SISTEMA);
    }

    /**
     * Obtener logros de un usuario con su progreso
     */
    public Map<String, Object> obtenerLogrosUsuario(String usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", usuarioId));

        List<Reciclaje> reciclajes = reciclajeRepository.findByUsuarioId(usuarioId);

        Map<String, Object> estadisticas = calcularEstadisticasUsuario(usuario, reciclajes);

        List<Map<String, Object>> logrosConProgreso = new ArrayList<>();
        int logrosCompletados = 0;
        int totalEcoCoinsGanados = 0;

        for (Map<String, Object> logro : LOGROS_SISTEMA) {
            Map<String, Object> logroConProgreso = new HashMap<>(logro);

            String criterio = (String) logro.get("criterio");
            int meta = (Integer) logro.get("meta");
            int progresoActual = obtenerProgresoSegunCriterio(estadisticas, criterio);

            boolean completado = progresoActual >= meta;

            logroConProgreso.put("progresoActual", progresoActual);
            logroConProgreso.put("completado", completado);
            logroConProgreso.put("progresoPorcentaje", Math.min(100, (progresoActual * 100) / meta));

            if (completado) {
                logrosCompletados++;
                totalEcoCoinsGanados += (Integer) logro.get("recompensaEcoCoins");
            }

            logrosConProgreso.add(logroConProgreso);
        }

        return Map.of(
                "logros", logrosConProgreso,
                "logrosCompletados", logrosCompletados,
                "totalLogros", LOGROS_SISTEMA.size(),
                "progresoPorcentaje", (logrosCompletados * 100) / LOGROS_SISTEMA.size(),
                "totalEcoCoinsGanados", totalEcoCoinsGanados
        );
    }

    /**
     * Verificar y actualizar logros de un usuario
     */
    public List<Map<String, Object>> verificarYActualizarLogros(String usuarioId) {
        Map<String, Object> logrosData = obtenerLogrosUsuario(usuarioId);

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> logros = (List<Map<String, Object>>) logrosData.get("logros");

        return logros.stream()
                .filter(logro -> (Boolean) logro.get("completado"))
                .collect(Collectors.toList());
    }

    /**
     * Calcular estadísticas del usuario
     */
    private Map<String, Object> calcularEstadisticasUsuario(Usuario usuario, List<Reciclaje> reciclajes) {
        Map<String, Object> stats = new HashMap<>();

        stats.put("totalReciclajes", usuario.getTotalReciclajes());
        stats.put("ecoCoins", usuario.getEcoCoins());
        stats.put("nivel", usuario.getNivel());
        stats.put("referidos", 0); // TODO: Implementar sistema de referidos
        stats.put("rachaActual", calcularRachaActual(reciclajes));

        return stats;
    }

    /**
     * Calcular racha actual de reciclaje
     */
    private int calcularRachaActual(List<Reciclaje> reciclajes) {
        if (reciclajes.isEmpty()) return 0;

        // Ordenar por fecha descendente
        List<Reciclaje> ordenados = reciclajes.stream()
                .sorted(Comparator.comparing(Reciclaje::getFecha).reversed())
                .collect(Collectors.toList());

        int racha = 0;
        LocalDateTime hoy = LocalDateTime.now().toLocalDate().atStartOfDay();

        for (Reciclaje reciclaje : ordenados) {
            LocalDateTime diaReciclaje = reciclaje.getFecha().toLocalDate().atStartOfDay();
            long diasDiferencia = java.time.temporal.ChronoUnit.DAYS.between(diaReciclaje, hoy);

            if (diasDiferencia <= racha) {
                racha++;
            } else {
                break;
            }
        }

        return racha;
    }

    /**
     * Obtener progreso según el criterio
     */
    private int obtenerProgresoSegunCriterio(Map<String, Object> estadisticas, String criterio) {
        Object valor = estadisticas.get(criterio);

        if (valor instanceof Integer) {
            return (Integer) valor;
        } else if (valor instanceof Long) {
            return ((Long) valor).intValue();
        }

        return 0;
    }
}