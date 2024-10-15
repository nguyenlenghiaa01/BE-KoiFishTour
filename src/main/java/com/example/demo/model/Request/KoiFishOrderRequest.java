package com.example.demo.model.Request;

import com.example.demo.entity.Account;
import com.example.demo.entity.KoiFish;
import com.example.demo.entity.ShoppingCart;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Data
public class KoiFishOrderRequest {
    long consulting_id;
    long koiFish_id;
}
