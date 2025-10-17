package com.emiliano.business_travel_management.payload;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record NewPrenotazioneDTO(
        @NotNull(message = "l'id del viaggio è obbligatorio")
        UUID viaggioId,

        @NotNull(message = "l'id del dipendente è obbligatorio")
        UUID dipendenteId,

        @Size(max = 500, message = "le note non possono superare i 500 caratteri")
        String note
) {
}