package com.example.demo.service;

import com.example.demo.entity.Account;
import com.example.demo.exception.DuplicateEntity;
import com.example.demo.model.AccountResponse;
import com.example.demo.model.LoginRequest;
import com.example.demo.model.RegisterRequest;
import com.example.demo.repository.AccountRepository;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthenticationService implements UserDetailsService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ModelMapper modelMapper;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthenticationService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
    public AccountResponse register(RegisterRequest registerRequest) {
        Account account = modelMapper.map(registerRequest,Account.class);
        try {

            String originPassword = account.getPassword();
            account.setPassword(passwordEncoder.encode(originPassword));
            Account newAccount = accountRepository.save(account);
            return modelMapper.map(newAccount,AccountResponse.class);
        } catch (Exception e) {
            if (e.getMessage().contains(account.getCode())) {
                throw new DuplicateEntity("Duplicate code!");
            } else if (e.getMessage().contains(account.getEmail())) {
                throw new DuplicateEntity("Duplicate email!");
            } else {
                throw new DuplicateEntity("Duplicate phone!");
            }
        }
    }

    @Autowired
    AuthenticationManager authenticationManager;

    public AccountResponse login(LoginRequest loginRequest){
        try {
            Authentication authentication=authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginRequest.getUserName(),
                    loginRequest.getPassword()
            ));
            // tai khoan co ton tai
            Account account =(Account) authentication.getPrincipal();
            return modelMapper.map(account,AccountResponse.class);
        }catch (Exception e){
            throw new EntityNotFoundException("User name or password is invalid !");
        }
    }

    public List<Account> getAllAccount() {
        List<Account> accounts = accountRepository.findAll();
        return accounts;
    }



    @Override
    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {
        Account account= accountRepository.findAccountByPhone(phone);
        if (account == null) {
            throw new UsernameNotFoundException("Account with phone " + phone + " not found");
        }
        return account;
    }

    public Account updateAccount(Long accountId, RegisterRequest registerRequest) {
        Account newAccount = accountRepository.findById(accountId)
                .orElseThrow(() -> new EntityNotFoundException("Account not found"));

        // Cập nhật thông tin tài khoản
        newAccount.setEmail(registerRequest.getEmail());
        newAccount.setCode(registerRequest.getCode());
        newAccount.setPhone(registerRequest.getPhone());

        // Cập nhật mật khẩu nếu có
        if (registerRequest.getPassword() != null) {
            newAccount.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        }

        return accountRepository.save(newAccount);
    }
}

