package bdd.uam.mx.service;

import java.util.List;

import org.springframework.stereotype.Service;

import bdd.uam.mx.model.EntidadFederativa;
import bdd.uam.mx.repository.EntidadFedarativaRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Service
@Getter
@RequiredArgsConstructor
public class EntidadFederativaService {
    private final EntidadFedarativaRepository entidadFedarativaRepository;

    public List<EntidadFederativa> getEntidadesFederativas (){
        return entidadFedarativaRepository.findAll();
    }

    
}
