package com.fuxion.inventario.controller;

import com.fuxion.inventario.dto.InventarioDTO;
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

    // Para el Dashboard del Socio: "¿Qué tengo en Arequipa?"
    // GET /api/inventario/mis-productos?idAlmacen=1&idDueno=2
    @GetMapping("/mis-productos")
    public ResponseEntity<List<InventarioDTO>> verMiStock(
            @RequestParam Long idAlmacen,
            @RequestParam Long idDueno) {
        // Nota: Necesitarás crear este método "listarStockDueno" en tu Service e Impl
        // que use inventarioRepository.findByAlmacenAndDueno(...) y lo convierta a DTO.
        return ResponseEntity.ok(inventarioService.listarStockDueno(idAlmacen, idDueno));
    }

    // Para Ti (Almacenero): Ver TODO lo que hay en casa
    // GET /api/inventario/global/{idAlmacen}
    @GetMapping("/global/{idAlmacen}")
    public ResponseEntity<List<InventarioDTO>> verStockGlobal(@PathVariable Long idAlmacen) {
        return ResponseEntity.ok(inventarioService.listarTodoStock(idAlmacen));
    }
}