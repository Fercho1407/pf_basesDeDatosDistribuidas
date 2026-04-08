package bdd.uam.mx.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class MunicipioResponseDTO {
    private Integer idMunicipio;
    private String nombreMunicipio;
    private String entidadFederativa;
}