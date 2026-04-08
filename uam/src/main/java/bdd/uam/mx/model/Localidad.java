package bdd.uam.mx.model;

import bdd.uam.mx.config.datasource.Zona;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Localidad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idLocalidad;

    private String nombre;
    
    @Enumerated(EnumType.STRING)
    private Zona tipo;

    @ManyToOne
    @JoinColumn(name = "idMunicipio")
    private Municipio municipio;
}
