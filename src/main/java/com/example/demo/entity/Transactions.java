package com.example.demo.entity;

import com.example.demo.Enum.PaymentEnum;
import com.example.demo.Enum.TransactionsEnum;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.query.Order;

import java.util.Date;
import java.util.Set;

@Entity
@Data
public class Transactions {
    @Id //đánh dấu đây là khóa chính
    @GeneratedValue(strategy = GenerationType.IDENTITY) //tự generate ra cột này
    long id;

    String description;

    TransactionsEnum status;

    @ManyToOne
    @JoinColumn(name="from_id")
    Account from;

    @ManyToOne
    @JoinColumn(name="receiver")
    Account receiver;

    @ManyToOne
    @JoinColumn(name="payment_id")
    Payment payment;

}

