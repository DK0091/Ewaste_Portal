package com.ewaste.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pickups")
public class Pickup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "citizen_id", nullable = false)
    private User citizen;

    @ManyToOne
    @JoinColumn(name = "recycler_id")
    private User recycler;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private LocalDate pickupDate;

    // Status: REQUESTED, ASSIGNED, COLLECTED, SORTING, SHREDDING, CERTIFIED
    @Column(nullable = false)
    private String status;

    @OneToMany(mappedBy = "pickup", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EwasteItem> items = new ArrayList<>();

    public Pickup() {
        this.status = "REQUESTED";
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public User getCitizen() { return citizen; }
    public void setCitizen(User citizen) { this.citizen = citizen; }
    
    public User getRecycler() { return recycler; }
    public void setRecycler(User recycler) { this.recycler = recycler; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    public LocalDate getPickupDate() { return pickupDate; }
    public void setPickupDate(LocalDate pickupDate) { this.pickupDate = pickupDate; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public List<EwasteItem> getItems() { return items; }
    public void setItems(List<EwasteItem> items) { this.items = items; }
}
