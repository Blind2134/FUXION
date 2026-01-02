package com.fuxion.inventario.service.impl;

import com.fuxion.inventario.dto.EnvioRequest;
import com.fuxion.inventario.dto.EnvioResponse;
import com.fuxion.inventario.model.entity.*;
import com.fuxion.inventario.model.enums.EstadoEnvio;
import com.fuxion.inventario.repository.*;
import com.fuxion.inventario.service.EnvioService;
import com.fuxion.inventario.service.InventarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fuxion.inventario.dto.DetalleEnvioDTO;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EnvioServiceImpl implements EnvioService {

    private final EnvioRepository envioRepository;
    private final DetalleEnvioRepository detalleEnvioRepository;
    private final UsuarioRepository usuarioRepository;
    private final AlmacenRepository almacenRepository;
    private final ProductoRepository productoRepository;

    // ¡CRÍTICO! Usamos el servicio de inventario para sumar stock al recibir
    private final InventarioService inventarioService;

    @Override
    @Transactional
    public EnvioResponse registrarEnvio(EnvioRequest request) {

        // 1. Validar Usuario y Almacén
        Usuario socio = usuarioRepository.findById(request.getIdSocioEmisor())
                .orElseThrow(() -> new RuntimeException("Socio no encontrado"));

        Almacen almacen = almacenRepository.findById(request.getIdAlmacenDestino())
                .orElseThrow(() -> new RuntimeException("Almacén destino no encontrado"));

        // 2. Crear Cabecera
        Envio envio = Envio.builder()
                .codigoRastreo(request.getCodigoRastreo()) // Ej: "SHALOM-5050"
                .fechaEnvio(LocalDateTime.now())
                .socioEmisor(socio)
                .almacenDestino(almacen)
                .estado(EstadoEnvio.EN_TRANSITO)
                .observacion(request.getObservacion())
                .build();

        envio = envioRepository.save(envio);

        Integer totalItems = 0;

        // 3. Guardar Detalles (Qué productos vienen en la caja)
        for (EnvioRequest.ItemEnvio item : request.getItems()) {
            Producto producto = productoRepository.findById(item.getIdProducto())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado ID: " + item.getIdProducto()));

            DetalleEnvio detalle = DetalleEnvio.builder()
                    .envio(envio)
                    .producto(producto)
                    .cantidadEnviada(item.getCantidad())
                    .cantidadRecibida(0) // Aún no se recibe nada
                    .build();

            detalleEnvioRepository.save(detalle);
            totalItems += item.getCantidad();
        }

        // Retornar respuesta simple
        return mapToResponse(envio, totalItems);
    }

    @Override
    @Transactional
    public void recepcionarEnvio(Long idEnvio, String observacion) {

        // 1. Buscar el envío
        Envio envio = envioRepository.findById(idEnvio)
                .orElseThrow(() -> new RuntimeException("Envío no encontrado"));

        // Evitar procesarlo dos veces
        if (envio.getEstado() == EstadoEnvio.RECIBIDO) {
            throw new RuntimeException("Este envío ya fue recepcionado anteriormente.");
        }

        // 2. Recorrer detalles y ACTUALIZAR STOCK
        List<DetalleEnvio> detalles = detalleEnvioRepository.findByEnvio(envio); // Necesitas este método en el repo o usar envio.getDetalles() si tienes FetchType.EAGER

        for (DetalleEnvio detalle : detalles) {

            // Asumimos que llegó todo lo que se envió (Opción simple).
            // Si quisieras reportar pérdidas, aquí pasarías una cantidad diferente.
            int cantidadRecibida = detalle.getCantidadEnviada();

            detalle.setCantidadRecibida(cantidadRecibida);
            detalleEnvioRepository.save(detalle);

            // AUMENTAR STOCK AL SOCIO CORRECTO EN TU ALMACÉN
            inventarioService.aumentarStock(
                    envio.getAlmacenDestino(),
                    envio.getSocioEmisor(), // El stock sigue siendo de Jeampier
                    detalle.getProducto(),
                    cantidadRecibida
            );
        }

        // 3. Cerrar el envío
        envio.setFechaRecepcion(LocalDateTime.now());
        envio.setEstado(EstadoEnvio.RECIBIDO);
        envio.setObservacion(envio.getObservacion() + " | Recepción: " + observacion);

        envioRepository.save(envio);
    }

    @Override
    public List<EnvioResponse> listarPorRecibir(Long idAlmacen) {
        Almacen almacen = almacenRepository.findById(idAlmacen)
                .orElseThrow(() -> new RuntimeException("Almacén no existe"));

        // Asumiendo que creaste el método en el Repo como te indiqué antes
        List<Envio> envios = envioRepository.findByAlmacenDestinoAndEstado(almacen, EstadoEnvio.EN_TRANSITO);

        return envios.stream()
                .map(e -> mapToResponse(e, 0)) // El totalItems puede ser 0 o calcularlo si quieres detalle
                .collect(Collectors.toList());
    }

    @Override
    public List<EnvioResponse> listarMisEnvios(Long idSocio) {
        Usuario socio = usuarioRepository.findById(idSocio).orElseThrow();
        List<Envio> envios = envioRepository.findBySocioEmisor(socio);

        return envios.stream()
                .map(e -> mapToResponse(e, 0))
                .collect(Collectors.toList());
    }
    @Override
    public List<DetalleEnvioDTO> listarDetalles(Long idEnvio) {
        Envio envio = envioRepository.findById(idEnvio)
                .orElseThrow(() -> new RuntimeException("Envío no encontrado"));

        List<DetalleEnvio> detalles = detalleEnvioRepository.findByEnvio(envio);

        return detalles.stream()
                .map(d -> DetalleEnvioDTO.builder()
                        .idProducto(d.getProducto().getIdProducto())
                        .nombreProducto(d.getProducto().getNombre())
                        .cantidad(d.getCantidadEnviada())
                        // Agregamos esto para que React sepa dividir
                        .sticksPorCaja(d.getProducto().getSticksPorCaja())
                        .build())
                .collect(Collectors.toList());
    }


    // Método auxiliar para convertir Entidad a DTO
    private EnvioResponse mapToResponse(Envio envio, Integer totalItemsCalc) {
        return EnvioResponse.builder()
                .idEnvio(envio.getIdEnvio())
                .fechaEnvio(envio.getFechaEnvio())
                .nombreSocio(envio.getSocioEmisor().getNombre())
                .codigoRastreo(envio.getCodigoRastreo())
                .estado(envio.getEstado().name())
                .observacion(envio.getObservacion())
                .totalItems(totalItemsCalc)
                .build();
    }
}