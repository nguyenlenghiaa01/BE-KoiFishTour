package com.example.demo.entity;

import com.example.demo.Enum.OrderEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Getter
@Setter
@Entity
public class KoiFishOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Code can not be blank!")
    @Pattern(regexp = "ORD\\d{7}", message = "Invalid code!")
    @Column(unique = true)
    private String koiFishOrderId;

    @Column(nullable = false)
    private boolean isDeleted = false;

    private String notes;
    @Enumerated(EnumType.STRING)
    private OrderEnum status;
    Date createAt;

    @Min(value = 0, message = "Total price must be positive!")
    private double paidMoney;

    private double totalPrice;

    public KoiFishOrder() {
        this.koiFishOrderId = generateOrderId();
    }

    private String generateOrderId() {
        Random random = new Random();
        int number = random.nextInt(10000000);
        return String.format("ORD%07d", number);
    }


    @ManyToOne
    @JoinColumn(name = "consulting_id")
    @JsonIgnore
    private Account consulting;

    @ManyToOne
    @JoinColumn(name="customer_id")
    private Account customer;

    @OneToMany(mappedBy = "koiFishOrder",cascade = CascadeType.ALL)
    private List<ShoppingCart> shoppingCarts;

    @OneToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;

}
