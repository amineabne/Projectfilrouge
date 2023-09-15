package com.example.projectfilrouge.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
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
    private UserEntity owner;


}
