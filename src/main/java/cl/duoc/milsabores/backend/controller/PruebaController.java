package cl.duoc.milsabores.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PruebaController {

    @GetMapping("/api/v1/prueba")
    public String probarBackend() {
        return "Backend de Pasteleria Mil Sabores funcionando correctamente";
    }
}