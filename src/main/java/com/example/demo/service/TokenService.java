package com.example.demo.service;

import com.example.demo.entity.Account;
import com.example.demo.repository.AccountRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class TokenService {
    // tao ra token
    @Autowired
    AccountRepository accountRepository;
    private String SECRET_KEY="4bb6d1dfbafb64a681139d1586b6f1160d18159afd57c8c79136d7490630407c";

    private SecretKey getSigninKey(){
        byte[]keyBytes= Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    public String generateToken(Account account){ // genera cho account nao do su dung nhu customer chi sai dc chuc nang cua customer
    String token = Jwts.builder()
            .subject(account.getId()+"")
            .issuedAt(new Date(System.currentTimeMillis()))// tao luc 10g 30
            .expiration(new Date(System.currentTimeMillis() +10008*60*60*24))
            .signWith(getSigninKey())
            .compact();
    return token;
    }
    // verify token
    public Account getAccountByToken(String token){
        Claims claims = Jwts.parser()
                .verifyWith(getSigninKey())
                .build()
                .parseEncryptedClaims(token)
                .getPayload();

        String idString = claims.getSubject();
        long id = Long.parseLong(idString);
        return accountRepository.findAccountById(id);
    }
}
