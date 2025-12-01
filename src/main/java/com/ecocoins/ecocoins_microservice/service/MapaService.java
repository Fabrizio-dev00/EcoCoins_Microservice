package com.ecocoins.ecocoins_microservice.service;

import com.ecocoins.ecocoins_microservice.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MapaService {

    /**
     * Obtener todos los puntos de reciclaje (datos mock)
     */
    private List<Map<String, Object>> obtenerPuntosMock() {
        List<Map<String, Object>> puntos = new ArrayList<>();

        // Punto 1: Campus Tecsup
        Map<String, Object> p1 = new HashMap<>();
        p1.put("id", "P001");
        p1.put("nombre", "Campus Principal Tecsup");
        p1.put("tipo", "Universidad");
        p1.put("direccion", "Av. Cascanueces 2221, Santa Anita");
        p1.put("latitud", -12.0464);
        p1.put("longitud", -76.9378);
        p1.put("horario", "Lun-Vie: 7:00 AM - 8:00 PM");
        p1.put("materialesAceptados", Arrays.asList("Plástico", "Papel", "Vidrio", "Metal", "Cartón"));
        p1.put("capacidad", 85);
        p1.put("distancia", 0.5);
        p1.put("estado", "Disponible");
        p1.put("telefono", "(01) 317-3900");
        puntos.add(p1);

        // Punto 2: Centro de Acopio Municipal
        Map<String, Object> p2 = new HashMap<>();
        p2.put("id", "P002");
        p2.put("nombre", "Centro de Acopio Municipal");
        p2.put("tipo", "Centro de Acopio");
        p2.put("direccion", "Av. Nicolás Ayllón 3850, Ate");
        p2.put("latitud", -12.0432);
        p2.put("longitud", -76.9401);
        p2.put("horario", "Lun-Sáb: 8:00 AM - 6:00 PM");
        p2.put("materialesAceptados", Arrays.asList("Plástico", "Papel", "Vidrio", "Metal", "Cartón", "Electrónicos"));
        p2.put("capacidad", 60);
        p2.put("distancia", 1.2);
        p2.put("estado", "Disponible");
        p2.put("telefono", "(01) 436-7890");
        puntos.add(p2);

        // Punto 3: Punto Limpio Santa Clara
        Map<String, Object> p3 = new HashMap<>();
        p3.put("id", "P003");
        p3.put("nombre", "Punto Limpio Santa Clara");
        p3.put("tipo", "Punto Limpio");
        p3.put("direccion", "Jr. Santa Clara 245, Ate");
        p3.put("latitud", -12.0498);
        p3.put("longitud", -76.9312);
        p3.put("horario", "Mar-Dom: 9:00 AM - 5:00 PM");
        p3.put("materialesAceptados", Arrays.asList("Plástico", "Papel", "Cartón"));
        p3.put("capacidad", 95);
        p3.put("distancia", 0.8);
        p3.put("estado", "Disponible");
        p3.put("telefono", "(01) 456-1234");
        puntos.add(p3);

        // Punto 4: Contenedor Plaza Vitarte
        Map<String, Object> p4 = new HashMap<>();
        p4.put("id", "P004");
        p4.put("nombre", "Contenedor Plaza Vitarte");
        p4.put("tipo", "Contenedor");
        p4.put("direccion", "Plaza Principal de Vitarte, Ate");
        p4.put("latitud", -12.0521);
        p4.put("longitud", -76.9287);
        p4.put("horario", "24/7");
        p4.put("materialesAceptados", Arrays.asList("Plástico", "Papel"));
        p4.put("capacidad", 40);
        p4.put("distancia", 1.5);
        p4.put("estado", "Casi Lleno");
        p4.put("telefono", "N/A");
        puntos.add(p4);

        // Punto 5: Centro Comercial Megaplaza
        Map<String, Object> p5 = new HashMap<>();
        p5.put("id", "P005");
        p5.put("nombre", "Centro Comercial Megaplaza");
        p5.put("tipo", "Centro de Acopio");
        p5.put("direccion", "Av. Alfredo Mendiola 3698, Independencia");
        p5.put("latitud", -11.9889);
        p5.put("longitud", -77.0612);
        p5.put("horario", "Lun-Dom: 10:00 AM - 10:00 PM");
        p5.put("materialesAceptados", Arrays.asList("Plástico", "Papel", "Vidrio", "Metal"));
        p5.put("capacidad", 70);
        p5.put("distancia", 8.5);
        p5.put("estado", "Disponible");
        p5.put("telefono", "(01) 533-0000");
        puntos.add(p5);

        // Punto 6: Parque Zonal Huiracocha
        Map<String, Object> p6 = new HashMap<>();
        p6.put("id", "P006");
        p6.put("nombre", "Parque Zonal Huiracocha");
        p6.put("tipo", "Punto Limpio");
        p6.put("direccion", "Jr. Huiracocha 1234, San Juan de Lurigancho");
        p6.put("latitud", -12.0012);
        p6.put("longitud", -76.9987);
        p6.put("horario", "Lun-Dom: 6:00 AM - 6:00 PM");
        p6.put("materialesAceptados", Arrays.asList("Plástico", "Papel", "Vidrio", "Cartón", "Orgánico"));
        p6.put("capacidad", 55);
        p6.put("distancia", 3.2);
        p6.put("estado", "Disponible");
        p6.put("telefono", "(01) 489-5678");
        puntos.add(p6);

        return puntos;
    }

    /**
     * Obtener todos los puntos de reciclaje
     */
    public List<Map<String, Object>> obtenerPuntosReciclaje(String tipo, String estado) {
        List<Map<String, Object>> puntos = obtenerPuntosMock();

        // Filtrar por tipo si se especifica
        if (tipo != null && !tipo.isEmpty()) {
            puntos = puntos.stream()
                    .filter(p -> p.get("tipo").equals(tipo))
                    .collect(Collectors.toList());
        }

        // Filtrar por estado si se especifica
        if (estado != null && !estado.isEmpty()) {
            puntos = puntos.stream()
                    .filter(p -> p.get("estado").equals(estado))
                    .collect(Collectors.toList());
        }

        // Ordenar por distancia
        puntos.sort((p1, p2) -> {
            Double d1 = (Double) p1.get("distancia");
            Double d2 = (Double) p2.get("distancia");
            return Double.compare(d1, d2);
        });

        return puntos;
    }

    /**
     * Obtener punto por ID
     */
    public Map<String, Object> obtenerPuntoPorId(String id) {
        List<Map<String, Object>> puntos = obtenerPuntosMock();

        return puntos.stream()
                .filter(p -> p.get("id").equals(id))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Punto de reciclaje", "id", id));
    }

    /**
     * Buscar puntos cercanos a una ubicación
     */
    public List<Map<String, Object>> buscarPuntosCercanos(
            double latitud, double longitud, double radioKm) {

        List<Map<String, Object>> puntos = obtenerPuntosMock();
        List<Map<String, Object>> puntosCercanos = new ArrayList<>();

        for (Map<String, Object> punto : puntos) {
            double puntoLat = (Double) punto.get("latitud");
            double puntoLon = (Double) punto.get("longitud");

            double distancia = calcularDistancia(latitud, longitud, puntoLat, puntoLon);

            if (distancia <= radioKm) {
                Map<String, Object> puntoConDistancia = new HashMap<>(punto);
                puntoConDistancia.put("distanciaCalculada",
                        Math.round(distancia * 100.0) / 100.0);
                puntosCercanos.add(puntoConDistancia);
            }
        }

        // Ordenar por distancia calculada
        puntosCercanos.sort((p1, p2) -> {
            Double d1 = (Double) p1.get("distanciaCalculada");
            Double d2 = (Double) p2.get("distanciaCalculada");
            return Double.compare(d1, d2);
        });

        return puntosCercanos;
    }

    /**
     * Filtrar por material aceptado
     */
    public List<Map<String, Object>> filtrarPorMaterial(String material) {
        List<Map<String, Object>> puntos = obtenerPuntosMock();

        return puntos.stream()
                .filter(punto -> {
                    @SuppressWarnings("unchecked")
                    List<String> materiales = (List<String>) punto.get("materialesAceptados");
                    return materiales.contains(material);
                })
                .collect(Collectors.toList());
    }

    /**
     * Calcular distancia entre dos puntos (fórmula de Haversine)
     */
    private double calcularDistancia(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Radio de la Tierra en km

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }
}