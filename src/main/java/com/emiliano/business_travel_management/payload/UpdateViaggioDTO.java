package com.emiliano.business_travel_management.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record UpdateViaggioDTO(
        @NotBlank(message = "la destinazione è obbligatoria")
        @Size(min = 2, max = 50, message = "la destinazione deve avere una lunghezza compresa tra 2 e 50 caratteri")
        String destinazione,

        @NotNull(message = "la data è obbligatoria")
        LocalDate data
) {
}
