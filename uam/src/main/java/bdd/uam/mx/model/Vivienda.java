package bdd.uam.mx.model;

import jakarta.persistence.Entity;
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
public class Vivienda {
    @Id
    private String direccion;

    private String tipo;
    private String materialPared;
    private String materialTecho;
    private String numeroExterno;
    private String servicioAgua;
    private String servicioLuz;

    @ManyToOne
    @JoinColumn(name = "idLocalidad")
    private Localidad localidad;
}
