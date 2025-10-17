package com.emiliano.business_travel_management.payload;

import jakarta.validation.constraints.Size;

public record UpdatePrenotazioneDTO(
        @Size(max = 500, message = "le note non possono superare i 500 caratteri")
        String note
) {
}