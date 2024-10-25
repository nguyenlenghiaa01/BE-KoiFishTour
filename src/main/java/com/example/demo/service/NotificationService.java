package com.example.demo.service;

import com.example.demo.Enum.Role;
import com.example.demo.entity.Account;
import com.example.demo.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void sendBookingNotification(String bookingInfo) {
        List<Account> salesAccounts = (List<Account>) accountRepository.findAccountByRole(Role.SALE);

        List<String> salesUsernames = salesAccounts.stream()
                .map(Account::getUsername)
                .collect(Collectors.toList());

        for (String saleUsername : salesUsernames) {
            messagingTemplate.convertAndSendToUser(saleUsername, "/queue/notifications",
                    "New booking request: " + bookingInfo);
        }
    }

}
