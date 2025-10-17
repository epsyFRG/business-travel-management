package com.emiliano.business_travel_management.repositories;

import com.emiliano.business_travel_management.entities.Prenotazione;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface PrenotazioniRepository extends JpaRepository<Prenotazione, UUID> {

    @Query("SELECT p FROM Prenotazione p WHERE p.dipendente.id = :dipendenteId AND p.viaggio.data = :data")
    List<Prenotazione> findByDipendenteAndData(UUID dipendenteId, LocalDate data);
}
