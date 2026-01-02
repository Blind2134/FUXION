package com.fuxion.inventario.repository;

import com.fuxion.inventario.model.entity.Auditoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuditoriaRepository extends JpaRepository<Auditoria, Long> {

    // 1. Ver historial de un Usuario (Ej: Qué hizo Jeampier hoy)
    List<Auditoria> findByIdUsuarioOrderByFechaDesc(Long idUsuario);

    // 2. Ver historial de un Pedido específico (Trazabilidad)
    // "Quiero saber quién tocó el pedido 505"
    List<Auditoria> findByTablaAfectadaAndIdReferenciaOrderByFechaDesc(String tablaAfectada, Long idReferencia);
}