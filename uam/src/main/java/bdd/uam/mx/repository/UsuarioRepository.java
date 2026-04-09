package bdd.uam.mx.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import bdd.uam.mx.model.Usuario;


public interface UsuarioRepository extends JpaRepository<Usuario, String>{
    Optional<Usuario>  findByUsername(String username);
}
