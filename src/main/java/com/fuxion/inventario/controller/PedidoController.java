package com.fuxion.inventario.controller;

import com.fuxion.inventario.dto.PedidoRequest;
import com.fuxion.inventario.dto.PedidoResponse;
import com.fuxion.inventario.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

    // 1. CREAR NUEVO PEDIDO
    // POST /api/pedidos
    @PostMapping
    public ResponseEntity<PedidoResponse> crearPedido(@RequestBody PedidoRequest request) {
        return ResponseEntity.ok(pedidoService.registrarPedido(request));
    }

    // 2. LISTAR PEDIDOS DE MI ALMACÉN
    // GET /api/pedidos?idAlmacen=1&estado=PENDIENTE
    @GetMapping
    public ResponseEntity<List<PedidoResponse>> listarPedidos(
            @RequestParam Long idAlmacen,
            @RequestParam(required = false) String estado) {
        return ResponseEntity.ok(pedidoService.listarPedidos(idAlmacen, estado));
    }

    // 3. ALISTAR PEDIDO (Cambiar a EMPAQUETADO)
    // PUT /api/pedidos/5/alistar
    @PutMapping("/{id}/alistar")
    public ResponseEntity<String> alistar(@PathVariable Long id) {
        pedidoService.cambiarEstado(id, "EMPAQUETADO");
        return ResponseEntity.ok("Pedido alistado correctamente");
    }

    // 4. MARCAR COMO ENTREGADO (Calcula comisión)
    // PUT /api/pedidos/5/entregar
    @PutMapping("/{id}/entregar")
    public ResponseEntity<String> marcarEntregado(@PathVariable Long id) {
        pedidoService.cambiarEstado(id, "ENTREGADO");
        return ResponseEntity.ok("Pedido entregado. Comisión registrada.");
    }

    // 5. VER DETALLE DE UN PEDIDO
    // GET /api/pedidos/5
    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponse> verDetalle(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.obtenerPorId(id));
    }
}