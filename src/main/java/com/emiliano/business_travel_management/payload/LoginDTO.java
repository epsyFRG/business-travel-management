package com.emiliano.business_travel_management.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginDTO(
        @NotBlank(message = "l'email è obbligatoria")
        @Email(message = "l'indirizzo email inserito non è nel formato corretto")
        String email,

        @NotBlank(message = "la password è obbligatoria")
        String password
) {
}