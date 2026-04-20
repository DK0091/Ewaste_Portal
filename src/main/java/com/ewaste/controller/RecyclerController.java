package com.ewaste.controller;

import com.ewaste.entity.Pickup;
import com.ewaste.entity.User;
import com.ewaste.service.NotificationService;
import com.ewaste.service.PickupService;
import com.ewaste.service.ProcessingService;
import com.ewaste.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/recycler")
public class RecyclerController {

    private final PickupService pickupService;
    private final UserService userService;
    private final ProcessingService processingService;
    private final NotificationService notificationService;

    public RecyclerController(PickupService pickupService, UserService userService, 
                              ProcessingService processingService, NotificationService notificationService) {
        this.pickupService = pickupService;
        this.userService = userService;
        this.processingService = processingService;
        this.notificationService = notificationService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication authentication, HttpSession session) {
        User recycler = userService.findByUsername(authentication.getName());
        model.addAttribute("myPickups", pickupService.getRecyclerPickups(recycler));
        model.addAttribute("requestedPickups", pickupService.getRequestedPickups());
        
        String sessionMsg = (String) session.getAttribute("flashMsg");
        if(sessionMsg != null) {
            model.addAttribute("msg", sessionMsg);
            session.removeAttribute("flashMsg");
        }
        
        return "recycler-dashboard";
    }

    @PostMapping("/assign")
    public String assignPickup(@RequestParam Long pickupId, Authentication authentication, HttpSession session) {
        User recycler = userService.findByUsername(authentication.getName());
        pickupService.assignRecycler(pickupId, recycler);
        
        Pickup pickup = pickupService.getPickupById(pickupId);
        notificationService.sendPickupAssignedNotification(pickup.getCitizen(), pickup);
        
        session.setAttribute("flashMsg", "Pickup assigned successfully. Citizen has been notified asynchronously.");
        return "redirect:/recycler/dashboard";
    }

    @PostMapping("/update-stage")
    public String updateStage(@RequestParam Long pickupId, 
                              @RequestParam String stage, 
                              @RequestParam(required=false) String notes, 
                              Authentication authentication,
                              HttpSession session) {
        User recycler = userService.findByUsername(authentication.getName());
        processingService.updateProcessingStage(pickupId, recycler, stage, notes != null ? notes : "");
        session.setAttribute("flashMsg", "Processing stage updated to: " + stage);
        return "redirect:/recycler/dashboard";
    }

    @PostMapping("/issue-certificate")
    public String issueCertificate(@RequestParam Long pickupId, Authentication authentication, HttpSession session) {
        User recycler = userService.findByUsername(authentication.getName());
        Pickup pickup = pickupService.getPickupById(pickupId);
        
        if (pickup != null && pickup.getRecycler().getId().equals(recycler.getId())) {
            processingService.updateProcessingStage(pickupId, recycler, "CERTIFIED", "Certificate successfully issued by Recycler");
            session.setAttribute("flashMsg", "Certificate generated!");
        }
        return "redirect:/recycler/dashboard";
    }
}
