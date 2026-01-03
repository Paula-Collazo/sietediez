package com.sietediez.sietediez.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sietediez.sietediez.domain.Movimiento;

public interface MovimientoRepository extends JpaRepository<Movimiento, Long>{
    List<Movimiento> findByIdmov(Long idmov);
    
    List<Movimiento> findByCuentaIban(String iban);
}
