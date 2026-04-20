package com.ewaste.controller;

import com.ewaste.entity.Pickup;
import com.ewaste.entity.User;
import com.ewaste.service.PickupService;
import com.ewaste.service.UserService;
import com.ewaste.util.PickupStatus;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

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
    public String dashboard(
            Model model,
            @RequestParam(required = false) String statusFilter,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {

        List<User> users = userService.findAllUsers();
        List<Pickup> allPickups = pickupService.getAllPickups();

        // ── Apply filters ─────────────────────────────────────────────────────────
        List<Pickup> filtered = allPickups.stream()
            .filter(p -> statusFilter == null || statusFilter.isEmpty() || statusFilter.equals(p.getStatus()))
            .filter(p -> from == null || !p.getPickupDate().isBefore(from))
            .filter(p -> to   == null || !p.getPickupDate().isAfter(to))
            .collect(Collectors.toList());

        // ── KPI Counts ────────────────────────────────────────────────────────────
        long countScheduled   = allPickups.stream().filter(p -> PickupStatus.SCHEDULED.equals(p.getStatus()) && p.getRecycler() == null).count();
        long countActive      = allPickups.stream().filter(p -> PickupStatus.isProcessing(p.getStatus())).count();
        long countCompleted   = allPickups.stream().filter(p -> PickupStatus.COMPLETED.equals(p.getStatus())).count();
        long countCertified   = allPickups.stream().filter(p -> PickupStatus.CERTIFICATE_ISSUED.equals(p.getStatus())).count();
        long countProcessing  = allPickups.stream().filter(p -> PickupStatus.isProcessing(p.getStatus())).count();
        long countRequested   = countScheduled;

        // Total e-waste weight (all certificate-issued pickups)
        double totalWeight = allPickups.stream()
            .filter(p -> PickupStatus.CERTIFICATE_ISSUED.equals(p.getStatus()))
            .flatMap(p -> p.getItems().stream())
            .mapToDouble(i -> i.getWeight() != null ? i.getWeight() : 0)
            .sum();

        // ── Per-Stage Breakdown for Bar Chart ────────────────────────────────────
        Map<String, Long> stageBreakdown = new LinkedHashMap<>();
        stageBreakdown.put(PickupStatus.SCHEDULED,          allPickups.stream().filter(p -> PickupStatus.SCHEDULED.equals(p.getStatus())).count());
        stageBreakdown.put(PickupStatus.ASSIGNED,           allPickups.stream().filter(p -> PickupStatus.ASSIGNED.equals(p.getStatus())).count());
        stageBreakdown.put(PickupStatus.COLLECTED,          allPickups.stream().filter(p -> PickupStatus.COLLECTED.equals(p.getStatus())).count());
        stageBreakdown.put(PickupStatus.SORTING,            allPickups.stream().filter(p -> PickupStatus.SORTING.equals(p.getStatus())).count());
        stageBreakdown.put(PickupStatus.SHREDDING,          allPickups.stream().filter(p -> PickupStatus.SHREDDING.equals(p.getStatus())).count());
        stageBreakdown.put(PickupStatus.COMPLETED,          allPickups.stream().filter(p -> PickupStatus.COMPLETED.equals(p.getStatus())).count());
        stageBreakdown.put(PickupStatus.CERTIFICATE_ISSUED, allPickups.stream().filter(p -> PickupStatus.CERTIFICATE_ISSUED.equals(p.getStatus())).count());

        // ── Daily Weight for Line Chart (last 14 days) ────────────────────────────
        LocalDate today = LocalDate.now();
        List<String> lineLabels = new ArrayList<>();
        List<Double> lineData   = new ArrayList<>();
        for (int i = 13; i >= 0; i--) {
            LocalDate day = today.minusDays(i);
            lineLabels.add(day.toString());
            double dayWeight = allPickups.stream()
                .filter(p -> day.equals(p.getPickupDate()))
                .flatMap(p -> p.getItems().stream())
                .mapToDouble(item -> item.getWeight() != null ? item.getWeight() : 0)
                .sum();
            lineData.add(dayWeight);
        }

        // ── Recent 10 pickups for table ───────────────────────────────────────────
        List<Pickup> recentPickups = filtered.stream()
            .sorted(Comparator.comparing(Pickup::getPickupDate).reversed())
            .limit(10)
            .collect(Collectors.toList());

        // ── Model ─────────────────────────────────────────────────────────────────
        model.addAttribute("users",           users);
        model.addAttribute("pickups",         filtered);
        model.addAttribute("recentPickups",   recentPickups);
        model.addAttribute("totalPickups",    allPickups.size());
        model.addAttribute("countActive",     countActive);
        model.addAttribute("countCompleted",  countCompleted);
        model.addAttribute("countCertified",  countCertified);
        model.addAttribute("countRequested",  countRequested);
        model.addAttribute("countProcessing", countProcessing);
        model.addAttribute("totalOverallWeight", Math.round(totalWeight * 100.0) / 100.0);

        // Chart data
        model.addAttribute("stageLabels", new ArrayList<>(stageBreakdown.keySet()));
        model.addAttribute("stageCounts", new ArrayList<>(stageBreakdown.values()));
        model.addAttribute("lineLabels",  lineLabels);
        model.addAttribute("lineData",    lineData);

        // Filter state (for form persistence)
        model.addAttribute("statusFilter", statusFilter != null ? statusFilter : "");
        model.addAttribute("fromFilter",   from != null ? from.toString() : "");
        model.addAttribute("toFilter",     to   != null ? to.toString()   : "");

        // All statuses for filter dropdown
        model.addAttribute("allStatuses", List.of(
            PickupStatus.SCHEDULED, PickupStatus.ASSIGNED, PickupStatus.COLLECTED,
            PickupStatus.SORTING, PickupStatus.SHREDDING, PickupStatus.COMPLETED,
            PickupStatus.CERTIFICATE_ISSUED));

        return "admin-dashboard";
    }
}
