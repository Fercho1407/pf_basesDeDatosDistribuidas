package bdd.uam.mx.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import bdd.uam.mx.model.EntidadFederativa;
import bdd.uam.mx.service.EntidadFederativaService;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;



@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
public class EntidadFederativaController {

    private final EntidadFederativaService entidadFederativaService;

    @GetMapping("entidades-federativas")
    public ResponseEntity<List<EntidadFederativa>> getEntidadesFederativas() {
        var entidades = entidadFederativaService.getEntidadesFederativas();
        return new ResponseEntity<>(entidades, HttpStatus.OK);
    }
    
}
