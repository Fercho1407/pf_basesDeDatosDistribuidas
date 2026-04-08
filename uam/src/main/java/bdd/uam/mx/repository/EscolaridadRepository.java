package bdd.uam.mx.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import bdd.uam.mx.DTO.EscolaridadPorEntidadDTO;
import bdd.uam.mx.model.Escolaridad;

public interface EscolaridadRepository extends JpaRepository<Escolaridad, Integer>{
    String query = """
            SELECT new bdd.uam.mx.DTO.EscolaridadPorEntidadDTO(
                e.descripcion,
                COUNT(DISTINCT p.curp)
            )
            FROM Escolaridad e
            JOIN e.personaDetalle pd
            JOIN pd.persona p
            JOIN p.hogares h
            JOIN h.vivienda v
            JOIN v.localidad l
            JOIN l.municipio m
            JOIN m.entidadFederativa ef
            WHERE ef.abreviatura = :abreviatura
            GROUP BY e.idEscolaridad, e.descripcion
            """;
    @Query(query)
    List<EscolaridadPorEntidadDTO> findEscolaridadByEntidad(@Param("abreviatura") String abreviatura);
}
