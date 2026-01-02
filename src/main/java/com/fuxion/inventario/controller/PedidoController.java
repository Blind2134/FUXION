package com.fuxion.inventario.controller;

import com.fuxion.inventario.dto.PedidoRequest;
import com.fuxion.inventario.dto.PedidoResponse;
import com.fuxion.inventario.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor

public class PedidoController {

    private final PedidoService pedidoService;

    // 1. REGISTRAR UNA NUEVA VENTA
    // POST http://localhost:8080/api/pedidos
    @PostMapping
    public ResponseEntity<PedidoResponse> registrarVenta(@RequestBody PedidoRequest request) {
        PedidoResponse response = pedidoService.registrarPedido(request);
        return ResponseEntity.ok(response);
    }

    // 2. CAMBIAR ESTADO (Para cuando el motorizado entrega)
    // PUT http://localhost:8080/api/pedidos/5/estado?nuevoEstado=ENTREGADO
    @PutMapping("/{id}/estado")
    public ResponseEntity<String> cambiarEstado(
            @PathVariable Long id,
            @RequestParam String nuevoEstado) {
        pedidoService.cambiarEstado(id, nuevoEstado);
        return ResponseEntity.ok("Estado actualizado correctamente");
    }
}