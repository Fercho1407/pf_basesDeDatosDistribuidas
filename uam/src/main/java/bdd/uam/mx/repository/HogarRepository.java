package bdd.uam.mx.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import bdd.uam.mx.model.Hogar;

public interface HogarRepository extends JpaRepository<Hogar, Integer>{
    
}
