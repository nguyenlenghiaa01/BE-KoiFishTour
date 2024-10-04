package com.example.demo.api;

import com.example.demo.entity.Account;
import com.example.demo.model.Request.ForgotPasswordRequest;
import com.example.demo.model.Request.LoginRequest;
import com.example.demo.model.Request.RegisterRequest;
import com.example.demo.model.Request.ResetPasswordRequest;
import com.example.demo.model.Response.AccountResponse;
import com.example.demo.service.AuthenticationService;
import com.example.demo.service.EmailService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api")
@SecurityRequirement(name = "api")
@CrossOrigin("*")
public class AuthenticationAPI {

    // DI: Dependency Injection
    ModelMapper modelMapper = new ModelMapper();
    @Autowired
    AuthenticationService authenticationService;
    @Autowired
    EmailService emailService;

    @PostMapping("register")
    public ResponseEntity <AccountResponse>register(@Valid @RequestBody RegisterRequest registerRequest) {
        AccountResponse newAccount = authenticationService.register(registerRequest);
        String email = registerRequest.getEmail();
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

    @DeleteMapping("{id}")
    public ResponseEntity deleteAcount(@PathVariable long id){
        Account newAccount = authenticationService.deleteAccount(id);
        return ResponseEntity.ok(newAccount);
    }


    @PostMapping("/loginGoogle")
    public ResponseEntity<String> login(@RequestBody String idToken) {
        try {
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
            String uid = decodedToken.getUid();
            return ResponseEntity.ok("User ID: " + uid);
        } catch (FirebaseAuthException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Error: " + e.getMessage());
        }
    }



    @PostMapping("forgot-password")
    public ResponseEntity forgotPassword(@Valid @RequestBody ForgotPasswordRequest forgotPasswordRequest){
        authenticationService.forgotPassword(forgotPasswordRequest);
        return ResponseEntity.ok("forgot password successfully");
    }

    @PostMapping("reset-password")
    public ResponseEntity resetPassword(@Valid @RequestBody ResetPasswordRequest resetPasswordRequest){
        authenticationService.resetPassword(resetPasswordRequest);
        return ResponseEntity.ok("reset password successfully");
    }

}


