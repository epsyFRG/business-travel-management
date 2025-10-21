package com.emiliano.business_travel_management.controllers;

import com.emiliano.business_travel_management.entities.Dipendente;
import com.emiliano.business_travel_management.exceptions.ValidationException;
import com.emiliano.business_travel_management.payload.NewDipendenteDTO;
import com.emiliano.business_travel_management.payload.UpdateDipendenteDTO;
import com.emiliano.business_travel_management.services.DipendentiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/dipendenti")
public class DipendentiController {

    @Autowired
    private DipendentiService dipendentiService;

    
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public Page<Dipendente> findAll(@RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "10") int size,
                                    @RequestParam(defaultValue = "id") String sortBy) {
        return this.dipendentiService.findAll(page, size, sortBy);
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ADMIN')")
    public Dipendente createDipendente(@RequestBody @Validated NewDipendenteDTO payload, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            throw new ValidationException(validationResult.getFieldErrors()
                    .stream().map(fieldError -> fieldError.getDefaultMessage()).toList());
        }
        return this.dipendentiService.save(payload);
    }

    @GetMapping("/me")
    public Dipendente getMyProfile(@AuthenticationPrincipal Dipendente currentDipendente) {
        return currentDipendente;
    }

    @GetMapping("/{dipendenteId}")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public Dipendente findById(@PathVariable UUID dipendenteId) {
        return this.dipendentiService.findById(dipendenteId);
    }

    @PutMapping("/{dipendenteId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Dipendente findByIdAndUpdate(@PathVariable UUID dipendenteId,
                                        @RequestBody @Validated UpdateDipendenteDTO payload,
                                        BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            throw new ValidationException(validationResult.getFieldErrors()
                    .stream().map(fieldError -> fieldError.getDefaultMessage()).toList());
        }
        return this.dipendentiService.findByIdAndUpdate(dipendenteId, payload);
    }

    @DeleteMapping("/{dipendenteId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ADMIN')")
    public void findByIdAndDelete(@PathVariable UUID dipendenteId) {
        this.dipendentiService.findByIdAndDelete(dipendenteId);
    }

    @PatchMapping("/me/immagine")
    public String uploadMyImage(@AuthenticationPrincipal Dipendente currentDipendente,
                                @RequestParam("immagine") MultipartFile file) {
        return this.dipendentiService.uploadImmagineProfilo(currentDipendente.getId(), file);
    }

    @PatchMapping("/{dipendenteId}/immagine")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String uploadImmagineProfilo(@PathVariable UUID dipendenteId,
                                        @RequestParam("immagine") MultipartFile file) {
        return this.dipendentiService.uploadImmagineProfilo(dipendenteId, file);
    }
}