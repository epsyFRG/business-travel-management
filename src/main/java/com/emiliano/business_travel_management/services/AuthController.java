package com.emiliano.business_travel_management.controllers;

import com.emiliano.business_travel_management.entities.Dipendente;
import com.emiliano.business_travel_management.exceptions.ValidationException;
import com.emiliano.business_travel_management.payload.LoginDTO;
import com.emiliano.business_travel_management.payload.LoginResponseDTO;
import com.emiliano.business_travel_management.payload.NewDipendenteDTO;
import com.emiliano.business_travel_management.services.AuthService;
import com.emiliano.business_travel_management.services.DipendentiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private DipendentiService dipendentiService;

    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody @Validated LoginDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            throw new ValidationException(validationResult.getFieldErrors()
                    .stream().map(fieldError -> fieldError.getDefaultMessage()).toList());
        }
        return new LoginResponseDTO(authService.checkCredentialsAndGenerateToken(body));
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public Dipendente register(@RequestBody @Validated NewDipendenteDTO payload, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            throw new ValidationException(validationResult.getFieldErrors()
                    .stream().map(fieldError -> fieldError.getDefaultMessage()).toList());
        }
        return this.dipendentiService.save(payload);
    }
}