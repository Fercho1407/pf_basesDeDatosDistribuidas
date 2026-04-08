package bdd.uam.mx.service;

import org.springframework.stereotype.Service;

import bdd.uam.mx.DTO.PersonaCreateDTO;
import bdd.uam.mx.DTO.PersonaResponseDTO;
import bdd.uam.mx.config.datasource.ZonaContext;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PersonaService {

    private final PersonaTransactionalService personaTransactionalService;

    public PersonaResponseDTO savePersona(PersonaCreateDTO personaDTO) {
        try {
            ZonaContext.setZona(personaDTO.getTipoLocalidad());
            return personaTransactionalService.savePersona(personaDTO);
        } finally {
            ZonaContext.clear();
        }
    }
}