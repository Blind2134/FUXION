package com.fuxion.inventario.repository;

import com.fuxion.inventario.model.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    // Para buscar rápido en el buscador del pedido
    List<Producto> findByNombreContainingIgnoreCase(String termino);

    // Para validar SKU
    Optional<Producto> findBySku(String sku);
}