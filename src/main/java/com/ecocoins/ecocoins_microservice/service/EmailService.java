package com.ecocoins.ecocoins_microservice.service;

import com.ecocoins.ecocoins_microservice.model.Usuario;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class EmailService {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    /**
     * Enviar email de bienvenida
     * (Por ahora solo simula el envío, puedes integrarlo con JavaMailSender después)
     */
    public void enviarEmailBienvenida(Usuario usuario) {
        String asunto = "¡Bienvenido a EcoCoins Campus! 🌱";
        String mensaje = construirMensajeBienvenida(usuario);

        // Por ahora solo logueamos
        System.out.println("📧 EMAIL ENVIADO:");
        System.out.println("Para: " + usuario.getCorreo());
        System.out.println("Asunto: " + asunto);
        System.out.println("Mensaje: " + mensaje);

        // TODO: Implementar envío real con JavaMailSender
        // mailSender.send(message);
    }

    /**
     * Enviar email de canje exitoso
     */
    public void enviarEmailCanje(String correo, String nombreRecompensa, int costoEcoCoins) {
        String asunto = "🎁 Canje exitoso - EcoCoins Campus";
        String mensaje = String.format(
                """
                ¡Hola!
                
                Tu canje ha sido procesado exitosamente:
                
                📦 Recompensa: %s
                💰 Costo: %d EcoCoins
                📅 Fecha: %s
                
                Nos pondremos en contacto contigo pronto para coordinar la entrega.
                
                ¡Gracias por reciclar!
                
                EcoCoins Campus Team 🌱
                """,
                nombreRecompensa,
                costoEcoCoins,
                LocalDateTime.now().format(FORMATTER)
        );

        System.out.println("📧 EMAIL ENVIADO:");
        System.out.println("Para: " + correo);
        System.out.println("Asunto: " + asunto);
        System.out.println("Mensaje: " + mensaje);
    }

    /**
     * Enviar email de reciclaje registrado
     */
    public void enviarEmailReciclaje(String correo, String tipoMaterial, double pesoKg, int ecoCoinsGanadas) {
        String asunto = "♻️ Reciclaje registrado - EcoCoins Campus";
        String mensaje = String.format(
                """
                ¡Excelente trabajo!
                
                Tu reciclaje ha sido registrado:
                
                ♻️ Material: %s
                ⚖️ Peso: %.2f kg
                💰 EcoCoins ganadas: +%d
                📅 Fecha: %s
                
                ¡Sigue reciclando y gana más recompensas!
                
                EcoCoins Campus Team 🌱
                """,
                tipoMaterial,
                pesoKg,
                ecoCoinsGanadas,
                LocalDateTime.now().format(FORMATTER)
        );

        System.out.println("📧 EMAIL ENVIADO:");
        System.out.println("Para: " + correo);
        System.out.println("Asunto: " + asunto);
        System.out.println("Mensaje: " + mensaje);
    }

    /**
     * Enviar email de recuperación de contraseña
     */
    public void enviarEmailRecuperacion(String correo, String token) {
        String asunto = "🔐 Recuperación de contraseña - EcoCoins Campus";
        String enlaceRecuperacion = "https://ecocoins.app/reset-password?token=" + token;

        String mensaje = String.format(
                """
                Hola,
                
                Hemos recibido una solicitud para restablecer tu contraseña.
                
                Haz clic en el siguiente enlace para crear una nueva contraseña:
                
                🔗 %s
                
                Este enlace expirará en 1 hora.
                
                Si no solicitaste este cambio, ignora este mensaje.
                
                EcoCoins Campus Team 🌱
                """,
                enlaceRecuperacion
        );

        System.out.println("📧 EMAIL ENVIADO:");
        System.out.println("Para: " + correo);
        System.out.println("Asunto: " + asunto);
        System.out.println("Mensaje: " + mensaje);
    }

    /**
     * Enviar email de notificación de nivel
     */
    public void enviarEmailNuevoNivel(String correo, String nombreUsuario, String nivel) {
        String asunto = "🎊 ¡Subiste de nivel! - EcoCoins Campus";
        String mensaje = String.format(
                """
                ¡Felicidades %s!
                
                🎉 Has alcanzado el nivel %s 🎉
                
                Esto significa que estás reciclando cada vez más y contribuyendo
                al cuidado del medio ambiente.
                
                ¡Sigue así y desbloquea más recompensas!
                
                EcoCoins Campus Team 🌱
                """,
                nombreUsuario,
                nivel
        );

        System.out.println("📧 EMAIL ENVIADO:");
        System.out.println("Para: " + correo);
        System.out.println("Asunto: " + asunto);
        System.out.println("Mensaje: " + mensaje);
    }

    /**
     * Enviar email de resumen semanal
     */
    public void enviarEmailResumenSemanal(String correo, int reciclajesRealizados,
                                          double kgReciclados, int ecoCoinsGanadas) {
        String asunto = "📊 Tu resumen semanal - EcoCoins Campus";
        String mensaje = String.format(
                """
                ¡Hola!
                
                Aquí está tu resumen de esta semana:
                
                ♻️ Reciclajes realizados: %d
                ⚖️ Kilogramos reciclados: %.2f kg
                💰 EcoCoins ganadas: +%d
                
                ¡Gracias por tu compromiso con el medio ambiente!
                
                EcoCoins Campus Team 🌱
                """,
                reciclajesRealizados,
                kgReciclados,
                ecoCoinsGanadas
        );

        System.out.println("📧 EMAIL ENVIADO:");
        System.out.println("Para: " + correo);
        System.out.println("Asunto: " + asunto);
        System.out.println("Mensaje: " + mensaje);
    }

    /**
     * Construir mensaje de bienvenida personalizado
     */
    private String construirMensajeBienvenida(Usuario usuario) {
        return String.format(
                """
                ¡Hola %s!
                
                🎉 Bienvenido a EcoCoins Campus 🎉
                
                Estamos emocionados de que te unas a nuestra comunidad de recicladores.
                
                Con EcoCoins Campus podrás:
                ✅ Reciclar materiales y ganar EcoCoins
                ✅ Canjear tus EcoCoins por recompensas
                ✅ Contribuir al cuidado del medio ambiente
                ✅ Subir de nivel y desbloquear beneficios
                
                Tu cuenta ha sido creada exitosamente:
                📧 Correo: %s
                💰 EcoCoins iniciales: %d
                🏅 Nivel: Bronce
                
                ¡Empieza a reciclar hoy mismo!
                
                EcoCoins Campus Team 🌱
                """,
                usuario.getNombre(),
                usuario.getCorreo(),
                usuario.getEcoCoins()
        );
    }

    /**
     * Validar formato de correo
     */
    public boolean esCorreoValido(String correo) {
        if (correo == null || correo.trim().isEmpty()) {
            return false;
        }

        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return correo.matches(regex);
    }
}