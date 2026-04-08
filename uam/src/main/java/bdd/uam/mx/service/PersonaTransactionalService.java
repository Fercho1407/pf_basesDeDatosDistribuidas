package bdd.uam.mx.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import bdd.uam.mx.DTO.DatosPersonaDTO;
import bdd.uam.mx.DTO.PersonaCreateDTO;
import bdd.uam.mx.DTO.PersonaResponseDTO;
import bdd.uam.mx.config.datasource.ZonaContext;
import bdd.uam.mx.model.Escolaridad;
import bdd.uam.mx.model.Hogar;
import bdd.uam.mx.model.Localidad;
import bdd.uam.mx.model.Municipio;
import bdd.uam.mx.model.Ocupacion;
import bdd.uam.mx.model.Persona;
import bdd.uam.mx.model.PersonaDetalle;
import bdd.uam.mx.model.Vivienda;
import bdd.uam.mx.repository.EscolaridadRepository;
import bdd.uam.mx.repository.HogarRepository;
import bdd.uam.mx.repository.LocalidadRepository;
import bdd.uam.mx.repository.MunicipioRepository;
import bdd.uam.mx.repository.OcupacionRepository;
import bdd.uam.mx.repository.PersonaDetalleRepository;
import bdd.uam.mx.repository.PersonaRepository;
import bdd.uam.mx.repository.ViviendaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PersonaTransactionalService {
    private final PersonaRepository personaRepository;
    private final MunicipioRepository municipioRepository;
    private final LocalidadRepository localidadRepository;
    private final ViviendaRepository viviendaRepository;
    private final HogarRepository hogarRepository;
    private final PersonaDetalleRepository personaDetalleRepository;
    private final OcupacionRepository ocupacionRepository;
    private final EscolaridadRepository escolaridadRepository;

    @Transactional
    public PersonaResponseDTO savePersona(PersonaCreateDTO personaDTO){
        try{
            ZonaContext.clear();
            ZonaContext.setZona(personaDTO.getTipoLocalidad());

            Municipio municipio = municipioRepository.findById(personaDTO.getIdMunicipio())
                    .orElseThrow(() -> new RuntimeException("Municipio no encontrado"));

            Localidad localidad = localidadRepository.save(new Localidad(
                null,
                personaDTO.getNombrelocalida(),
                personaDTO.getTipoLocalidad(),
                municipio
            ));

            Vivienda vivienda = viviendaRepository.save(
                new Vivienda(
                    personaDTO.getDireccion(),
                    personaDTO.getTipoVivienda(),
                    personaDTO.getMaterialPared(),
                    personaDTO.getMaterialTecho(),
                    personaDTO.getNumeroExterno(),
                    personaDTO.getServicioAgua(),
                    personaDTO.getServicioLuz(),
                    localidad
                )
            );

            Persona persona = new Persona();
            persona.setCurp(personaDTO.getCurp());
            persona.setNombre(personaDTO.getNombrePersona());
            persona.setSexo(personaDTO.getSexoPersona());
            persona.setEdad(personaDTO.getEdadPersona());
            persona.setParentezco(personaDTO.getParentezco());
            persona.setEsJefeHogar(personaDTO.getEsJefeHogar());
            persona.setApellidoPaterno(personaDTO.getApellidoPaterno());
            persona.setApellidoMaterno(personaDTO.getApellidoMaterno());

            persona = personaRepository.save(persona);


            Hogar hogar = new Hogar();
            hogar.setNumeroInterno(personaDTO.getNumeroInterno());
            hogar.setTipo(personaDTO.getTipoHogar());
            hogar.setVivienda(vivienda);
            
            List<Persona> listaPersonas = new ArrayList<>();
            listaPersonas.add(persona);
            hogar.setPersonas(listaPersonas);
            
            hogar = hogarRepository.save(hogar);

            PersonaDetalle personaDetalle = personaDetalleRepository.save(new PersonaDetalle(
                null,
                personaDTO.getIngresoMensual(),
                personaDTO.getHablaLenguaIndigena(),
                persona
            ));

            ocupacionRepository.save(new Ocupacion(
                null,
                personaDTO.getDescripcionOcupacion(),
                personaDetalle
            ));

            escolaridadRepository.save(new Escolaridad(
                null,
                personaDTO.getDescripcionEscolaridad(),
                personaDetalle
            ));
            
            return new PersonaResponseDTO(persona.getNombre(),persona.getApellidoPaterno());

        }finally{
            ZonaContext.clear();
        }
    }


    @Transactional
    public List<DatosPersonaDTO> obtenerDatosPersona(){
        return personaRepository.findByDatosPersonaDTOs();
    }

    @Transactional
    public BigDecimal actualizarSalario(BigDecimal nuevoSalario, Integer idPersonaDetalle){
        PersonaDetalle personaDetalle = personaDetalleRepository.findById(idPersonaDetalle)
                                        .orElseThrow(() -> new RuntimeException("No se encontro salario anterior")) ;
        personaDetalle.setIngresoMensual(nuevoSalario);
        return personaDetalleRepository.save(personaDetalle).getIngresoMensual();
    }
}
