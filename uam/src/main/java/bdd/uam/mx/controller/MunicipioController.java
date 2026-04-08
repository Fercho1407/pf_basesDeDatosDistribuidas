package bdd.uam.mx.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import bdd.uam.mx.DTO.MunicipioResponseDTO;
import bdd.uam.mx.service.MunicipioService;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
public class MunicipioController {
    
    private final MunicipioService municipioService;

    @GetMapping("municipios-entidad/{abreviatura}")
    public ResponseEntity<List<MunicipioResponseDTO>> getMunicipiosByEntidad(@PathVariable String abreviatura) {
        var municipios = municipioService.getMunicipiosByEntidadFederativa(abreviatura);
        return new ResponseEntity<>(municipios, HttpStatus.OK);
    }
    
}
