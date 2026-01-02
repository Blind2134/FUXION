package com.fuxion.inventario.service;

import com.fuxion.inventario.dto.PedidoRequest;
import com.fuxion.inventario.dto.PedidoResponse;

import java.util.List;

public interface PedidoService {

    PedidoResponse registrarPedido(PedidoRequest request);

    void cambiarEstado(Long idPedido, String nuevoEstado);

    List<PedidoResponse> listarPedidos(Long idAlmacen, String estado);

    PedidoResponse obtenerPorId(Long idPedido);
}