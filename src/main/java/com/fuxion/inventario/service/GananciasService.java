package com.fuxion.inventario.service;

import com.fuxion.inventario.dto.GananciasResumenDTO;
import com.fuxion.inventario.dto.PedidoResponse;

import java.util.List;

public interface GananciasService {

    GananciasResumenDTO obtenerResumen(Long idAlmacenero, Integer mes, Integer anio);

    List<PedidoResponse> obtenerHistorial(Long idAlmacenero);
}