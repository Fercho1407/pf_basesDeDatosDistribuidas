package bdd.uam.mx.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import bdd.uam.mx.model.Localidad;

public interface LocalidadRepository extends JpaRepository<Localidad, Integer>{
    
}
