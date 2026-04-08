package bdd.uam.mx.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import bdd.uam.mx.model.Persona;

public interface PersonaRepository extends JpaRepository<Persona, String>{
    
}
