package com.fuxion.inventario.controller;

import com.fuxion.inventario.dto.ProductoDTO;
import com.fuxion.inventario.dto.ProductoRequest;
import com.fuxion.inventario.model.entity.Producto;
import com.fuxion.inventario.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoRepository productoRepository;

    // 1. LISTAR TODOS (Devuelve DTOs)
    @GetMapping
    public ResponseEntity<List<ProductoDTO>> listar() {
        List<ProductoDTO> lista = productoRepository.findAll()
                .stream()
                .map(this::mapToDTO) // Convertimos cada entidad a DTO
                .collect(Collectors.toList());

        return ResponseEntity.ok(lista);
    }

    // 2. CREAR NUEVO
    @PostMapping
    public ResponseEntity<ProductoDTO> crear(@RequestBody ProductoRequest request) {
        Producto p = new Producto();
        p.setNombre(request.getNombre());
        p.setSku(request.getSku());
        p.setImgUrl(request.getImgUrl());
        p.setSticksPorCaja(request.getSticksPorCaja());
        p.setPrecioReferencial(request.getPrecioReferencial());

        Producto guardado = productoRepository.save(p);
        return ResponseEntity.ok(mapToDTO(guardado));
    }

    // 3. EDITAR EXISTENTE
    @PutMapping("/{id}")
    public ResponseEntity<ProductoDTO> editar(@PathVariable Long id, @RequestBody ProductoRequest request) {
        return productoRepository.findById(id)
                .map(p -> {
                    p.setNombre(request.getNombre());
                    p.setSku(request.getSku());
                    p.setImgUrl(request.getImgUrl());
                    p.setSticksPorCaja(request.getSticksPorCaja());
                    p.setPrecioReferencial(request.getPrecioReferencial());

                    Producto actualizado = productoRepository.save(p);
                    return ResponseEntity.ok(mapToDTO(actualizado));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // 4. ELIMINAR
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        productoRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    // --- MÉTODO AUXILIAR PARA MAPEAR ---
    private ProductoDTO mapToDTO(Producto p) {
        return ProductoDTO.builder()
                .idProducto(p.getIdProducto())
                .nombre(p.getNombre())
                .sku(p.getSku())
                .imgUrl(p.getImgUrl())
                .sticksPorCaja(p.getSticksPorCaja())
                .precioReferencial(p.getPrecioReferencial())
                .build();
    }
}