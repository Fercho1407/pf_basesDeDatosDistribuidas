package bdd.uam.mx.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import bdd.uam.mx.config.datasource.Zona;
import bdd.uam.mx.config.datasource.ZonaContext;
import bdd.uam.mx.model.Usuario;
import bdd.uam.mx.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public void guardarEnTodasLasZonas(Usuario usuario) {
        try {
            guardarEnZona(Zona.RURAL, usuario.getUsername(), usuario.getPassword(), usuario.getActivo());
            guardarEnZona(Zona.SUBURBANA, usuario.getUsername(), usuario.getPassword(), usuario.getActivo());
            guardarEnZona(Zona.URBANA, usuario.getUsername(), usuario.getPassword(), usuario.getActivo());
        } finally {
            ZonaContext.clear();
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void guardarEnZona(Zona zona, String username, String passwordEncriptado, boolean activo) {
        try {
            ZonaContext.setZona(zona);

            Usuario usuario = new Usuario();
            usuario.setUsername(username);
            usuario.setPassword(passwordEncriptado);
            usuario.setActivo(activo);

            usuarioRepository.save(usuario);
        } finally {
            ZonaContext.clear();
        }
    }
}