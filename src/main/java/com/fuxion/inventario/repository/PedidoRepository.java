package com.fuxion.inventario.repository;

import com.fuxion.inventario.model.entity.Almacen;
import com.fuxion.inventario.model.entity.Pedido;
import com.fuxion.inventario.model.entity.Usuario;
import com.fuxion.inventario.model.enums.EstadoPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    // 1. PEDIDOS PENDIENTES (Para tu Alerta de "Despachar ya!")
    // "Dame los pedidos de MI almacén que estén PENDIENTES o EMPAQUETADOS"
    List<Pedido> findByAlmacenOrigenAndEstadoIn(Almacen almacen, List<EstadoPedido> estados);

    // 2. MIS VENTAS (Para Jeampier/Betzaida)
    // "Dame todo lo que yo he vendido"
    List<Pedido> findByVendedor(Usuario vendedor);

    // 3. BUSCAR POR CÓDIGO
    Optional<Pedido> findByCodigoPedido(String codigoPedido);

    List<Pedido> findByAlmacenOrigen(Almacen almacen);
    List<Pedido> findByAlmacenOrigenAndEstado(Almacen almacen, EstadoPedido estado);
}