package com.emiliano.business_travel_management.controllers;

import com.emiliano.business_travel_management.entities.Viaggio;
import com.emiliano.business_travel_management.exceptions.ValidationException;
import com.emiliano.business_travel_management.payload.NewViaggioDTO;
import com.emiliano.business_travel_management.payload.UpdateStatoViaggioDTO;
import com.emiliano.business_travel_management.payload.UpdateViaggioDTO;
import com.emiliano.business_travel_management.services.ViaggiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/viaggi")
public class ViaggiController {

    @Autowired
    private ViaggiService viaggiService;

    @GetMapping
    public Page<Viaggio> findAll(@RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "10") int size,
                                 @RequestParam(defaultValue = "id") String sortBy) {
        return this.viaggiService.findAll(page, size, sortBy);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Viaggio createViaggio(@RequestBody @Validated NewViaggioDTO payload, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            throw new ValidationException(validationResult.getFieldErrors()
                    .stream().map(fieldError -> fieldError.getDefaultMessage()).toList());
        }
        return this.viaggiService.save(payload);
    }

    @GetMapping("/{viaggioId}")
    public Viaggio findById(@PathVariable UUID viaggioId) {
        return this.viaggiService.findById(viaggioId);
    }

    @PutMapping("/{viaggioId}")
    public Viaggio findByIdAndUpdate(@PathVariable UUID viaggioId, @RequestBody @Validated UpdateViaggioDTO payload, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            throw new ValidationException(validationResult.getFieldErrors()
                    .stream().map(fieldError -> fieldError.getDefaultMessage()).toList());
        }
        return this.viaggiService.findByIdAndUpdate(viaggioId, payload);
    }

    @PatchMapping("/{viaggioId}/stato")
    public Viaggio updateStato(@PathVariable UUID viaggioId, @RequestBody @Validated UpdateStatoViaggioDTO payload, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            throw new ValidationException(validationResult.getFieldErrors()
                    .stream().map(fieldError -> fieldError.getDefaultMessage()).toList());
        }
        return this.viaggiService.updateStato(viaggioId, payload);
    }

    @DeleteMapping("/{viaggioId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void findByIdAndDelete(@PathVariable UUID viaggioId) {
        this.viaggiService.findByIdAndDelete(viaggioId);
    }
}