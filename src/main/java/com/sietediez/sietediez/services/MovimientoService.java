package com.sietediez.sietediez.services;

import java.util.List;

import com.sietediez.sietediez.domain.Movimiento;
import com.sietediez.sietediez.dto.MovimientoDTO;

public interface MovimientoService {
    Movimiento crearMovimiento(String iban, MovimientoDTO dto);
    List<Movimiento> listarMovimientos(String iban);
}
