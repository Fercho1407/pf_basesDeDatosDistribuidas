package bdd.uam.mx.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Hogar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idHogar;

    private String numeroInterno;
    private String tipo;

    @ManyToOne
    @JoinColumn(name = "idVivienda")
    private Vivienda vivienda;

    @ManyToMany
    @JoinTable(
        name = "hogar_persona",
        joinColumns = @JoinColumn(name = "id_hogar"),
        inverseJoinColumns = @JoinColumn(name = "curp")
    )
    private List<Persona> personas;
}
