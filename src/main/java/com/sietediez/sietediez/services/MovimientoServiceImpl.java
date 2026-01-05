package com.sietediez.sietediez.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; 

import com.sietediez.sietediez.domain.Cuenta;
import com.sietediez.sietediez.domain.Movimiento;
import com.sietediez.sietediez.dto.MovimientoDTO;
import com.sietediez.sietediez.repositories.CuentaRepository;
import com.sietediez.sietediez.repositories.MovimientoRepository;

@Service
@Transactional(readOnly = true)
public class MovimientoServiceImpl  implements MovimientoService{

    @Autowired 
    private MovimientoRepository repositorioMovimiento;
    @Autowired
    private CuentaRepository repositorioCuenta;

    @Override
    public List<Movimiento> obtenerTodos(){
        return repositorioMovimiento.findAll();
    }

    @Override
    @Transactional
    public Movimiento crearMovimiento(String iban, MovimientoDTO dto) {
        Cuenta cuenta = repositorioCuenta.findById(iban)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));

        Movimiento movimiento = new Movimiento();
        movimiento.setCuenta(cuenta);
        
        // Determinar si es depósito o retiro
        Double importe = dto.getImporte();
        if ("RETIRO".equals(dto.getOperacion())) {
            importe = -importe;
        }
        
        movimiento.setImporte(importe);
        movimiento.setFecha(LocalDateTime.now());

        // Actualizar saldo
        cuenta.setSaldo(cuenta.getSaldo() + importe);

        System.out.println("[MovimientoService] creando movimiento: importe=" + movimiento.getImporte() + " cuenta=" + cuenta.getIBAN());
        repositorioMovimiento.save(movimiento);
        System.out.println("[MovimientoService] movimientos totales tras save: " + repositorioMovimiento.findAll().size());
        repositorioCuenta.save(cuenta);

        return movimiento;
    }

    @Override
    public List<Movimiento> listarMovimientos(String iban) {
        if (!repositorioCuenta.existsById(iban)) {
            throw new RuntimeException("Cuenta no encontrada");
        }
        return repositorioMovimiento.findByCuentaIBAN(iban);
    }
}
