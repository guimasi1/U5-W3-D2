package com.example.U5W3D2.device;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record NewDeviceDTO(
        @NotEmpty(message = "Inserire il tipo di dispositivo")
        @Size(min = 3, max = 20, message = "Il nome del tipo di dispositivo deve essere di almeno 3 caratteri e massimo 20.")
        String type,
        @NotEmpty(message = "Inserire lo stato del dispositivo")
        @Size(min = 3, max = 20, message = "Lo stato del dispositivo deve essere di almeno 3 caratteri e massimo 20.")
        String status,
        UUID userUUID) {
}
