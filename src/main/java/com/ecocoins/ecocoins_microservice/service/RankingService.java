package com.ecocoins.ecocoins_microservice.service;

import com.ecocoins.ecocoins_microservice.exception.BadRequestException;
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
public class RankingService {

    private final UsuarioRepository usuarioRepository;
    private final ReciclajeRepository reciclajeRepository;

    public RankingService(UsuarioRepository usuarioRepository, ReciclajeRepository reciclajeRepository) {
        this.usuarioRepository = usuarioRepository;
        this.reciclajeRepository = reciclajeRepository;
    }

    /**
     * Obtener ranking por periodo
     */
    public List<Map<String, Object>> obtenerRankingPorPeriodo(String periodo) {
        LocalDateTime fechaInicio = obtenerFechaInicioPeriodo(periodo);

        List<Usuario> usuarios = usuarioRepository.findAll();

        List<Map<String, Object>> ranking = new ArrayList<>();

        for (Usuario usuario : usuarios) {
            List<Reciclaje> reciclajes = reciclajeRepository
                    .findByUsuarioIdAndFechaGreaterThanEqual(usuario.getId(), fechaInicio);

            int ecoCoinsEnPeriodo = reciclajes.stream()
                    .mapToInt(Reciclaje::getEcoCoinsGanadas)
                    .sum();

            double kgEnPeriodo = reciclajes.stream()
                    .mapToDouble(Reciclaje::getPesoKg)
                    .sum();

            Map<String, Object> usuarioRanking = new HashMap<>();
            usuarioRanking.put("usuarioId", usuario.getId());
            usuarioRanking.put("nombre", usuario.getNombre());
            usuarioRanking.put("avatar", usuario.getAvatar());
            usuarioRanking.put("nivel", usuario.getNivel());
            usuarioRanking.put("ecoCoins", ecoCoinsEnPeriodo);
            usuarioRanking.put("totalKgReciclados", kgEnPeriodo);
            usuarioRanking.put("totalReciclajes", reciclajes.size());

            ranking.add(usuarioRanking);
        }

        // Ordenar por EcoCoins descendente
        ranking.sort((a, b) -> Integer.compare(
                (Integer) b.get("ecoCoins"),
                (Integer) a.get("ecoCoins")
        ));

        // Agregar posición
        for (int i = 0; i < ranking.size(); i++) {
            ranking.get(i).put("posicion", i + 1);
        }

        return ranking;
    }

    /**
     * Obtener posición de un usuario específico
     */
    public Map<String, Object> obtenerPosicionUsuario(String usuarioId, String periodo) {
        List<Map<String, Object>> ranking = obtenerRankingPorPeriodo(periodo);

        for (int i = 0; i < ranking.size(); i++) {
            Map<String, Object> usuario = ranking.get(i);
            if (usuario.get("usuarioId").equals(usuarioId)) {
                return Map.of(
                        "posicion", i + 1,
                        "totalUsuarios", ranking.size(),
                        "ecoCoins", usuario.get("ecoCoins"),
                        "kgReciclados", usuario.get("totalKgReciclados")
                );
            }
        }

        throw new ResourceNotFoundException("Usuario no encontrado en el ranking");
    }

    /**
     * Obtener top 3 (podio)
     */
    public List<Map<String, Object>> obtenerTop3(String periodo) {
        List<Map<String, Object>> ranking = obtenerRankingPorPeriodo(periodo);
        return ranking.stream()
                .limit(3)
                .collect(Collectors.toList());
    }

    /**
     * Calcular fecha de inicio según el periodo
     */
    private LocalDateTime obtenerFechaInicioPeriodo(String periodo) {
        LocalDateTime ahora = LocalDateTime.now();

        return switch (periodo.toUpperCase()) {
            case "SEMANAL" -> ahora.minusWeeks(1);
            case "MENSUAL" -> ahora.minusMonths(1);
            case "HISTORICO" -> LocalDateTime.of(2000, 1, 1, 0, 0);
            default -> throw new BadRequestException("Periodo inválido. Usa: SEMANAL, MENSUAL o HISTORICO");
        };
    }
}