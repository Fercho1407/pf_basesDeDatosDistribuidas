package bdd.uam.mx.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import bdd.uam.mx.DTO.PersonaCreateDTO;
import bdd.uam.mx.DTO.PersonaResponseDTO;
import bdd.uam.mx.service.PersonaService;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
public class PersonaController {

    private final PersonaService personaService;
    
    @PostMapping("/persona")
    public ResponseEntity<PersonaResponseDTO> guardarPersona(@RequestBody PersonaCreateDTO personaDTO) {
        PersonaResponseDTO personaResponseDTO = personaService.savePersona(personaDTO);
        return new ResponseEntity<>(personaResponseDTO, HttpStatus.CREATED);
    }
    
}
