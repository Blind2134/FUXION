package com.fuxion.inventario.controller;

import com.fuxion.inventario.model.entity.Almacen;
import com.fuxion.inventario.repository.AlmacenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/almacenes")
@RequiredArgsConstructor
public class AlmacenController {

    private final AlmacenRepository almacenRepository;

    // 1. LISTAR TODOS LOS ALMACENES DISPONIBLES
    // GET http://localhost:8080/api/almacenes
    @GetMapping
    public ResponseEntity<List<Almacen>> listarTodos() {
        return ResponseEntity.ok(almacenRepository.findAll());
    }

    // 2. OBTENER UN ALMACÉN POR ID (Opcional, útil para mostrar nombres en el frontend)
    @GetMapping("/{id}")
    public ResponseEntity<Almacen> obtenerPorId(@PathVariable Long id) {
        return almacenRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}