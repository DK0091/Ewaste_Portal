package com.ewaste.controller;

import com.ewaste.entity.Pickup;
import com.ewaste.entity.User;
import com.ewaste.service.PickupService;
import com.ewaste.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final PickupService pickupService;

    public AdminController(UserService userService, PickupService pickupService) {
        this.userService = userService;
        this.pickupService = pickupService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        List<User> users = userService.findAllUsers();
        List<Pickup> pickups = pickupService.getAllPickups();
        
        Double totalOverallWeight = 0.0;
        for (Pickup p : pickups) {
            if ("CERTIFIED".equals(p.getStatus()) && !p.getItems().isEmpty() && p.getItems().get(0).getWeight() != null) {
                totalOverallWeight += p.getItems().get(0).getWeight();
            }
        }

        long requested = pickups.stream().filter(p -> "REQUESTED".equals(p.getStatus())).count();
        long processing = pickups.stream().filter(p -> p.getStatus().equals("ASSIGNED") || p.getStatus().equals("COLLECTED") || p.getStatus().equals("SORTING") || p.getStatus().equals("SHREDDING")).count();
        long certified = pickups.stream().filter(p -> "CERTIFIED".equals(p.getStatus())).count();

        model.addAttribute("users", users);
        model.addAttribute("pickups", pickups);
        model.addAttribute("totalOverallWeight", totalOverallWeight);
        model.addAttribute("countRequested", requested);
        model.addAttribute("countProcessing", processing);
        model.addAttribute("countCertified", certified);
        
        return "admin-dashboard";
    }
}
