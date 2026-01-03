package com.sietediez.sietediez.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MovimientoDTO {
    
    @NotNull
    private Double importe;

}

