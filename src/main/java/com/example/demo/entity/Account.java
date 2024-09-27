package com.example.demo.entity;



import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Account implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @NotBlank(message = "Code can not be blank!")
    @Pattern(regexp = "[A-Z]{3}\\d{6}", message = "Invalid code!")
    @Column(unique = true)
    String code;

    @Enumerated(EnumType.STRING) // Sử dụng EnumType.STRING
    @Column(nullable = false) // Đảm bảo không null
    private Role role; //Role để front-end nhập

    @Email(message = "Invalid Email!")
    @Column(unique = true)
    private String email;

    @Pattern(regexp = "(84|0[3|5|7|8|9])+(\\d{8})", message = "Invalid phone!")
    @Column(unique = true)
    private String phone;

    @NotBlank(message = "Name cannot be blank")
    @Pattern(regexp = "^[^\\d]*$", message = "Name cannot contain numbers!")
    @Pattern(regexp = "^[^\\s].*", message = "First character cannot have space!")
    @Column(name="user_name",unique = true)
    private String userName;

    @Size(min = 6, message = "Password must be at least 6 character!")
    @Column(unique = true)
    private String password;

    @Column(name = "created_at", nullable = false) // Thêm tên cột và yêu cầu không null
    private LocalDateTime createdAt;

    public Account() {
        this.createdAt = LocalDateTime.now(); // Gán thời gian hiện tại khi tạo mới
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getUsername() {
        return this.userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @OneToMany(mappedBy = "account")
    List<Booking> bookings;

    @OneToMany(mappedBy = "account")
    List<OrderCart> orderCarts;

    @OneToMany(mappedBy = "account")
    List<Feedback> feedbacks;

    @OneToMany(mappedBy = "account")
    List<QuotationProcess> quotationProcesses;
}

