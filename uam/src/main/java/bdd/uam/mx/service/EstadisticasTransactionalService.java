package bdd.uam.mx.service;

import java.util.List;

import org.springframework.transaction.annotation.Propagation; // <-- Ojo, usa el de Spring
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import bdd.uam.mx.DTO.ConteoPersonasEntidadDTO;
import bdd.uam.mx.DTO.EscolaridadPorEntidadDTO;
import bdd.uam.mx.DTO.HogaresPorMunicipioDTO;
import bdd.uam.mx.DTO.SueldoPromedioEntidadDTO;
import bdd.uam.mx.repository.EntidadFedarativaRepository;
import bdd.uam.mx.repository.EscolaridadRepository;
import bdd.uam.mx.repository.HogarRepository;
import bdd.uam.mx.repository.PersonaRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EstadisticasTransactionalService {
    private final PersonaRepository personaRepository;
    private final EntidadFedarativaRepository entidadFedarativaRepository;
    private final HogarRepository hogarRepository;
    private final EscolaridadRepository escolaridadRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public List<ConteoPersonasEntidadDTO> getNumPersonasByEntidad(){
        return personaRepository.findConteoPersonasEntidad();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public List<SueldoPromedioEntidadDTO> getSalariosPromedioEntidad(){
        return entidadFedarativaRepository.findPromedioIngresosPorEntidad();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public List<HogaresPorMunicipioDTO> getNumHogaresByMunicipio(){
        return hogarRepository.finByNumHogaresMunicipio();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public List<EscolaridadPorEntidadDTO> getEscolaridadByEntidad(String abreviatura){
        return escolaridadRepository.findEscolaridadByEntidad(abreviatura);
    }
}
