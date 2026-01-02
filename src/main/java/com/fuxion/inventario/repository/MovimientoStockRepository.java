package com.fuxion.inventario.repository;

import com.fuxion.inventario.model.entity.Almacen;
import com.fuxion.inventario.model.entity.MovimientoStock;
import com.fuxion.inventario.model.entity.Producto;
import com.fuxion.inventario.model.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MovimientoStockRepository extends JpaRepository<MovimientoStock, Long> {

    // Para reportes: Ver movimientos de un producto específico en un rango de fechas
    List<MovimientoStock> findByAlmacenAndProductoAndFechaBetween(
            Almacen almacen,
            Producto producto,
            LocalDateTime inicio,
            LocalDateTime fin
    );

    // Para auditoría: Ver todos los movimientos que hizo un dueño
    List<MovimientoStock> findByDueno(Usuario dueno);
}