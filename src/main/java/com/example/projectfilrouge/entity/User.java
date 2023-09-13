package com.example.projectfilrouge.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String email;
    private String photo;


    @OneToMany(mappedBy = "user")
    private List<Transaction> transactions;

    @OneToMany(mappedBy = "owner")
    private List<Ticket> tickets;

    public User(Long id, String username, String password) {
    }

    public User(Long id, String username, String password, String email, String photo, List<Transaction> transactions, List<Ticket> tickets) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.photo = photo;
        this.transactions = transactions;
        this.tickets = tickets;
    }

    public User(String username, String password, String email, String photo, List<Transaction> transactions, List<Ticket> tickets) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.photo = photo;
        this.transactions = transactions;
        this.tickets = tickets;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }
}

