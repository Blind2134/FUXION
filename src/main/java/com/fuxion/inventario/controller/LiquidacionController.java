package com.fuxion.inventario.controller;

import com.fuxion.inventario.dto.LiquidacionRequest;
// import com.fuxion.inventario.service.LiquidacionService; // Descomenta cuando crees el service
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/liquidaciones")
@RequiredArgsConstructor

public class LiquidacionController {

    // private final LiquidacionService liquidacionService;

    @PostMapping("/solicitar")
    public ResponseEntity<String> solicitarPago(@RequestBody LiquidacionRequest request) {
        // liquidacionService.registrarSolicitud(request);
        return ResponseEntity.ok("Solicitud de pago enviada al socio correctamente.");
    }
}