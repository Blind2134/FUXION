package com.fuxion.inventario.service;

import com.fuxion.inventario.dto.EntradaStockRequest;
import com.fuxion.inventario.dto.InventarioDTO;
import com.fuxion.inventario.dto.MovimientoStockDTO;
import com.fuxion.inventario.model.entity.Almacen;
import com.fuxion.inventario.model.entity.Producto;
import com.fuxion.inventario.model.entity.Usuario;

import java.util.List;

public interface InventarioService {

    // MÉTODOS INTERNOS (Los usa PedidoService)
    void descontarStockEstricto(Almacen almacen, Usuario vendedor, Producto producto, Integer cantidad);
    void aumentarStock(Almacen almacen, Usuario dueno, Producto producto, Integer cantidad);
    Integer consultarStockActual(Almacen almacen, Usuario dueno, Producto producto);

    // MÉTODOS PÚBLICOS PARA MVP
    void registrarEntrada(EntradaStockRequest request);
    List<InventarioDTO> listarTodoStock(Long idAlmacen);
    List<MovimientoStockDTO> listarMovimientos(Long idAlmacen);
}