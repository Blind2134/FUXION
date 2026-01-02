package com.fuxion.inventario.repository;

import com.fuxion.inventario.model.entity.Almacen;
import com.fuxion.inventario.model.entity.Envio;
import com.fuxion.inventario.model.entity.Usuario;
import com.fuxion.inventario.model.enums.EstadoEnvio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnvioRepository extends JpaRepository<Envio, Long> {

    // 1. ENVÍOS POR RECIBIR (Para ti)
    // "Dame los envíos que vienen a MI almacén y están EN TRÁNSITO"
    List<Envio> findByAlmacenDestinoAndEstado(Almacen almacenDestino, EstadoEnvio estado);

    // 2. HISTORIAL DE ENVÍOS (Para el Socio)
    // "Dame los envíos que YO (Jeampier) hice"
    List<Envio> findBySocioEmisor(Usuario socioEmisor);
}