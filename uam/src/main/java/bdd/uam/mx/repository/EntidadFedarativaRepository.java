package bdd.uam.mx.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import bdd.uam.mx.DTO.SueldoPromedioEntidadDTO;
import bdd.uam.mx.model.EntidadFederativa;

public interface EntidadFedarativaRepository extends JpaRepository<EntidadFederativa, String>{
    @Query("SELECT new bdd.uam.mx.DTO.SueldoPromedioEntidadDTO(" +
           "ef.nombre, " +
           "ROUND(AVG(pd.ingresoMensual), 2), " +
           "COUNT(DISTINCT p.curp)) "+
           "FROM PersonaDetalle pd " +
           "JOIN pd.persona p " +
           "JOIN p.hogares h " +
           "JOIN h.vivienda v " +
           "JOIN v.localidad l " +
           "JOIN l.municipio m " +
           "JOIN m.entidadFederativa ef " +
           "GROUP BY ef.nombre, ef.abreviatura")
    List<SueldoPromedioEntidadDTO> findPromedioIngresosPorEntidad();
}
