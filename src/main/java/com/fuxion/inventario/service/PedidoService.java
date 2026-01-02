package com.fuxion.inventario.service;

import com.fuxion.inventario.dto.PedidoRequest;
import com.fuxion.inventario.dto.PedidoResponse;

public interface PedidoService {
    PedidoResponse registrarPedido(PedidoRequest request);
    void cambiarEstado(Long idPedido,String nuevoEstado); // Para marcar "ENTREGADO"
}