package bdd.uam.mx.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import bdd.uam.mx.model.PersonaDetalle;

public interface PersonaDetalleRepository extends JpaRepository<PersonaDetalle, Integer>{
    
}
