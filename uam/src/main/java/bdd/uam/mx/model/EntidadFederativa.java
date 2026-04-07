package bdd.uam.mx.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EntidadFederativa {
    
    @Id
    @Column(length = 5)
    private String abreviatura;

    private String nombre;
}
