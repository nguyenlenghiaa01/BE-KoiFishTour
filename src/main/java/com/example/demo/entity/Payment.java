package com.example.demo.entity;

import com.example.demo.Enum.PaymentEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.query.Order;

import java.util.Date;
import java.util.Set;

@Entity
@Data
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    Date createAt;

    @Enumerated(EnumType.STRING)
    PaymentEnum payment_method;


    @OneToMany(mappedBy = "payment")
    @JsonIgnore
    Set<Transactions> transactions;

    @OneToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;



}
