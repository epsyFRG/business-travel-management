package com.emiliano.business_travel_management.payload;

import com.emiliano.business_travel_management.entities.StatoViaggio;
import jakarta.validation.constraints.NotNull;

public record UpdateStatoViaggioDTO(
        @NotNull(message = "lo stato è obbligatorio")
        StatoViaggio stato
) {
}