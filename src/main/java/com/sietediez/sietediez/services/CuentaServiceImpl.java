package com.sietediez.sietediez.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sietediez.sietediez.domain.Cuenta;
import com.sietediez.sietediez.dto.CuentaDTO;
import com.sietediez.sietediez.repositories.CuentaRepository;

@Service
public class CuentaServiceImpl implements CuentaService{
    
    @Autowired 
    private CuentaRepository repositorio;

    public List<Cuenta> obtenerTodos(){
        return repositorio.findAll();
    }

    @Override
    public Cuenta crearCuenta(CuentaDTO dto) {
        if (repositorio.existsById(dto.getIBAN())) {
            throw new RuntimeException("Ya existe una cuenta con IBAN " + dto.getIBAN());
        }

        Cuenta cuenta = new Cuenta();
        cuenta.setIBAN(dto.getIBAN());
        cuenta.setNombre(dto.getNombre());
        cuenta.setSaldo(0.0);

        return repositorio.save(cuenta);
    }

    @Override
    public List<Cuenta> listarCuentas() {
        return repositorio.findAll();
    }

    @Override
    public Cuenta obtenerCuenta(String iban) {
        return repositorio.findById(iban)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));
    }

    @Override
    public Cuenta actualizarCuenta(String iban, CuentaDTO dto) {
        Cuenta cuenta = obtenerCuenta(iban);
        cuenta.setNombre(dto.getNombre());
        return repositorio.save(cuenta);
    }

    @Override
    public void eliminarCuenta(String iban) {
        if (!repositorio.existsById(iban)) {
            throw new RuntimeException("Cuenta no encontrada");
        }
        repositorio.deleteById(iban);
    }
}
