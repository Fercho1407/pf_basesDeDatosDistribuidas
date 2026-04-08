package bdd.uam.mx.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import bdd.uam.mx.DTO.DatosPersonaDTO;
import bdd.uam.mx.DTO.PersonaCreateDTO;
import bdd.uam.mx.DTO.PersonaResponseDTO;
import bdd.uam.mx.config.datasource.Zona;
import bdd.uam.mx.service.PersonaService;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



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
    
    @GetMapping("/personas/{zona}")
    public ResponseEntity<List<DatosPersonaDTO>> obtnerDatosPersonaDTOs(@PathVariable Zona zona) {
        return new ResponseEntity<>(personaService.obtenerPersonasZona(zona), HttpStatus.FOUND);
    }

    @PostMapping("/persona/{zona}/{idPersonaDetalle}/{salario}")
    public ResponseEntity<BigDecimal> actualizarSalario(@PathVariable Zona zona, 
                                              @PathVariable Integer idPersonaDetalle, 
                                              @PathVariable BigDecimal salario) {
        
        
        return new ResponseEntity<>(personaService.actualizarSalario(zona, idPersonaDetalle, salario), HttpStatus.OK);
    }
    
    
}
