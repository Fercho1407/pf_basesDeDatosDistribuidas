package bdd.uam.mx.service;

import java.util.List;

import org.springframework.transaction.annotation.Propagation; // <-- Ojo, usa el de Spring
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import bdd.uam.mx.DTO.ConteoPersonasEntidadDTO;
import bdd.uam.mx.repository.PersonaRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EstadisticasTransactionalService {
    private final PersonaRepository personaRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public List<ConteoPersonasEntidadDTO> getNumPersonasByEntidad(){
        return personaRepository.findConteoPersonasEntidad();
    }
}
