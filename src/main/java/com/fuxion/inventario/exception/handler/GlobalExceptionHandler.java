package com.fuxion.inventario.exception.handler; // O exception.handler

/*

import com.fuxion.inventario.exception.StockInsuficienteException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. Manejo del Error de Stock (Para el Pop-up de Préstamo)
    @ExceptionHandler(StockInsuficienteException.class)
    public ResponseEntity<Map<String, Object>> handleStockInsuficiente(StockInsuficienteException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "STOCK_INSUFICIENTE_CON_PRESTAMO"); // Código clave para React
        response.put("mensaje", ex.getMessage());
        response.put("idPosiblePrestamista", ex.getIdPosiblePrestamista());
        response.put("nombrePrestamista", ex.getNombrePrestamista());

        // Retornamos 409 Conflict (estado lógico) en vez de 500
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    // 2. Manejo de errores generales (Validaciones, IDs no encontrados)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleGeneral(RuntimeException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "ERROR_OPERACION");
        response.put("mensaje", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}


 */