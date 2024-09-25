package com.example.demo.api;

import com.example.demo.entity.Account;
import com.example.demo.model.AccountResponse;
import com.example.demo.model.LoginRequest;
import com.example.demo.model.RegisterRequest;
import com.example.demo.service.AuthenticationService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api")
public class AuthenticationAPI {

    // DI: Dependency Injection
    ModelMapper modelMapper = new ModelMapper();
    @Autowired
    AuthenticationService authenticationService;

    @PostMapping("register")
    public ResponseEntity <AccountResponse>register(@Valid @RequestBody RegisterRequest registerRequest) {
        AccountResponse newAccount = authenticationService.register(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(newAccount);
    }
    @PostMapping("login")
    public ResponseEntity <AccountResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        AccountResponse accountResponse = authenticationService.login(loginRequest);
        if (accountResponse == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(accountResponse);
    }


    @GetMapping("account")
    public ResponseEntity <List<Account>>getAllAccount(){
        List<Account> accounts = authenticationService.getAllAccount();
        return ResponseEntity.ok(accounts);
    }
    @PutMapping("/account/{id}")
    public ResponseEntity<AccountResponse> updateAccount(
            @PathVariable Long id,
            @RequestBody RegisterRequest registerRequest) {
        try {
            Account updatedAccount = authenticationService.updateAccount(id, registerRequest);
            AccountResponse accountResponse = modelMapper.map(updatedAccount, AccountResponse.class);
            return ResponseEntity.ok(accountResponse);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
    @PostMapping("/loginGoogle")
    public ResponseEntity<String> login(@RequestBody String idToken) {
        try {
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
            String uid = decodedToken.getUid();
            return ResponseEntity.ok("User ID: " + uid); // Xử lý thêm nếu cần
        } catch (FirebaseAuthException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Error: " + e.getMessage());
        }
    }

}


