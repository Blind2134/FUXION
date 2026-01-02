package com.fuxion.inventario.repository;

import com.fuxion.inventario.model.entity.Almacen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlmacenRepository extends JpaRepository<Almacen, Long> {
    // Para que tú (Bryan) veas TUS almacenes al entrar
    List<Almacen> findByEncargadoIdUsuario(Long idUsuario);
}