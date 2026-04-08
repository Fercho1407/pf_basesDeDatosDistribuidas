package bdd.uam.mx.DTO;

import java.math.BigDecimal;

import bdd.uam.mx.config.datasource.Zona;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class PersonaCreateDTO {
    //municipio
    private Integer idMunicipio;
    
    //Localidad
    private String nombrelocalida;
    private Zona tipoLocalidad;
    
    //Vivienda
    private String direccion;
    private String tipoVivienda;
    private String materialPared;
    private String materialTecho;
    private String numeroExterno;
    private String servicioAgua;
    private String servicioLuz;
    //Hogar
    private String numeroInterno;
    private String tipoHogar;
    //Persona
    private String curp;
    private String nombrePersona;
    private String sexoPersona;
    private Integer edadPersona;
    private String parentezco;
    private String esJefeHogar;
    private String apellidoMaterno;
    private String apellidoPaterno;
    //Persona Detalle
    private BigDecimal ingresoMensual;
    private String hablaLenguaIndigena;
    //Ocupacion
    private String descripcionOcupacion;
    //Escolaridad
    private String descripcionEscolaridad;
}
