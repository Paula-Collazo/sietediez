package com.sietediez.sietediez.dto;

import com.sietediez.sietediez.domain.Cuenta;

import lombok.Data;

@Data
public class CuentaDTO {

    private String IBAN;
    private String nombre;


    public Cuenta crearCuenta() {
        Cuenta cuenta = new Cuenta();
        cuenta.setIBAN(this.IBAN);
        cuenta.setNombre(this.nombre);
        cuenta.setSaldo(0.0);
        return cuenta;
    }
}
