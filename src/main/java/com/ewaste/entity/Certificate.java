package com.ewaste.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "certificates")
public class Certificate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "pickup_id", nullable = false)
    private Pickup pickup;

    @Column(nullable = false)
    private LocalDate issueDate;

    @Column(columnDefinition = "TEXT")
    private String content;

    public Certificate() {}

    public Certificate(Pickup pickup, LocalDate issueDate, String content) {
        this.pickup = pickup;
        this.issueDate = issueDate;
        this.content = content;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Pickup getPickup() { return pickup; }
    public void setPickup(Pickup pickup) { this.pickup = pickup; }

    public LocalDate getIssueDate() { return issueDate; }
    public void setIssueDate(LocalDate issueDate) { this.issueDate = issueDate; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}
