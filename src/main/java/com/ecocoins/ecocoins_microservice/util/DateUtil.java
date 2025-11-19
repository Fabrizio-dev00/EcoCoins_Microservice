package com.ecocoins.ecocoins_microservice.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class DateUtil {

    // Formateadores comunes
    private static final DateTimeFormatter FORMATO_COMPLETO =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    private static final DateTimeFormatter FORMATO_CORTO =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private static final DateTimeFormatter FORMATO_HORA =
            DateTimeFormatter.ofPattern("HH:mm");

    /**
     * Formatea una fecha en formato completo
     * Ejemplo: "18/11/2024 15:30:45"
     */
    public static String formatearCompleto(LocalDateTime fecha) {
        if (fecha == null) return "";
        return fecha.format(FORMATO_COMPLETO);
    }

    /**
     * Formatea una fecha en formato corto
     * Ejemplo: "18/11/2024"
     */
    public static String formatearCorto(LocalDateTime fecha) {
        if (fecha == null) return "";
        return fecha.format(FORMATO_CORTO);
    }

    /**
     * Formatea solo la hora
     * Ejemplo: "15:30"
     */
    public static String formatearHora(LocalDateTime fecha) {
        if (fecha == null) return "";
        return fecha.format(FORMATO_HORA);
    }

    /**
     * Obtiene la fecha de inicio del día actual (00:00:00)
     */
    public static LocalDateTime inicioDelDia() {
        return LocalDateTime.now().truncatedTo(ChronoUnit.DAYS);
    }

    /**
     * Obtiene la fecha de fin del día actual (23:59:59)
     */
    public static LocalDateTime finDelDia() {
        return LocalDateTime.now()
                .truncatedTo(ChronoUnit.DAYS)
                .plusDays(1)
                .minusSeconds(1);
    }

    /**
     * Obtiene la fecha de inicio del mes actual
     */
    public static LocalDateTime inicioDelMes() {
        return LocalDateTime.now()
                .withDayOfMonth(1)
                .truncatedTo(ChronoUnit.DAYS);
    }

    /**
     * Calcula la diferencia en días entre dos fechas
     */
    public static long diasEntre(LocalDateTime fecha1, LocalDateTime fecha2) {
        return ChronoUnit.DAYS.between(fecha1, fecha2);
    }

    /**
     * Calcula la diferencia en horas entre dos fechas
     */
    public static long horasEntre(LocalDateTime fecha1, LocalDateTime fecha2) {
        return ChronoUnit.HOURS.between(fecha1, fecha2);
    }

    /**
     * Verifica si una fecha es de hoy
     */
    public static boolean esHoy(LocalDateTime fecha) {
        if (fecha == null) return false;
        LocalDateTime ahora = LocalDateTime.now();
        return fecha.toLocalDate().equals(ahora.toLocalDate());
    }

    /**
     * Verifica si una fecha es de este mes
     */
    public static boolean esEsteMes(LocalDateTime fecha) {
        if (fecha == null) return false;
        LocalDateTime ahora = LocalDateTime.now();
        return fecha.getYear() == ahora.getYear() &&
                fecha.getMonth() == ahora.getMonth();
    }

    /**
     * Convierte una fecha a texto relativo
     * Ejemplo: "Hace 5 minutos", "Hace 2 horas", "Ayer"
     */
    public static String tiempoRelativo(LocalDateTime fecha) {
        if (fecha == null) return "";

        LocalDateTime ahora = LocalDateTime.now();
        long minutos = ChronoUnit.MINUTES.between(fecha, ahora);

        if (minutos < 1) {
            return "Justo ahora";
        } else if (minutos < 60) {
            return "Hace " + minutos + (minutos == 1 ? " minuto" : " minutos");
        }

        long horas = ChronoUnit.HOURS.between(fecha, ahora);
        if (horas < 24) {
            return "Hace " + horas + (horas == 1 ? " hora" : " horas");
        }

        long dias = ChronoUnit.DAYS.between(fecha, ahora);
        if (dias == 1) {
            return "Ayer";
        } else if (dias < 7) {
            return "Hace " + dias + " días";
        } else if (dias < 30) {
            long semanas = dias / 7;
            return "Hace " + semanas + (semanas == 1 ? " semana" : " semanas");
        } else if (dias < 365) {
            long meses = dias / 30;
            return "Hace " + meses + (meses == 1 ? " mes" : " meses");
        } else {
            long anios = dias / 365;
            return "Hace " + anios + (anios == 1 ? " año" : " años");
        }
    }
}