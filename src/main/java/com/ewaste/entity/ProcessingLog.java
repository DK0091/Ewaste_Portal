package com.ewaste.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "processing_log")
public class ProcessingLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pickup_id", nullable = false)
    private Pickup pickup;

    @ManyToOne
    @JoinColumn(name = "recycler_id", nullable = false)
    private User recycler;

    @Column(nullable = false)
    private String stage;

    @Column(nullable = false)
    private LocalDateTime logDate;
    
    @Column(columnDefinition = "TEXT")
    private String notes;

    public ProcessingLog() {}

    public ProcessingLog(Pickup pickup, User recycler, String stage, LocalDateTime logDate, String notes) {
        this.pickup = pickup;
        this.recycler = recycler;
        this.stage = stage;
        this.logDate = logDate;
        this.notes = notes;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Pickup getPickup() { return pickup; }
    public void setPickup(Pickup pickup) { this.pickup = pickup; }

    public User getRecycler() { return recycler; }
    public void setRecycler(User recycler) { this.recycler = recycler; }

    public String getStage() { return stage; }
    public void setStage(String stage) { this.stage = stage; }

    public LocalDateTime getLogDate() { return logDate; }
    public void setLogDate(LocalDateTime logDate) { this.logDate = logDate; }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
