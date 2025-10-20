package com.emiliano.business_travel_management.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "dipendenti")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Dipendente {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID id;

    private String username;
    private String nome;
    private String cognome;
    private String email;
    private String password;

    @Column(name = "immagine_profilo")
    private String immagineProfilo;

    public Dipendente(String username, String nome, String cognome, String email, @NotBlank(message = "la password è obbligatoria") @Size(min = 4, message = "la password deve avere minimo 4 caratteri") String password) {
        this.username = username;
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.password = this.password;
    }
}