package bdd.uam.mx.DTO;

import java.math.BigDecimal;

import bdd.uam.mx.config.datasource.Zona;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class DatosPersonaDTO {
    private Integer idLocalidad;
    private Zona tipoLocalidad;
    private String direccion;
    private String numeroInterno;
    private String curp;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String sexo;
    private Integer idPersonaDetalle;
    private BigDecimal ingresoMensual;
}
