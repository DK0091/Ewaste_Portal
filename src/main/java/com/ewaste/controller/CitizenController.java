package com.ewaste.controller;

import com.ewaste.entity.Pickup;
import com.ewaste.entity.User;
import com.ewaste.service.PdfCertificateService;
import com.ewaste.service.PickupService;
import com.ewaste.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpSession;

import java.time.LocalDate;

@Controller
@RequestMapping("/citizen")
public class CitizenController {

    private final PickupService pickupService;
    private final UserService userService;
    private final PdfCertificateService certificateService;

    public CitizenController(PickupService pickupService, UserService userService, PdfCertificateService certificateService) {
        this.pickupService = pickupService;
        this.userService = userService;
        this.certificateService = certificateService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication authentication, HttpSession session) {
        User citizen = userService.findByUsername(authentication.getName());
        model.addAttribute("pickups", pickupService.getCitizenPickups(citizen));
        
        String sessionMsg = (String) session.getAttribute("flashMsg");
        if(sessionMsg != null) {
            model.addAttribute("msg", sessionMsg);
            session.removeAttribute("flashMsg");
        }
        return "citizen-dashboard";
    }

    @GetMapping("/schedule")
    public String scheduleForm() { return "schedule-pickup"; }

    @PostMapping("/schedule")
    public String schedulePickup(@RequestParam String address,
                                 @RequestParam String date,
                                 @RequestParam String itemType,
                                 @RequestParam Double weight,
                                 Authentication authentication,
                                 HttpSession session) {
        User citizen = userService.findByUsername(authentication.getName());
        pickupService.schedulePickup(citizen, address, LocalDate.parse(date), itemType, weight);
        
        session.setAttribute("flashMsg", "Pickup scheduled successfully!");
        return "redirect:/citizen/dashboard";
    }

    @GetMapping("/certificate/download")
    public ResponseEntity<byte[]> downloadCertificate(@RequestParam Long pickupId, Authentication authentication) {
        Pickup pickup = pickupService.getPickupById(pickupId);
        
        if (pickup == null || !pickup.getCitizen().getUsername().equals(authentication.getName()) || !pickup.getStatus().equals("CERTIFIED")) {
            return ResponseEntity.badRequest().build();
        }

        byte[] pdfBytes = certificateService.generatePdf(pickup);
        
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"ewaste_cert_" + pickupId + ".pdf\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }
}
