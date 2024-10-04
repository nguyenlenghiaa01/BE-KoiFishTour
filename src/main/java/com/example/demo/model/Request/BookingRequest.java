package com.example.demo.model.Request;

import com.example.demo.entity.Account;
import com.example.demo.entity.OpenTour;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Random;

@Data
public class BookingRequest {

    @Temporal(TemporalType.DATE)
    @NotBlank(message = "Date can not be blank")
    @Pattern(regexp = "(([1-2][0-9])|([1-9])|(3[0-1]))/(0?[1-9]|1[0-2])/[0-9]{4}", message = "Enter the correct format!")
    private Date startDate;

    @Pattern(regexp = "^[^\\d]*$", message = "Status cannot have numbers!")
    @Pattern(regexp = "^[^\\s].*", message = "First character cannot have space!")
    private String status;

    @NotBlank(message = "Price cannot be blank")
    @Pattern(regexp = "^(VND)?\\s?\\d+(?:[.,]\\d{3})*(?:[.,]\\d{2})?$", message = "Enter the correct format!")
    private BigDecimal price;

    @Temporal(TemporalType.DATE)
    @NotBlank(message = "Date cannot be blank")
    @Pattern(regexp = "(([1-2][0-9])|([1-9])|(3[0-1]))/(0?[1-9]|1[0-2])/[0-9]{4}", message = "Enter the correct format!")
    private Date endDate;

    @Temporal(TemporalType.DATE)
    @NotBlank(message = "Date cannot be blank")
    @Pattern(regexp = "(([1-2][0-9])|([1-9])|(3[0-1]))/(0?[1-9]|1[0-2])/[0-9]{4}", message = "Enter the correct format!")
    private Date bookingDate;

    @ManyToOne
    @JoinColumn(name = "account_id")
    @JsonIgnore
    Account account;

    @ManyToOne
    @JoinColumn(name = "openTour_id")
    @JsonIgnore
    OpenTour openTour;
}
