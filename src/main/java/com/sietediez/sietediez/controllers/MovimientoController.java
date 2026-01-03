package com.sietediez.sietediez.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sietediez.sietediez.dto.MovimientoDTO;
import com.sietediez.sietediez.services.CuentaService;
import com.sietediez.sietediez.services.MovimientoService;

import jakarta.validation.Valid;

@Controller
public class MovimientoController {
    
    @Autowired
    private MovimientoService movimientoService;
    private CuentaService cuentaService;
    
    private String txtMsg;


    @GetMapping("/{iban}")
    public String listarMovimientos(@PathVariable String iban, Model model) {
        try {
            model.addAttribute("cuenta", cuentaService.obtenerCuenta(iban));
            model.addAttribute("listaMovimientos",
                    movimientoService.listarMovimientos(iban));
            return "listMovimientosView";
        } catch (RuntimeException e) {
            model.addAttribute("msg", e.getMessage());
            return "redirect:/cuentas";
        }
    }

    /* =========================
       FORMULARIO NUEVO MOVIMIENTO
       ========================= */
    @GetMapping("/{iban}/nuevo")
    public String showNewMovimiento(@PathVariable String iban, Model model) {
        try {
            model.addAttribute("cuenta", cuentaService.obtenerCuenta(iban));
            model.addAttribute("movimientoDTO", new MovimientoDTO());
            return "newMovimientoView";
        } catch (RuntimeException e) {
            model.addAttribute("msg", e.getMessage());
            return "redirect:/cuentas";
        }
    }

    /* =========================
       ALTA MOVIMIENTO
       ========================= */
    @PostMapping("/{iban}/nuevo/submit")
    public String showNewMovimientoSubmit(
            @PathVariable String iban,
            @Valid MovimientoDTO movimientoDTO,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "newMovimientoView";
        }

        try {
            movimientoService.crearMovimiento(iban, movimientoDTO);
            redirectAttributes.addFlashAttribute(
                    "msg", "Movimiento añadido correctamente");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("msg", e.getMessage());
        }

        return "redirect:/movimientos/" + iban;
    }
}
