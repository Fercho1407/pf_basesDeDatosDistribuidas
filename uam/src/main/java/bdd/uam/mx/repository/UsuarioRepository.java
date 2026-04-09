package bdd.uam.mx.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import bdd.uam.mx.model.Usuario;


public interface UsuarioRepository extends JpaRepository<Usuario, String>{
    Usuario findByUsername(String username);
}
