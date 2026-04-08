package bdd.uam.mx.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SueldoPromedioEntidadDTO {
    private String nombreEntidad;
    private Double salarioPromedio;
    public Long totalPersona;

}
