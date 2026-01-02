package com.fuxion.inventario.controller;

// Clase comentada temporalmente por petición del usuario (no se utiliza por el momento)
/*
@RestController
@RequestMapping("/api/envios")
@RequiredArgsConstructor
public class EnvioController {

    private final EnvioService envioService;

    // 1. REGISTRAR QUE SE ENVIÓ UNA CAJA (Lo hace Jeampier)
    @PostMapping
    public ResponseEntity<EnvioResponse> registrarEnvio(@RequestBody EnvioRequest request) {
        return ResponseEntity.ok(envioService.registrarEnvio(request));
    }

    // 2. CONFIRMAR RECEPCIÓN (Lo haces tú y suma stock)
    // PUT http://localhost:8080/api/envios/10/recepcionar?observacion=TodoOK
    @PutMapping("/{id}/recepcionar")
    public ResponseEntity<String> recepcionar(
            @PathVariable Long id,
            @RequestParam(required = false, defaultValue = "Sin observaciones") String observacion) {
        envioService.recepcionarEnvio(id, observacion);
        return ResponseEntity.ok("Envío recepcionado y stock sumado correctamente.");
    }

    // 3. VER QUÉ ENVÍOS ESTÁN POR LLEGAR A TU ALMACÉN
    @GetMapping("/por-recibir/{idAlmacen}")
    public ResponseEntity<List<EnvioResponse>> listarPorRecibir(@PathVariable Long idAlmacen) {
        return ResponseEntity.ok(envioService.listarPorRecibir(idAlmacen));
    }

    // 4. VER MIS ENVÍOS (Jeampier ve qué ha mandado)
    @GetMapping("/mis-envios/{idSocio}")
    public ResponseEntity<List<EnvioResponse>> listarMisEnvios(@PathVariable Long idSocio) {
        return ResponseEntity.ok(envioService.listarMisEnvios(idSocio));
    }

    // NUEVO: Ver el contenido detallado de un envío
    // GET /api/envios/1/detalles
    @GetMapping("/{id}/detalles")
    public ResponseEntity<List<DetalleEnvioDTO>> verDetallesEnvio(@PathVariable Long id) {
        return ResponseEntity.ok(envioService.listarDetalles(id));
    }
}
*/
