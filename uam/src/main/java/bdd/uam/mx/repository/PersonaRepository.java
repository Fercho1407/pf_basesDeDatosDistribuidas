package bdd.uam.mx.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import bdd.uam.mx.DTO.ConteoPersonasEntidadDTO;
import bdd.uam.mx.DTO.DatosPersonaDTO;
import bdd.uam.mx.model.Persona;

public interface PersonaRepository extends JpaRepository<Persona, String>{
    String query = """
            SELECT new bdd.uam.mx.DTO.DatosPersonaDTO
            (l.idLocalidad, l.tipo, v.direccion, h.numeroInterno, p.curp,
             p.nombre, p.apellidoPaterno, p.apellidoMaterno, p.sexo, pd.idPersonaDetalle, pd.ingresoMensual)
            FROM PersonaDetalle pd 
            JOIN pd.persona p
            JOIN p.hogares h
            JOIN h.vivienda v
            JOIN v.localidad l
            """;
    @Query(query)
    List<DatosPersonaDTO> findByDatosPersonaDTOs();
    

    String queryPersonas = """
            SELECT new bdd.uam.mx.DTO.ConteoPersonasEntidadDTO(
                ef.nombre,
                COUNT(DISTINCT p.curp)
            )
            FROM Persona p
            JOIN p.hogares h
            JOIN h.vivienda v
            JOIN v.localidad l
            JOIN l.municipio m
            JOIN m.entidadFederativa ef
            GROUP BY ef.nombre, ef.abreviatura 
            """;

    @Query(queryPersonas)
    List<ConteoPersonasEntidadDTO> findConteoPersonasEntidad();
}
