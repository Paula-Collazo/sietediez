package com.sietediez.sietediez.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MovimientoDTO {
    
    @NotNull
    private Double importe;

    @NotEmpty
    private String iban;
    
    private String operacion; // "DEPOSITO" o "RETIRO"

}

