package com.sietediez.sietediez.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sietediez.sietediez.dto.MovimientoDTO;
import com.sietediez.sietediez.services.CuentaService;
import com.sietediez.sietediez.services.MovimientoService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/movimientos")
public class MovimientoController {
    
    @Autowired
    private MovimientoService movimientoService;
    @Autowired
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
            MovimientoDTO dto = new MovimientoDTO();
            dto.setIban(iban);
            model.addAttribute("movimientoDTO", dto);
            return "newMovimientoView";
        } catch (RuntimeException e) {
            model.addAttribute("msg", e.getMessage());
            return "redirect:/cuentas";
        }
    }

    @GetMapping("/nuevo")
    public String showGlobalNewMovimiento(Model model) {
        model.addAttribute("movimientoDTO", new MovimientoDTO());
        model.addAttribute("cuentas", cuentaService.listarCuentas());
        return "newMovimientoGlobalView";
    }

    @PostMapping("/nuevo/submit")
    public String showGlobalNewMovimientoSubmit(
            @Valid MovimientoDTO movimientoDTO,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("cuentas", cuentaService.listarCuentas());
            model.addAttribute("movimientoDTO", movimientoDTO);
            return "newMovimientoGlobalView";
        }

        // Validar límites
        if ("DEPOSITO".equals(movimientoDTO.getOperacion()) && movimientoDTO.getImporte() > 1000) {
            model.addAttribute("cuentas", cuentaService.listarCuentas());
            model.addAttribute("movimientoDTO", movimientoDTO);
            model.addAttribute("error", "El importe máximo para depósitos es 1000");
            return "newMovimientoGlobalView";
        }
        if ("RETIRO".equals(movimientoDTO.getOperacion()) && movimientoDTO.getImporte() > 300) {
            model.addAttribute("cuentas", cuentaService.listarCuentas());
            model.addAttribute("movimientoDTO", movimientoDTO);
            model.addAttribute("error", "El importe máximo para retiros es 300");
            return "newMovimientoGlobalView";
        }

        try {
            movimientoService.crearMovimiento(movimientoDTO.getIban(), movimientoDTO);
            redirectAttributes.addFlashAttribute(
                    "msg", "Movimiento añadido correctamente");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("msg", e.getMessage());
        }

        return "redirect:/movimientos/" + movimientoDTO.getIban();
    }

    /* =========================
       ALTA MOVIMIENTO
       ========================= */
    @PostMapping("/{iban}/nuevo/submit")
    public String showNewMovimientoSubmit(
            @PathVariable String iban,
            @Valid MovimientoDTO movimientoDTO,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("cuenta", cuentaService.obtenerCuenta(iban));
            model.addAttribute("movimientoDTO", movimientoDTO);
            return "newMovimientoView";
        }

        // Validar límites
        if ("DEPOSITO".equals(movimientoDTO.getOperacion()) && movimientoDTO.getImporte() > 1000) {
            model.addAttribute("cuenta", cuentaService.obtenerCuenta(iban));
            model.addAttribute("movimientoDTO", movimientoDTO);
            model.addAttribute("error", "El importe máximo para depósitos es 1000");
            return "newMovimientoView";
        }
        if ("RETIRO".equals(movimientoDTO.getOperacion()) && movimientoDTO.getImporte() > 300) {
            model.addAttribute("cuenta", cuentaService.obtenerCuenta(iban));
            model.addAttribute("movimientoDTO", movimientoDTO);
            model.addAttribute("error", "El importe máximo para retiros es 300");
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
