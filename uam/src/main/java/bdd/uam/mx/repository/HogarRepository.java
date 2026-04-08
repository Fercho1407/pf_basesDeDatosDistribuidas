package bdd.uam.mx.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import bdd.uam.mx.DTO.HogaresPorMunicipioDTO;
import bdd.uam.mx.model.Hogar;

public interface HogarRepository extends JpaRepository<Hogar, Integer>{
    
    String query = """
            SELECT new bdd.uam.mx.DTO.HogaresPorMunicipioDTO(
                m.nombre,
                COUNT(h.idHogar)
            )
            FROM Hogar h
            JOIN h.vivienda v
            JOIN v.localidad l
            JOIN l.municipio m
            GROUP BY m.idMunicipio, m.nombre
            """;

    @Query(query)
    List<HogaresPorMunicipioDTO> finByNumHogaresMunicipio();
}
