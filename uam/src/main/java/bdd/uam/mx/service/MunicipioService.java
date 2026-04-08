package bdd.uam.mx.service;

import java.util.List;

import org.springframework.stereotype.Service;

import bdd.uam.mx.DTO.MunicipioResponseDTO;
import bdd.uam.mx.repository.MunicipioRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MunicipioService {
    private final MunicipioRepository municipioRepository;

    public List<MunicipioResponseDTO> getMunicipiosByEntidadFederativa(String abreviatura){
        return municipioRepository.findByAbreviaturaCustom(abreviatura);
    }
}
