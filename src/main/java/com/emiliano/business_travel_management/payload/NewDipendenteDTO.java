package com.emiliano.business_travel_management.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record NewDipendenteDTO(
        @NotBlank(message = "lo username è obbligatorio")
        @Size(min = 3, max = 20, message = "lo username deve avere una lunghezza compresa tra 3 e 20 caratteri")
        String username,

        @NotBlank(message = "il nome è obbligatorio")
        @Size(min = 2, max = 30, message = "il nome deve avere una lunghezza compresa tra 2 e 30 caratteri")
        String nome,

        @NotBlank(message = "il cognome è obbligatorio")
        @Size(min = 2, max = 30, message = "il cognome deve avere una lunghezza compresa tra 2 e 30 caratteri")
        String cognome,

        @NotBlank(message = "l'email è obbligatoria")
        @Email(message = "l'indirizzo email inserito non è nel formato corretto")
        String email
) {
}