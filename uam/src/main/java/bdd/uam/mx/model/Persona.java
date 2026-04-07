package bdd.uam.mx.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Persona {
    @Id
    private String curp;

    private String nombre;
    private String sexo;
    private Integer edad;
    private String parentezco;
    private String esJefeHogar;
    private String apellidoMaterno;
    private String apellidoPaterno;

    @ManyToMany(mappedBy = "personas")
    private List<Hogar> hogares;
}
