package com.ecocoins.ecocoins_microservice.util;

import com.ecocoins.ecocoins_microservice.exception.BadRequestException;
import java.util.regex.Pattern;

public class ValidationUtil {

    // Patrón para correos institucionales de Tecsup
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@tecsup\\.edu\\.pe$"
    );

    // Patrón para validar nombres (solo letras y espacios)
    private static final Pattern NAME_PATTERN = Pattern.compile(
            "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$"
    );

    // Patrón para teléfonos peruanos (9 dígitos)
    private static final Pattern PHONE_PATTERN = Pattern.compile(
            "^9\\d{8}$"
    );

    /**
     * Valida que el correo sea institucional de Tecsup
     */
    public static void validarCorreoInstitucional(String correo) {
        if (correo == null || correo.trim().isEmpty()) {
            throw new BadRequestException("El correo no puede estar vacío");
        }

        if (!EMAIL_PATTERN.matcher(correo.toLowerCase()).matches()) {
            throw new BadRequestException(
                    "Debes usar tu correo institucional de Tecsup (@tecsup.edu.pe)"
            );
        }
    }

    /**
     * Valida que el nombre solo contenga letras y espacios
     */
    public static void validarNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new BadRequestException("El nombre no puede estar vacío");
        }

        if (nombre.trim().length() < 3) {
            throw new BadRequestException("El nombre debe tener al menos 3 caracteres");
        }

        if (!NAME_PATTERN.matcher(nombre).matches()) {
            throw new BadRequestException("El nombre solo puede contener letras y espacios");
        }
    }

    /**
     * Valida formato de teléfono peruano
     */
    public static void validarTelefono(String telefono) {
        if (telefono == null || telefono.trim().isEmpty()) {
            return; // Teléfono es opcional
        }

        String telefonoLimpio = telefono.replaceAll("[\\s-]", "");

        if (!PHONE_PATTERN.matcher(telefonoLimpio).matches()) {
            throw new BadRequestException(
                    "Formato de teléfono inválido. Debe ser un celular peruano de 9 dígitos (ej: 987654321)"
            );
        }
    }

    /**
     * Valida que la contraseña cumpla con requisitos mínimos
     */
    public static void validarContrasenia(String contrasenia) {
        if (contrasenia == null || contrasenia.isEmpty()) {
            throw new BadRequestException("La contraseña no puede estar vacía");
        }

        if (contrasenia.length() < 6) {
            throw new BadRequestException("La contraseña debe tener al menos 6 caracteres");
        }

        if (contrasenia.length() > 100) {
            throw new BadRequestException("La contraseña no puede exceder 100 caracteres");
        }

        // Opcional: Validar complejidad
        // boolean tieneNumero = contrasenia.matches(".*\\d.*");
        // boolean tieneMayuscula = contrasenia.matches(".*[A-Z].*");
        // if (!tieneNumero || !tieneMayuscula) {
        //     throw new BadRequestException("La contraseña debe contener al menos una mayúscula y un número");
        // }
    }

    /**
     * Valida que un peso sea válido
     */
    public static void validarPeso(Double peso) {
        if (peso == null) {
            throw new BadRequestException("El peso es obligatorio");
        }

        if (peso <= 0) {
            throw new BadRequestException("El peso debe ser mayor a 0");
        }

        if (peso > 1000) {
            throw new BadRequestException("El peso no puede exceder 1000 kg");
        }
    }

    /**
     * Valida que un ID de MongoDB sea válido (24 caracteres hexadecimales)
     */
    public static void validarMongoId(String id, String nombreCampo) {
        if (id == null || id.trim().isEmpty()) {
            throw new BadRequestException("El " + nombreCampo + " no puede estar vacío");
        }

        if (!id.matches("^[a-fA-F0-9]{24}$")) {
            throw new BadRequestException("El " + nombreCampo + " no tiene un formato válido");
        }
    }

    /**
     * Valida que un string no esté vacío
     */
    public static void validarNoVacio(String valor, String nombreCampo) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new BadRequestException("El campo " + nombreCampo + " no puede estar vacío");
        }
    }

    /**
     * Valida que un valor esté dentro de un rango
     */
    public static void validarRango(int valor, int min, int max, String nombreCampo) {
        if (valor < min || valor > max) {
            throw new BadRequestException(
                    String.format("El %s debe estar entre %d y %d", nombreCampo, min, max)
            );
        }
    }
}