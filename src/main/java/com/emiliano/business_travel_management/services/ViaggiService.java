package com.emiliano.business_travel_management.services;

import com.emiliano.business_travel_management.entities.Viaggio;
import com.emiliano.business_travel_management.exceptions.NotFoundException;
import com.emiliano.business_travel_management.payload.NewViaggioDTO;
import com.emiliano.business_travel_management.payload.UpdateStatoViaggioDTO;
import com.emiliano.business_travel_management.payload.UpdateViaggioDTO;
import com.emiliano.business_travel_management.repositories.ViaggiRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class ViaggiService {

    @Autowired
    private ViaggiRepository viaggiRepository;

    public Page<Viaggio> findAll(int pageNumber, int pageSize, String sortBy) {
        if (pageSize > 50) pageSize = 50;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).ascending());
        return this.viaggiRepository.findAll(pageable);
    }

    public Viaggio save(NewViaggioDTO payload) {
        Viaggio newViaggio = new Viaggio(payload.destinazione(), payload.data());

        Viaggio savedViaggio = this.viaggiRepository.save(newViaggio);

        log.info("viaggio salvato con id: " + savedViaggio.getId());

        return savedViaggio;
    }

    public Viaggio findById(UUID viaggioId) {
        return this.viaggiRepository.findById(viaggioId).orElseThrow(() -> new NotFoundException(viaggioId));
    }

    public Viaggio findByIdAndUpdate(UUID viaggioId, UpdateViaggioDTO payload) {
        Viaggio found = this.findById(viaggioId);

        found.setDestinazione(payload.destinazione());
        found.setData(payload.data());

        Viaggio modifiedViaggio = this.viaggiRepository.save(found);

        log.info("viaggio modificato con id " + modifiedViaggio.getId());

        return modifiedViaggio;
    }

    public Viaggio updateStato(UUID viaggioId, UpdateStatoViaggioDTO payload) {
        Viaggio found = this.findById(viaggioId);

        found.setStato(payload.stato());

        Viaggio modifiedViaggio = this.viaggiRepository.save(found);

        log.info("stato viaggio aggiornato per id " + modifiedViaggio.getId() + " a: " + payload.stato());

        return modifiedViaggio;
    }

    public void findByIdAndDelete(UUID viaggioId) {
        Viaggio found = this.findById(viaggioId);
        this.viaggiRepository.delete(found);
        log.info("viaggio eliminato con id " + viaggioId);
    }
}