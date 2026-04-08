package bdd.uam.mx.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import bdd.uam.mx.DTO.MunicipioResponseDTO;
import bdd.uam.mx.model.Municipio;

public interface MunicipioRepository extends JpaRepository<Municipio, Integer>{
    String query = """
            SELECT new bdd.uam.mx.DTO.MunicipioResponseDTO (m.idMunicipio, m.nombre, e.nombre)
            FROM Municipio m 
            JOIN entidadFederativa e
            WHERE e.abreviatura = :abreviatura
            """;
    @Query(query)
    List<MunicipioResponseDTO> findByAbreviaturaCustom(@Param("abreviatura") String abreviatura);
}
