package com.fuxion.inventario.repository;

import com.fuxion.inventario.model.entity.DetallePedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetallePedidoRepository extends JpaRepository<DetallePedido, Long> {
    // Generalmente se accede desde Pedido, pero esto sirve para reportes de "Productos más vendidos"
}
