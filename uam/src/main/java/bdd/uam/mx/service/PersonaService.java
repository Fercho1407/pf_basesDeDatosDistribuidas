package bdd.uam.mx.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import bdd.uam.mx.DTO.DatosPersonaDTO;
import bdd.uam.mx.DTO.PersonaCreateDTO;
import bdd.uam.mx.DTO.PersonaResponseDTO;
import bdd.uam.mx.config.datasource.Zona;
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


    public List<DatosPersonaDTO> obtenerPersonasZona(Zona zona){
        try{
            ZonaContext.setZona(zona);
            return personaTransactionalService.obtenerDatosPersona();
        }finally{
            ZonaContext.clear();
        }
    }

    public BigDecimal actualizarSalario(Zona zona, Integer idPersonaDetalle, BigDecimal nuevoSalario){
        try{
            ZonaContext.setZona(zona);
            return personaTransactionalService.actualizarSalario(nuevoSalario, idPersonaDetalle);
        }finally{
            ZonaContext.clear();
        }
    }
}