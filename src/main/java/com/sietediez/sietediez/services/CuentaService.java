package com.sietediez.sietediez.services;

import java.util.List;

import com.sietediez.sietediez.domain.Cuenta;
import com.sietediez.sietediez.dto.CuentaDTO;

public interface CuentaService {
    Cuenta crearCuenta(CuentaDTO dto);
    List<Cuenta> listarCuentas();
    Cuenta obtenerCuenta(String iban);
    Cuenta actualizarCuenta(String iban, CuentaDTO dto);
    void eliminarCuenta(String iban);
}
