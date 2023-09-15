package com.example.projectfilrouge.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data


public class Ticket {
    @Id

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String eventName;

    private Date eventDate;
    private Double price;
    private String details;
    private String state;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;



}
