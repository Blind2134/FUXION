package com.fuxion.inventario.repository;

import com.fuxion.inventario.model.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // Para el Login
    Optional<Usuario> findByEmail(String email);

    // Para validar que no se repitan correos al registrar
    boolean existsByEmail(String email);

    // Para listar solo socios o solo almaceneros
    List<Usuario> findByRolNombre(String nombreRol);
}