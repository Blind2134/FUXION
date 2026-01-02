package com.fuxion.inventario.controller;

import com.fuxion.inventario.model.entity.Prestamo;
import com.fuxion.inventario.service.PrestamoService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prestamos")
@RequiredArgsConstructor

public class PrestamoController {

    private final PrestamoService prestamoService;

    // 1. SOLICITAR UN PRÉSTAMO (Jeampier pide a Betzaida)
    @PostMapping("/solicitar")
    public ResponseEntity<String> solicitar(@RequestBody SolicitudDto dto) {
        prestamoService.solicitarPrestamo(
                dto.getIdAlmacen(),
                dto.getIdDeudor(),
                dto.getIdAcreedor(),
                dto.getIdProducto(),
                dto.getCantidad()
        );
        return ResponseEntity.ok("Solicitud de préstamo enviada para aprobación.");
    }

    // 2. APROBAR PRÉSTAMO (Tú confirmas que moviste la caja)
    @PutMapping("/{id}/aprobar")
    public ResponseEntity<String> aprobar(@PathVariable Long id) {
        prestamoService.aprobarPrestamo(id);
        return ResponseEntity.ok("Préstamo aprobado. El stock ha sido transferido.");
    }

    // 3. VER SOLICITUDES PENDIENTES (Tu campanita de notificaciones)
    @GetMapping("/pendientes/{idAlmacen}")
    public ResponseEntity<List<Prestamo>> listarPendientes(@PathVariable Long idAlmacen) {
        return ResponseEntity.ok(prestamoService.listarPendientes(idAlmacen));
    }

    // DTO interno simple para recibir los datos del JSON
    @Data
    public static class SolicitudDto {
        private Long idAlmacen;
        private Long idDeudor;   // El que pide (Jeampier)
        private Long idAcreedor; // El que tiene (Betzaida)
        private Long idProducto;
        private Integer cantidad;
    }
}