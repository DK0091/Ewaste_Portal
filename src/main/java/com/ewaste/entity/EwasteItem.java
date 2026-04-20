package com.ewaste.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "ewaste_items")
public class EwasteItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pickup_id", nullable = false)
    private Pickup pickup;

    @Column(nullable = false)
    private String itemType;

    @Column(nullable = false)
    private Double weight;

    public EwasteItem() {}

    public EwasteItem(Pickup pickup, String itemType, Double weight) {
        this.pickup = pickup;
        this.itemType = itemType;
        this.weight = weight;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Pickup getPickup() { return pickup; }
    public void setPickup(Pickup pickup) { this.pickup = pickup; }

    public String getItemType() { return itemType; }
    public void setItemType(String itemType) { this.itemType = itemType; }

    public Double getWeight() { return weight; }
    public void setWeight(Double weight) { this.weight = weight; }
}
