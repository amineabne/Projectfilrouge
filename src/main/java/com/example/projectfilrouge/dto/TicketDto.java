package com.example.projectfilrouge.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
public class TicketDto {

    private String eventName;
    private Date eventDate;
    private Double price;
    private String details;
    private String state;

}
