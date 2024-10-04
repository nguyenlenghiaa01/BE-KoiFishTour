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
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Account implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Code can not be blank!")
    @Pattern(regexp = "[A-Z]{3}\\d{6}", message = "Invalid code!")
    @Column(unique = true)
    String code;

    @Enumerated(EnumType.STRING) // Sử dụng EnumType.STRING
    @Column(nullable = false) // Đảm bảo không null
    private Role role; //Role để front-end nhập

    private boolean isDeleted=false;

    @Email(message = "Invalid Email!")
    @Column(unique = true)
    private String email;

    @Pattern(regexp = "(84|0[3|5|7|8|9])+(\\d{8})", message = "Invalid phone!")
    @Column(unique = true)
    private String phone;

    @NotBlank(message = "UserName cannot be blank")
    @Pattern(regexp = "^\\S+$", message = "username cannot have space!")
    @Column(name="user_name",unique = true)
    private String userName;

    @Size(min = 6, message = "Password must be at least 6 character!")
    @Column(unique = true)
    private String password;

    @NotBlank(message = "Name cannot be blank")
    @Pattern(regexp = "^[^\\d]*$", message = "Name cannot contain numbers!")
    private String fullName;

    @NotBlank(message = "Address cannot be blank")
    private String address;


    @Column(name = "created_at", nullable = false) // Thêm tên cột và yêu cầu không null
    private LocalDateTime createdAt;

    public Account() {
        this.createdAt = LocalDateTime.now(); // Gán thời gian hiện tại khi tạo mới
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        if (this.role != null) authorities.add(new SimpleGrantedAuthority(this.role.toString()));
        return authorities;
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
    @JsonIgnore
    List<Booking> bookings;

    @OneToMany(mappedBy = "account")
    @JsonIgnore
    List<OrderCart> orderCarts;

    @OneToMany(mappedBy = "account")
    @JsonIgnore
    List<Feedback> feedbacks;

    @OneToMany(mappedBy = "account")
    @JsonIgnore
    List<QuotationProcess> quotationProcesses;

}

