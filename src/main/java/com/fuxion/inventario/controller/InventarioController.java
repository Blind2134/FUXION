package com.fuxion.inventario.controller;

import com.fuxion.inventario.dto.EntradaStockRequest;
import com.fuxion.inventario.dto.InventarioDTO;
import com.fuxion.inventario.dto.MovimientoStockDTO;
import com.fuxion.inventario.service.InventarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventario")
@RequiredArgsConstructor
public class InventarioController {

    private final InventarioService inventarioService;

    // 1. VER STOCK ACTUAL DE MI ALMACÉN
    // GET /api/inventario?idAlmacen=1
    @GetMapping
    public ResponseEntity<List<InventarioDTO>> verStockActual(@RequestParam Long idAlmacen) {
        return ResponseEntity.ok(inventarioService.listarTodoStock(idAlmacen));
    }

    // 2. REGISTRAR ENTRADA DE CAJAS (Cuando recoges productos)
    // POST /api/inventario/entrada
    @PostMapping("/entrada")
    public ResponseEntity<String> registrarEntrada(@RequestBody EntradaStockRequest request) {
        inventarioService.registrarEntrada(request);
        return ResponseEntity.ok("Entrada registrada correctamente");
    }

    // 3. VER HISTORIAL DE MOVIMIENTOS
    // GET /api/inventario/movimientos?idAlmacen=1
    @GetMapping("/movimientos")
    public ResponseEntity<List<MovimientoStockDTO>> verHistorial(@RequestParam Long idAlmacen) {
        return ResponseEntity.ok(inventarioService.listarMovimientos(idAlmacen));
    }
}