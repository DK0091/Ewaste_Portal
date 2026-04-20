package com.ewaste.controller;

import com.ewaste.entity.Pickup;
import com.ewaste.entity.User;
import com.ewaste.service.PdfCertificateService;
import com.ewaste.service.PickupService;
import com.ewaste.service.ProcessingService;
import com.ewaste.service.UserService;
import com.ewaste.util.PickupStatus;
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
import java.util.List;

@Controller
@RequestMapping("/citizen")
public class CitizenController {

    private final PickupService pickupService;
    private final UserService userService;
    private final PdfCertificateService certificateService;
    private final ProcessingService processingService;

    public CitizenController(PickupService pickupService, UserService userService,
                             PdfCertificateService certificateService, ProcessingService processingService) {
        this.pickupService = pickupService;
        this.userService = userService;
        this.certificateService = certificateService;
        this.processingService = processingService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication authentication, HttpSession session) {
        User citizen = userService.findByUsername(authentication.getName());
        List<Pickup> pickups = pickupService.getCitizenPickups(citizen);

        // "Your Impact" — total weight personally recycled by this citizen
        double myImpactKg = pickups.stream()
            .filter(p -> PickupStatus.CERTIFICATE_ISSUED.equals(p.getStatus()))
            .flatMap(p -> p.getItems().stream())
            .mapToDouble(i -> i.getWeight() != null ? i.getWeight() : 0)
            .sum();

        long certCount   = pickups.stream().filter(p -> PickupStatus.CERTIFICATE_ISSUED.equals(p.getStatus())).count();
        long activeCount = pickups.stream().filter(p -> PickupStatus.isProcessing(p.getStatus())).count();

        model.addAttribute("pickups",      pickups);
        model.addAttribute("myImpactKg",   Math.round(myImpactKg * 100.0) / 100.0);
        model.addAttribute("certCount",    certCount);
        model.addAttribute("activeCount",  activeCount);
        model.addAttribute("pickupCount",  pickups.size());

        String sessionMsg = (String) session.getAttribute("flashMsg");
        if (sessionMsg != null) {
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
        session.setAttribute("flashMsg", "✅ Pickup scheduled successfully! A recycler will accept it shortly.");
        return "redirect:/citizen/dashboard";
    }

    @GetMapping("/certificate/download")
    public ResponseEntity<byte[]> downloadCertificate(@RequestParam Long pickupId, Authentication authentication) {
        Pickup pickup = pickupService.getPickupById(pickupId);

        if (pickup == null
                || !pickup.getCitizen().getUsername().equals(authentication.getName())
                || !PickupStatus.CERTIFICATE_ISSUED.equals(pickup.getStatus())) {
            return ResponseEntity.badRequest().build();
        }

        byte[] pdfBytes = certificateService.generatePdf(pickup);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"ewaste_cert_" + pickupId + ".pdf\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }
}
