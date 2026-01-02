package com.fuxion.inventario.controller;

import com.fuxion.inventario.dto.GananciasResumenDTO;
import com.fuxion.inventario.dto.PedidoResponse;
import com.fuxion.inventario.service.GananciasService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ganancias")
@RequiredArgsConstructor
public class GananciasController {

    private final GananciasService gananciasService;

    // 1. RESUMEN DE COMISIONES
    // GET /api/ganancias/resumen?idAlmacenero=1&mes=12&anio=2025
    @GetMapping("/resumen")
    public ResponseEntity<GananciasResumenDTO> obtenerResumen(
            @RequestParam Long idAlmacenero,
            @RequestParam(required = false) Integer mes,
            @RequestParam(required = false) Integer anio) {
        return ResponseEntity.ok(gananciasService.obtenerResumen(idAlmacenero, mes, anio));
    }

    // 2. HISTORIAL DE PEDIDOS ENTREGADOS (Con comisiones)
    // GET /api/ganancias/historial?idAlmacenero=1
    @GetMapping("/historial")
    public ResponseEntity<List<PedidoResponse>> obtenerHistorial(
            @RequestParam Long idAlmacenero) {
        return ResponseEntity.ok(gananciasService.obtenerHistorial(idAlmacenero));
    }
}