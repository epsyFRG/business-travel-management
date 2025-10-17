package com.emiliano.business_travel_management.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "prenotazioni")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Prenotazione {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "viaggio_id")
    private Viaggio viaggio;

    @ManyToOne
    @JoinColumn(name = "dipendente_id")
    private Dipendente dipendente;

    @Column(name = "data_richiesta")
    private LocalDate dataRichiesta;

    @Column(length = 500)
    private String note;

    public Prenotazione(Viaggio viaggio, Dipendente dipendente, String note) {
        this.viaggio = viaggio;
        this.dipendente = dipendente;
        this.dataRichiesta = LocalDate.now();
        this.note = note;
    }
}
