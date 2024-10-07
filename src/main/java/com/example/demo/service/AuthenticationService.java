package com.example.demo.service;

import com.example.demo.entity.Account;
import com.example.demo.entity.Role;
import com.example.demo.exception.DuplicateEntity;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.*;
import com.example.demo.model.Request.ForgotPasswordRequest;
import com.example.demo.model.Request.LoginRequest;
import com.example.demo.model.Request.RegisterRequest;
import com.example.demo.model.Request.ResetPasswordRequest;
import com.example.demo.model.Response.AccountResponse;
import com.example.demo.repository.AccountRepository;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
@Lazy
public class AuthenticationService implements UserDetailsService {

    @Autowired
    private AccountRepository accountRepository;

    private ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private EmailService emailService;


    public AccountResponse register(RegisterRequest registerRequest) {
        Account account = modelMapper.map(registerRequest, Account.class);
        try {
            // hash pass
            String originPassword = account.getPassword();
            account.setPassword(passwordEncoder.encode(originPassword));
            //generate code account
            String getAccountCode;
            do {
                getAccountCode = generateAccountCode(account.getRole());
            } while (accountRepository.findAccountByCode(getAccountCode) != null);
            account.setCode(getAccountCode);
            //save code account
            Account newAccount = accountRepository.save(account);
//            String otp = otpService.generateOtp(); // Tạo OTP
//            emailService.sendOtp(account.getEmail(), otp); // Gửi OTP đến email
//            // gui mail
            EmailDetail emailDetail = new EmailDetail();
            emailDetail.setReceiver(newAccount);
            emailDetail.setSubject("WelCome");
            emailDetail.setLink("https://www.ansovatravel.com/send-an-inquiry/");// ve trang home page
            emailService.sendEmail(emailDetail);
            return modelMapper.map(newAccount, AccountResponse.class);
        } catch (Exception e) {
            if (e.getMessage().contains(account.getUsername())) {
                throw new DuplicateEntity("Duplicate username!");
            } else if (e.getMessage().contains(account.getEmail())) {
                throw new DuplicateEntity("Duplicate email!");
            } else if (e.getMessage().contains(account.getPhone())) {
                throw new DuplicateEntity("Duplicate phone!");
            } else {
                System.out.println(e.getMessage());
                throw e;
            }
        }
    }

    @Autowired
    AuthenticationManager authenticationManager;

    public AccountResponse login(LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginRequest.getUserName(),
                    loginRequest.getPassword()
            ));
            // tai khoan co ton tai
            Account account = (Account) authentication.getPrincipal();
            AccountResponse accountResponse = modelMapper.map(account, AccountResponse.class);
            accountResponse.setToken(tokenService.generateToken(account));
            return accountResponse;
        } catch (Exception e) {
            throw new EntityNotFoundException("User name or password is invalid !");
        }
    }

    public List<Account> getAllAccount() {
        List<Account> accounts = accountRepository.findAll();
        return accounts;
    }


    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Account account = accountRepository.findAccountByUserName(userName);
        if (account == null) {
            throw new UsernameNotFoundException("Account with username " + userName + " not found");
        }
        return account;
    }

    public Account deleteAccount(long id){
        Account oldAccount = accountRepository.findAccountById(id);
        if(oldAccount ==null){
            throw new NotFoundException("Account not found !");//dung viec xu ly ngay tu day
        }
        oldAccount.setDeleted(true);
        return accountRepository.save(oldAccount);
    }

    public Account updateAccount(Long Id, RegisterRequest registerRequest) {
        Account oldAccount = accountRepository.findById(Id)
                .orElseThrow(() -> new EntityNotFoundException("Account not found"));

        // Cập nhật thông tin tài khoản
        oldAccount.setEmail(registerRequest.getEmail());
        oldAccount.setUserName(registerRequest.getUserName());
        oldAccount.setPhone(registerRequest.getPhone());
        oldAccount.setFullName(registerRequest.getFullName());
        oldAccount.setAddress(registerRequest.getAddress());

        if (registerRequest.getPassword() != null) {
            oldAccount.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
//            // Gửi OTP qua email khi cập nhật mật khẩu
//            String otp = otpService.generateOtp();
//            emailService.sendOtp(newAccount.getEmail(), otp);
        }

        return accountRepository.save(oldAccount);
    }

    public Account getCurrentAccount(){
        Account account = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return accountRepository.findAccountByCode(account.getCode());
    }

    //extensions
    public String generateAccountCode(Role role) {
        String accountCode;
        String prefix = role.name().substring(0, 3).toUpperCase();

        Random random = new Random();
        int randomNumber = 100000 + random.nextInt(900000);

        return prefix + randomNumber;
    }
    public void forgotPassword(ForgotPasswordRequest forgotPasswordRequest){
        Account account = accountRepository.findAccountByEmail(forgotPasswordRequest.getEmail());
        if(account == null){
            throw new NotFoundException("Email not found !");
        }else{
            EmailDetail emailDetail = new EmailDetail();
            emailDetail.setReceiver(account);
            emailDetail.setSubject("Reset Password");
            emailDetail.setLink("https://www.google.com/?token="+tokenService.generateToken(account));
            emailService.sendEmail(emailDetail);
        }
    }

    public void resetPassword(ResetPasswordRequest resetPasswordRequest){
        Account account = getCurrentAccount();
        account.setPassword(passwordEncoder.encode(resetPasswordRequest.getPassword()));
        accountRepository.save(account);

    }
}

