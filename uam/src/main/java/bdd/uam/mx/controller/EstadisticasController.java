package bdd.uam.mx.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import bdd.uam.mx.DTO.ConteoPersonasEntidadDTO;
import bdd.uam.mx.DTO.EscolaridadPorEntidadDTO;
import bdd.uam.mx.DTO.HogaresPorMunicipioDTO;
import bdd.uam.mx.DTO.SueldoPromedioEntidadDTO;
import bdd.uam.mx.service.EstadisticasService;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
public class EstadisticasController {
    
    private final EstadisticasService estadisticasService;

    @GetMapping("habitantes-entidades")
    public ResponseEntity<List<ConteoPersonasEntidadDTO>> getNumPersonasByEntidad() {
        return new ResponseEntity<>(estadisticasService.getNumPersonasByEntidadGlobal(), HttpStatus.OK);
    }

    @GetMapping("salario-promedio")
    public ResponseEntity<List<SueldoPromedioEntidadDTO>> getSaldoPromedioEntidad() {
        return new ResponseEntity<>(estadisticasService.getSalariosPromedioEntidadGlobal(), HttpStatus.OK);
    }

    @GetMapping("hogares-municipio")
    public ResponseEntity<List<HogaresPorMunicipioDTO>> getNumHogaresByMunicipio() {
        return new ResponseEntity<>(estadisticasService.getNumHogaresByMunicipioGlobal(), HttpStatus.OK);
    }

    @GetMapping("escolaridad-entidad/{abreviatura}")
    public ResponseEntity<List<EscolaridadPorEntidadDTO>> getEscolaridadByEntidad(@PathVariable String abreviatura) {
        return new ResponseEntity<>(estadisticasService.getEscolaridadByEntidadGlobal(abreviatura), HttpStatus.OK);
    }
    

}
