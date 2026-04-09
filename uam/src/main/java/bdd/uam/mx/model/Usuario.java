package bdd.uam.mx.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@AllArgsConstructor
public class Usuario {

    @Id
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private Boolean activo = true;

}