package com.sietediez.sietediez;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.sietediez.sietediez.domain.Cuenta;
import com.sietediez.sietediez.dto.MovimientoDTO;
import com.sietediez.sietediez.repositories.CuentaRepository;
import com.sietediez.sietediez.services.MovimientoService;

@Component
public class TestDataLoader implements CommandLineRunner {

    @Autowired
    private CuentaRepository cuentaRepository;

    @Autowired
    private MovimientoService movimientoService;

    @Override
    public void run(String... args) throws Exception {
        String iban = "ESLOAD";
        if (!cuentaRepository.existsById(iban)) {
            Cuenta cuenta = new Cuenta();
            cuenta.setIBAN(iban);
            cuenta.setNombre("Cuenta Carga");
            cuenta.setSaldo(0.0);
            cuentaRepository.save(cuenta);
            System.out.println("[TestDataLoader] Created account " + iban);

            MovimientoDTO m = new MovimientoDTO();
            m.setImporte(123.45);
            movimientoService.crearMovimiento(iban, m);
            System.out.println("[TestDataLoader] Created movement for " + iban);
        } else {
            System.out.println("[TestDataLoader] Account already present: " + iban);
        }
    }
}