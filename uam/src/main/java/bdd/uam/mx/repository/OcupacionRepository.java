package bdd.uam.mx.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import bdd.uam.mx.model.Ocupacion;

public interface OcupacionRepository extends JpaRepository<Ocupacion, Integer>{
    
}
