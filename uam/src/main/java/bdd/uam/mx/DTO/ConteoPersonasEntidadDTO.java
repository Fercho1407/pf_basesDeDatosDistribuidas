package bdd.uam.mx.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ConteoPersonasEntidadDTO {
    public String nombre;
    public Long totalPersonas;
}
