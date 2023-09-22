package com.example.projectfilrouge.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
public class TicketFilterDto {
    private String eventName;
    private Date eventStartDate;
    private Date eventEndDate;
    private Double priceMin;
    private Double priceMax;
    private String details;
    private String state;
    private List<String> tags;
}
