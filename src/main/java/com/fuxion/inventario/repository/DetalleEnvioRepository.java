package com.fuxion.inventario.repository;

import com.fuxion.inventario.model.entity.DetalleEnvio;
import com.fuxion.inventario.model.entity.Envio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetalleEnvioRepository extends JpaRepository<DetalleEnvio, Long> {

    // Método necesario para saber qué productos hay dentro de un Envío específico
    List<DetalleEnvio> findByEnvio(Envio envio);
}