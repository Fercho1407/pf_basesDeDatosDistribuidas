package bdd.uam.mx.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import bdd.uam.mx.model.Usuario;
import bdd.uam.mx.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    
    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username){
        Usuario usuario = usuarioRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));


        return User.builder()
                .username(usuario.getUsername())
                .password(usuario.getPassword())
                .disabled(!usuario.getActivo()) // Si activo es false, bloquea el login
                .authorities("EMPLEADO") // Rol por defecto.
                .build();
    }
}