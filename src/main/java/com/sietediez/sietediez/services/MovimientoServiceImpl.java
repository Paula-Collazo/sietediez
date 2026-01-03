package com.sietediez.sietediez.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.sietediez.sietediez.domain.Cuenta;
import com.sietediez.sietediez.domain.Movimiento;
import com.sietediez.sietediez.dto.MovimientoDTO;
import com.sietediez.sietediez.repositories.CuentaRepository;
import com.sietediez.sietediez.repositories.MovimientoRepository;

public class MovimientoServiceImpl  implements MovimientoService{

    @Autowired 
    private MovimientoRepository repositorioMovimiento;
    private CuentaRepository repositorioCuenta;

    public List<Movimiento> obtenerTodos(){
        return repositorioMovimiento.findAll();
    }

    @Override
    public Movimiento crearMovimiento(String iban, MovimientoDTO dto) {
        Cuenta cuenta = repositorioCuenta.findById(iban)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));

        Movimiento movimiento = new Movimiento();
        movimiento.setCuenta(cuenta);
        movimiento.setImporte(dto.getImporte());
        movimiento.setFecha(LocalDateTime.now());

        // Actualizar saldo
        cuenta.setSaldo(cuenta.getSaldo() + dto.getImporte());

        repositorioMovimiento.save(movimiento);
        repositorioCuenta.save(cuenta);

        return movimiento;
    }

    @Override
    public List<Movimiento> listarMovimientos(String iban) {
        if (!repositorioCuenta.existsById(iban)) {
            throw new RuntimeException("Cuenta no encontrada");
        }
        return repositorioMovimiento.findByCuentaIban(iban);
    }
}
