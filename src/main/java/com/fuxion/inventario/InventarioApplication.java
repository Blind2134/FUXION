package com.fuxion.inventario; // Tu paquete puede variar

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone; // <--- IMPORTAR ESTO

@SpringBootApplication
public class InventarioApplication {

    public static void main(String[] args) {
        SpringApplication.run(InventarioApplication.class, args);
    }

    // --- AGREGA ESTE BLOQUE ---
    @PostConstruct
    public void init() {
        // Le decimos a Java: "Tu zona horaria es America/Lima" (o la que corresponda)
        TimeZone.setDefault(TimeZone.getTimeZone("America/Lima"));
        System.out.println("Hora configurada a: " + new java.util.Date());
    }
    // --------------------------
}