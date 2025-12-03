package com.ecocoins.ecocoins_microservice.controller;

import com.ecocoins.ecocoins_microservice.dto.ApiResponse;
import com.ecocoins.ecocoins_microservice.dto.ReciclajeQrRequest;
import com.ecocoins.ecocoins_microservice.service.QrService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/qr")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "QR Code", description = "Gestión de reciclaje mediante códigos QR")
public class QrController {

    private final QrService qrService;

    public QrController(QrService qrService) {
        this.qrService = qrService;
    }

    /**
     * Validar código QR escaneado
     * GET /api/qr/validar/{codigo}
     */
    @GetMapping("/validar/{codigo}")
    @Operation(summary = "Validar código QR", description = "Verifica que un código QR sea válido y obtiene info del contenedor")
    public ResponseEntity<ApiResponse<Map<String, Object>>> validarQr(@PathVariable String codigo) {
        Map<String, Object> info = qrService.obtenerInfoContenedor(codigo);
        return ResponseEntity.ok(ApiResponse.success("Contenedor encontrado", info));
    }

    /**
     * Registrar reciclaje escaneando QR
     * POST /api/qr/registrar
     */
    @PostMapping("/registrar")
    @Operation(
            summary = "Registrar reciclaje con QR",
            description = "Registra un reciclaje escaneando el código QR de un contenedor"
    )
    public ResponseEntity<ApiResponse<Map<String, Object>>> registrarConQr(
            @Valid @RequestBody ReciclajeQrRequest request) {

        Map<String, Object> resultado = qrService.registrarReciclajeConQr(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Reciclaje registrado exitosamente", resultado));
    }

    /**
     * Obtener tarifas de materiales
     * GET /api/qr/tarifas
     */
    @GetMapping("/tarifas")
    @Operation(summary = "Obtener tarifas", description = "Lista las tarifas de EcoCoins por kg para cada tipo de material")
    public ResponseEntity<ApiResponse<Map<String, Integer>>> obtenerTarifas() {
        Map<String, Integer> tarifas = qrService.obtenerTarifas();
        return ResponseEntity.ok(ApiResponse.success(tarifas));
    }
}