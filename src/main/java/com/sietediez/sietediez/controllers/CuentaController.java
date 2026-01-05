package com.sietediez.sietediez.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.sietediez.sietediez.domain.Cuenta;
import com.sietediez.sietediez.dto.CuentaDTO;
import com.sietediez.sietediez.services.CuentaService;

import jakarta.validation.Valid;

@Controller
public class CuentaController {
 
    @Autowired
    public CuentaService cuentaService;

    private String txtMsg;

    @GetMapping({ "/", "/list" })
    public String showList(Model model) {
        model.addAttribute("tituloListado", "Listado de cuentas");
        model.addAttribute("listaCuentas", cuentaService.listarCuentas());
        if (txtMsg != null) {
            model.addAttribute("msg", txtMsg);
            txtMsg = null;
        }
        return "CuentasView";
    }

    @GetMapping("/{id}")
    public String showElement(@PathVariable String id, Model model) {
        try {
            model.addAttribute("cuenta", cuentaService.obtenerCuenta(id));
            return "listOneView";
        } catch (RuntimeException e) {
            txtMsg = e.getMessage();
            return "redirect:/";
        }
    }

    @GetMapping("/nuevo")
    public String showNew(Model model) {
        model.addAttribute("cuentaDTO", new CuentaDTO());
        return "newCuentaView";
    }

    @PostMapping("/nuevo/submit")
    public String showNewSubmit(@Valid CuentaDTO CuentaDTO,
            BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("cuentaDTO", CuentaDTO);
            return "newCuentaView";
        }
        try {
            cuentaService.crearCuenta(CuentaDTO);
            txtMsg = "Operación realizada con éxito";
        } catch (RuntimeException e) {
            txtMsg = e.getMessage();
        }
        return "redirect:/";
    }

    @GetMapping("/editar/{id}")
    public String showEditForm(@PathVariable String id, Model model) {
        try {
            Cuenta cuenta = cuentaService.obtenerCuenta(id);
            model.addAttribute("cuentaDTO", cuenta);
            return "editCuentaView";
        } catch (RuntimeException e) {
            txtMsg = e.getMessage();
            return "redirect:/";
        }
    }

    @PostMapping("/editar/{id}/submit")
    public String showEditSubmit(@PathVariable String id, @Valid CuentaDTO CuentaDTO,
            BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("cuentaDTO", CuentaDTO);
            return "editCuentaView";
        }
        try {
            cuentaService.actualizarCuenta(id, CuentaDTO);
            txtMsg = "Operación realizada con éxito";
        } catch (RuntimeException e) {
            txtMsg = e.getMessage();
        }
        return "redirect:/";
    }

    @GetMapping("/borrar/{id}")
    public String showDelete(@PathVariable String id) {
        cuentaService.eliminarCuenta(id);
        txtMsg = "Operación realizada con éxito";
        return "redirect:/";
    }
}
