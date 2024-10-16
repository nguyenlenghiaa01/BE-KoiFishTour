package com.example.demo.service;

import com.example.demo.Enum.PaymentEnum;
import com.example.demo.Enum.Role;
import com.example.demo.Enum.TransactionsEnum;
import com.example.demo.entity.*;
import com.example.demo.exception.DuplicateEntity;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.Request.BookingRequest;
import com.example.demo.model.Request.KoiFishOrderRequest;
import com.example.demo.model.Response.BookingResponse;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.BookingRepository;
import com.example.demo.repository.OpenTourRepository;
import com.example.demo.repository.PaymentRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class BookingService {
    private ModelMapper modelMapper = new ModelMapper();
    // xu ly nhung logic lien qua
    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    OpenTourRepository openTourRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    PaymentRepository paymentRepository;


    @Autowired
    BookingService bookingService;
    public Booking createNewBooking(BookingRequest bookingRequest) {
        //  create booking
        Booking booking = new Booking();
        OpenTour openTour = openTourRepository.findById(bookingRequest.getOpenTourId())
                .orElseThrow(() -> new NotFoundException("Open-Tour not exist"));
        Account account = modelMapper.map(bookingRequest, Account.class);
        Account currentAccount = authenticationService.getCurrentAccount();
        if (currentAccount == null) {
            throw new RuntimeException("Customer account not found.");
        }
        booking.setBookingDate(new Date());
        booking.setStatus(bookingRequest.getStatus());
        booking.setEndDate(bookingRequest.getEndDate());
        booking.setStartDate(bookingRequest.getStartDate());
        booking.setPrice(booking.getPrice());
        booking.setOpenTour(openTour);
        booking.setAccount(currentAccount);
        //set sale id
        Account saleAccount = accountRepository.findById(bookingRequest.getSaleId())
                .orElseThrow(() -> new NotFoundException("Sale account not found"));
        booking.setSale(saleAccount);
        try {
            return bookingRepository.save(booking);
        } catch (Exception e) {
            throw new DuplicateEntity("Duplicate booking id !");
        }
    }


    public BookingResponse getAllBooking(int page, int size) {
        Page bookingPage = bookingRepository.findAll(PageRequest.of(page, size));
        List<Booking> bookings = bookingPage.getContent();
        List<Booking> activeBooking = new ArrayList<>();
        for(Booking booking : bookings) {
            if(!booking.isDeleted()) {
                activeBooking.add(booking);
            }
        }

        BookingResponse bookingResponse = new BookingResponse();
        bookingResponse.setListBooking(activeBooking);
        bookingResponse.setPageNumber(bookingPage.getNumber());
        bookingResponse.setTotalElements(bookingResponse.getTotalElements());
        bookingResponse.setTotalPages(bookingResponse.getTotalPages());

        return bookingResponse;
    }
    public Long getTotalBookingsByMonthAndYear(int month, int year) {
        return bookingRepository.countBookingsByMonthAndYear(month, year);
    }
    public Long getTotalPriceByMonthAndYear(int month, int year) {
        return bookingRepository.sumPriceByMonthAndYear(month, year);
    }

    public Long getTotalDeletedBookingsByMonthAndYear(int month, int year) {
        return bookingRepository.countDeletedBookingsByMonthAndYear(month, year);
    }

    public float getTotalBookingPayments() {
        List<Booking> bookings = bookingRepository.findAll();
        float totalPayments = 0;
        for (Booking booking : bookings) {
            if (!booking.isDeleted()) {
                totalPayments += booking.getPrice();
            }
        }
        return totalPayments;
    }

    public Booking updateBooking(BookingRequest bookingRequest, long id) {

        Booking oldBooking = bookingRepository.findBookingById(id);
        if (oldBooking == null) {
            throw new NotFoundException("Booking not found !");//dung viec xu ly ngay tu day
        }
        OpenTour openTour = openTourRepository.findById(bookingRequest.getOpenTourId())
                .orElseThrow(() -> new NotFoundException("Open-Tour not found"));
        oldBooking.setStatus(bookingRequest.getStatus());
        oldBooking.setEndDate(bookingRequest.getEndDate());
        oldBooking.setStartDate(bookingRequest.getStartDate());
        oldBooking.setPrice(bookingRequest.getPrice());
        oldBooking.setOpenTour(openTour);
        return bookingRepository.save(oldBooking);
    }

    public Booking deleteBooking(long Id) {
        Booking oldBooking = bookingRepository.findBookingById(Id);
        if (oldBooking == null) {
            throw new NotFoundException("Booking not found !");//dung viec xu ly ngay tu day
        }
        oldBooking.setDeleted(true);
        return bookingRepository.save(oldBooking);
    }

    public String createUrl(BookingRequest bookingRequest) throws  Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDateTime createDate = LocalDateTime.now();
        String formattedCreateDate = createDate.format(formatter);

        Booking booking = createNewBooking(bookingRequest);
        float money = booking.getPrice()*100;
        String amount = String.valueOf((int)money);

        String tmnCode = "V3LITBWK";
        String secretKey = "S1OJUTMQOMLRDMI8D6HVHXCVKH97P33I";
        String vnpUrl = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
        String returnUrl = "https://blearning.vn/guide/swp/docker-local?orderID=" + booking.getId();
        String currCode = "VND";

        Map<String, String> vnpParams = new TreeMap<>();
        vnpParams.put("vnp_Version", "2.1.0");
        vnpParams.put("vnp_Command", "pay");
        vnpParams.put("vnp_TmnCode", tmnCode);
        vnpParams.put("vnp_Locale", "vn");
        vnpParams.put("vnp_CurrCode", currCode);
        vnpParams.put("vnp_TxnRef", String.valueOf(booking.getId()));
        vnpParams.put("vnp_OrderInfo", "Thanh toan cho ma GD: " + booking.getId());
        vnpParams.put("vnp_OrderType", "other");
        vnpParams.put("vnp_Amount",amount);

        vnpParams.put("vnp_ReturnUrl", returnUrl);
        vnpParams.put("vnp_CreateDate", formattedCreateDate);
        vnpParams.put("vnp_IpAddr", "128.199.178.23");

        StringBuilder signDataBuilder = new StringBuilder();
        for (Map.Entry<String, String> entry : vnpParams.entrySet()) {
            signDataBuilder.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8.toString()));
            signDataBuilder.append("=");
            signDataBuilder.append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8.toString()));
            signDataBuilder.append("&");
        }
        signDataBuilder.deleteCharAt(signDataBuilder.length() - 1); // Remove last '&'

        String signData = signDataBuilder.toString();
        String signed = generateHMAC(secretKey, signData);

        vnpParams.put("vnp_SecureHash", signed);

        StringBuilder urlBuilder = new StringBuilder(vnpUrl);
        urlBuilder.append("?");
        for (Map.Entry<String, String> entry : vnpParams.entrySet()) {
            urlBuilder.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8.toString()));
            urlBuilder.append("=");
            urlBuilder.append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8.toString()));
            urlBuilder.append("&");
        }
        urlBuilder.deleteCharAt(urlBuilder.length() - 1); // Remove last '&'

        return urlBuilder.toString();
    }

    private String generateHMAC(String secretKey, String signData) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac hmacSha512 = Mac.getInstance("HmacSHA512");
        SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
        hmacSha512.init(keySpec);
        byte[] hmacBytes = hmacSha512.doFinal(signData.getBytes(StandardCharsets.UTF_8));

        StringBuilder result = new StringBuilder();
        for (byte b : hmacBytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }

    public void createTransactionId(long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Booking Not Found"));

        // Tạo đối tượng Payment
        Payment payment = new Payment();
        payment.setBooking(booking);
        payment.setCreateAt(new Date());
        payment.setPayment_method(PaymentEnum.BANKING);

        Set<Transactions> transactionsSet = new HashSet<>();

        // Tạo giao dịch nạp tiền cho khách hàng
        Transactions transactions = new Transactions();
        Account customer = authenticationService.getCurrentAccount();
        transactions.setFrom(null); // Nạp tiền không có người gửi
        transactions.setReceiver(booking.getAccount()); // Người nhận là khách hàng
        transactions.setPayment(payment);
        transactions.setStatus(TransactionsEnum.SUCCESS); // Set trang thai
        transactions.setDescription("NAP TIEN TO CUSTOMER");
        transactionsSet.add(transactions);

        // Tạo giao dịch từ khách hàng đến admin
        Transactions transactions1 = new Transactions();
        Account admin = accountRepository.findAccountByRole(Role.ADMIN);
        transactions1.setFrom(customer); // Người gửi là khách hàng
        transactions1.setReceiver(admin); // Người nhận là admin
        transactions1.setPayment(payment);
        transactions1.setStatus(TransactionsEnum.SUCCESS);
        transactions1.setDescription("CUSTOMER TO ADMIN");
        transactionsSet.add(transactions1);

        // Cập nhật số dư admin
        float newBalance = admin.getBalance() + booking.getPrice();
        admin.setBalance(newBalance);

        // set transaction vao payment
        payment.setTransactions(transactionsSet);

        // save all
        paymentRepository.save(payment); // save payment transactions
        accountRepository.save(admin);   // update admin
    }
}
