package com.emiliano.business_travel_management.controllers;

import com.emiliano.business_travel_management.entities.Dipendente;
import com.emiliano.business_travel_management.entities.Prenotazione;
import com.emiliano.business_travel_management.exceptions.ValidationException;
import com.emiliano.business_travel_management.payload.NewPrenotazioneDTO;
import com.emiliano.business_travel_management.payload.UpdatePrenotazioneDTO;
import com.emiliano.business_travel_management.services.PrenotazioniService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/prenotazioni")
public class PrenotazioniController {

    @Autowired
    private PrenotazioniService prenotazioniService;

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public Page<Prenotazione> findAll(@RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "10") int size,
                                      @RequestParam(defaultValue = "id") String sortBy) {
        return this.prenotazioniService.findAll(page, size, sortBy);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public Prenotazione createPrenotazione(@AuthenticationPrincipal Dipendente currentDipendente,
                                           @RequestBody @Validated NewPrenotazioneDTO payload,
                                           BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            throw new ValidationException(validationResult.getFieldErrors()
                    .stream().map(fieldError -> fieldError.getDefaultMessage()).toList());
        }
        return this.prenotazioniService.save(payload, currentDipendente);
    }

    @GetMapping("/{prenotazioneId}")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public Prenotazione findById(@PathVariable UUID prenotazioneId) {
        return this.prenotazioniService.findById(prenotazioneId);
    }

    @PutMapping("/{prenotazioneId}")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public Prenotazione findByIdAndUpdate(@PathVariable UUID prenotazioneId,
                                          @RequestBody @Validated UpdatePrenotazioneDTO payload,
                                          BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            throw new ValidationException(validationResult.getFieldErrors()
                    .stream().map(fieldError -> fieldError.getDefaultMessage()).toList());
        }
        return this.prenotazioniService.findByIdAndUpdate(prenotazioneId, payload);
    }

    @DeleteMapping("/{prenotazioneId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public void findByIdAndDelete(@PathVariable UUID prenotazioneId) {
        this.prenotazioniService.findByIdAndDelete(prenotazioneId);
    }
}