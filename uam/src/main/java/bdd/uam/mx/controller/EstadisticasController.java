package bdd.uam.mx.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import bdd.uam.mx.DTO.ConteoPersonasEntidadDTO;
import bdd.uam.mx.service.EstadisticasService;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;


@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
public class EstadisticasController {
    
    private final EstadisticasService estadisticasService;

    @GetMapping("habitantes-entidades")
    public ResponseEntity<List<ConteoPersonasEntidadDTO>> getNumPersonasByEntidad() {
        return new ResponseEntity<>(estadisticasService.getNumPersonasByEntidadGlobal(), HttpStatus.OK);
    }
    
}
