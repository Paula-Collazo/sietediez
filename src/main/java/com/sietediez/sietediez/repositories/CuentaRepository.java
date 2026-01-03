package com.sietediez.sietediez.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sietediez.sietediez.domain.Cuenta;

public interface CuentaRepository extends JpaRepository<Cuenta, String> {
    List<Cuenta> findByIBAN(String iBAN);

    
}