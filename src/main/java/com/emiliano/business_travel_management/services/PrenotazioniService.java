package com.emiliano.business_travel_management.services;

import com.emiliano.business_travel_management.entities.Dipendente;
import com.emiliano.business_travel_management.entities.Prenotazione;
import com.emiliano.business_travel_management.entities.Viaggio;
import com.emiliano.business_travel_management.exceptions.BadRequestException;
import com.emiliano.business_travel_management.exceptions.NotFoundException;
import com.emiliano.business_travel_management.payload.NewPrenotazioneDTO;
import com.emiliano.business_travel_management.payload.UpdatePrenotazioneDTO;
import com.emiliano.business_travel_management.repositories.PrenotazioniRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class PrenotazioniService {

    @Autowired
    private PrenotazioniRepository prenotazioniRepository;

    @Autowired
    private ViaggiService viaggiService;

    @Autowired
    private DipendentiService dipendentiService;

    public Page<Prenotazione> findAll(int pageNumber, int pageSize, String sortBy) {
        if (pageSize > 50) pageSize = 50;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).ascending());
        return this.prenotazioniRepository.findAll(pageable);
    }

    public Prenotazione save(NewPrenotazioneDTO payload) {
        Viaggio viaggio = this.viaggiService.findById(payload.viaggioId());
        Dipendente dipendente = this.dipendentiService.findById(payload.dipendenteId());

        List<Prenotazione> prenotazioniEsistenti = this.prenotazioniRepository.findByDipendenteAndData(
                dipendente.getId(),
                viaggio.getData()
        );

        if (!prenotazioniEsistenti.isEmpty()) {
            throw new BadRequestException("il dipendente ha già una prenotazione per questa data");
        }

        Prenotazione newPrenotazione = new Prenotazione(viaggio, dipendente, payload.note());

        Prenotazione savedPrenotazione = this.prenotazioniRepository.save(newPrenotazione);

        log.info("prenotazione salvata con id: " + savedPrenotazione.getId());

        return savedPrenotazione;
    }

    public Prenotazione findById(UUID prenotazioneId) {
        return this.prenotazioniRepository.findById(prenotazioneId).orElseThrow(() -> new NotFoundException(prenotazioneId));
    }

    public Prenotazione findByIdAndUpdate(UUID prenotazioneId, UpdatePrenotazioneDTO payload) {
        Prenotazione found = this.findById(prenotazioneId);

        found.setNote(payload.note());

        Prenotazione modifiedPrenotazione = this.prenotazioniRepository.save(found);

        log.info("prenotazione modificata con id " + modifiedPrenotazione.getId());

        return modifiedPrenotazione;
    }

    public void findByIdAndDelete(UUID prenotazioneId) {
        Prenotazione found = this.findById(prenotazioneId);
        this.prenotazioniRepository.delete(found);
        log.info("prenotazione eliminata con id " + prenotazioneId);
    }
}