package com.fuxion.inventario.service;

import com.fuxion.inventario.model.entity.Prestamo;
import java.util.List;

public interface PrestamoService {
    void solicitarPrestamo(Long idAlmacen, Long idDeudor, Long idAcreedor, Long idProducto, Integer cantidad);
    void aprobarPrestamo(Long idPrestamo);
    List<Prestamo> listarPendientes(Long idAlmacen);
}