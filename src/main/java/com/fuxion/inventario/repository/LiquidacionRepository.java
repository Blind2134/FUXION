package com.fuxion.inventario.repository;

import com.fuxion.inventario.model.entity.Liquidacion;
import com.fuxion.inventario.model.entity.Usuario;
import com.fuxion.inventario.model.enums.EstadoLiquidacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LiquidacionRepository extends JpaRepository<Liquidacion, Long> {

    // Ver mis liquidaciones pendientes (Tú cobrando)
    List<Liquidacion> findByAlmaceneroAndEstado(Usuario almacenero, EstadoLiquidacion estado);
}
