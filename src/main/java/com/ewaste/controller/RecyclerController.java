package com.ewaste.controller;

import com.ewaste.entity.Pickup;
import com.ewaste.entity.User;
import com.ewaste.service.NotificationService;
import com.ewaste.service.PickupService;
import com.ewaste.service.ProcessingService;
import com.ewaste.service.UserService;
import com.ewaste.util.PickupStatus;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/recycler")
public class RecyclerController {

    // Ordered stage progression — no skipping or going backward
    private static final List<String> STAGE_ORDER = List.of(
        PickupStatus.ASSIGNED,
        PickupStatus.COLLECTED,
        PickupStatus.SORTING,
        PickupStatus.SHREDDING,
        PickupStatus.COMPLETED,
        PickupStatus.CERTIFICATE_ISSUED
    );

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
        List<Pickup> myPickups = pickupService.getRecyclerPickups(recycler);

        // Stats panel
        long totalHandled   = myPickups.size();
        long totalCompleted = myPickups.stream().filter(p -> PickupStatus.CERTIFICATE_ISSUED.equals(p.getStatus())).count();
        double totalWeight  = Math.round(pickupService.getRecyclerTotalWeight(recycler) * 100.0) / 100.0;

        model.addAttribute("myPickups",        myPickups);
        model.addAttribute("requestedPickups",  pickupService.getRequestedPickups());
        model.addAttribute("totalHandled",      totalHandled);
        model.addAttribute("totalCompleted",    totalCompleted);
        model.addAttribute("totalWeight",       totalWeight);
        model.addAttribute("stageOrder",        STAGE_ORDER);

        String sessionMsg = (String) session.getAttribute("flashMsg");
        if (sessionMsg != null) {
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

        session.setAttribute("flashMsg", "✅ Pickup #" + pickupId + " assigned. Citizen notified asynchronously.");
        return "redirect:/recycler/dashboard";
    }

    @PostMapping("/update-stage")
    public String updateStage(@RequestParam Long pickupId,
                              @RequestParam String stage,
                              @RequestParam(required = false) String notes,
                              Authentication authentication,
                              HttpSession session) {
        User recycler = userService.findByUsername(authentication.getName());
        Pickup pickup = pickupService.getPickupById(pickupId);

        // Enforce forward-only stage progression
        if (pickup != null && isValidNextStage(pickup.getStatus(), stage)) {
            processingService.updateProcessingStage(pickupId, recycler, stage, notes != null ? notes : "");
            session.setAttribute("flashMsg", "📦 Stage updated to: " + stage);
        } else {
            session.setAttribute("flashMsg", "⚠️ Invalid stage transition attempted.");
        }
        return "redirect:/recycler/dashboard";
    }

    @PostMapping("/issue-certificate")
    public String issueCertificate(@RequestParam Long pickupId, Authentication authentication, HttpSession session) {
        User recycler = userService.findByUsername(authentication.getName());
        Pickup pickup = pickupService.getPickupById(pickupId);

        if (pickup != null && pickup.getRecycler().getId().equals(recycler.getId())
                && PickupStatus.COMPLETED.equals(pickup.getStatus())) {
            processingService.updateProcessingStage(pickupId, recycler,
                PickupStatus.CERTIFICATE_ISSUED, "Certificate successfully issued by Recycler.");
            session.setAttribute("flashMsg", "🎉 Certificate issued for Pickup #" + pickupId + "!");
        } else {
            session.setAttribute("flashMsg", "⚠️ Certificate can only be issued after status is Completed.");
        }
        return "redirect:/recycler/dashboard";
    }

    /** Returns true only if `next` is exactly one step ahead of `current` in STAGE_ORDER */
    private boolean isValidNextStage(String current, String next) {
        int currentIdx = STAGE_ORDER.indexOf(current);
        int nextIdx    = STAGE_ORDER.indexOf(next);
        return currentIdx >= 0 && nextIdx == currentIdx + 1;
    }
}
