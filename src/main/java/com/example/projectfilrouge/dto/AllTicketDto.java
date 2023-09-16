package com.example.projectfilrouge.dto;

import java.util.List;

public record AllTicketDto(List<TicketDto> ticketOnSale, List<TicketDto> ticketPurchased) {
}
