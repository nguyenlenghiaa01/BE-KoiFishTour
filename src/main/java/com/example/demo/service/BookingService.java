package com.example.demo.service;

import com.example.demo.Enum.PaymentEnum;
import com.example.demo.Enum.Role;
import com.example.demo.Enum.TransactionsEnum;
import com.example.demo.entity.*;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.EmailDetail;
import com.example.demo.model.Request.BookingRequest;
import com.example.demo.model.Response.*;
import com.example.demo.repository.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

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
    @Autowired
    BookingRepository bookingRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    AuthenticationService authenticationService;
    @Autowired
    PaymentRepository paymentRepository;
    @Autowired
    EmailService emailService;
    @Autowired
    TourRepository tourRepository;
    @Autowired
    QuotationRepository quotationRepository;
    @Autowired
    FarmRepository farmRepository;
    @Autowired
    BookingService bookingService;

    public Booking createNewBooking(BookingRequest bookingRequest) {
        Tour tour = tourRepository.findTourByTourId(bookingRequest.getTourId());
        if (tour == null) {
            throw new NotFoundException("Not found tour");
        }

        Account customer = accountRepository.findAccountByCode(bookingRequest.getCustomerId());
        if (customer == null) {
            throw new NotFoundException("Not found customer");
        }

        Booking booking = new Booking();
        booking.setBookingDate(new Date());
        booking.setStatus("PENDING");
        booking.setPrice(bookingRequest.getPrice());
        booking.setFullName(bookingRequest.getFullName());
        booking.setEmail(bookingRequest.getEmail());
        booking.setPhone(bookingRequest.getPhone());
        booking.setChild(bookingRequest.getChild());
        booking.setAdult(bookingRequest.getAdult());
        booking.setInfant(bookingRequest.getInfant());
        booking.setTour(tour);
        booking.setAccount(customer);


        return bookingRepository.save(booking);

    }


    public DataResponse<BookingResponse> getAllBookingCustomer(@RequestParam int page,
                                                               @RequestParam int size,
                                                               @RequestParam long customerId) {
        Page<Booking> bookingPage = bookingRepository.findByAccountIdAndIsDeletedFalse(customerId, PageRequest.of(page, size));
        List<Booking> bookings = bookingPage.getContent();


        List<BookingResponse> activeBookings = new ArrayList<>();
        for (Booking booking : bookings) {
            BookingResponse bookingResponse = new BookingResponse();
            bookingResponse.setBookingId(booking.getId());
            bookingResponse.setEmail(booking.getEmail());
            bookingResponse.setFullName(booking.getFullName());
            bookingResponse.setStatus(booking.getStatus());
            bookingResponse.setAdult(booking.getAdult());
            bookingResponse.setInfant(booking.getInfant());
            bookingResponse.setPrice(booking.getPrice());
            bookingResponse.setPhone(booking.getPhone());
            bookingResponse.setTourId(booking.getTour().getId());
            bookingResponse.setCustomerId(booking.getAccount().getId());
            bookingResponse.setBookingDate(booking.getBookingDate());

            activeBookings.add(bookingResponse);
        }

        DataResponse<BookingResponse> dataResponse = new DataResponse<>();
        dataResponse.setListData(activeBookings);
        dataResponse.setPageNumber(bookingPage.getNumber());
        dataResponse.setTotalElements(bookingPage.getTotalElements());
        dataResponse.setTotalPages(bookingPage.getTotalPages());

        return dataResponse;
    }

    public DataResponse<BookingResponses> getBookingByCustomer(@RequestParam int page,
                                                               @RequestParam int size,
                                                               @RequestParam String id) {
        Account customer = accountRepository.findAccountByCode(id);
        if (customer == null) {
            throw new NotFoundException("Customer not found!");
        }

        Page<Booking> bookingPage = bookingRepository.findByStatusAndAccount_Code("PAID", id, PageRequest.of(page, size));
        List<BookingResponses> activeBookings = new ArrayList<>();

        for (Booking booking : bookingPage.getContent()) {
            if (!booking.isDeleted()) {
                BookingResponses bookingResponse = new BookingResponses();
                bookingResponse.setBookingId(booking.getBookingId());
                bookingResponse.setEmail(booking.getEmail());
                bookingResponse.setFullName(booking.getFullName());
                bookingResponse.setStatus(booking.getStatus());
                bookingResponse.setAdult(booking.getAdult());
                bookingResponse.setInfant(booking.getInfant());
                bookingResponse.setPhone(booking.getPhone());
                bookingResponse.setCustomerId(booking.getAccount().getId());

                Tour tour = booking.getTour();
                if (tour != null) {
                    TourResponses tourResponse = new TourResponses();
                    tourResponse.setId(tour.getId());
                    tourResponse.setTourName(tour.getTourName());
                    tourResponse.setDescription(tour.getDescription());
                    tourResponse.setStartDate(tour.getStartDate());
                    tourResponse.setPrice(tour.getPrice());
                    tourResponse.setImage(tour.getImage());
                    tourResponse.setDuration(tour.getDuration());
                    tourResponse.setTourId(tour.getTourId());
                    tourResponse.setTime(tour.getTime());

                    Account consulting = tour.getAccount();
                    if (consulting != null) {
                        tourResponse.setConsultingName(consulting.getFullName());
                    }

                    bookingResponse.setTourId(tourResponse);
                }

                activeBookings.add(bookingResponse);
            }
        }

        DataResponse<BookingResponses> dataResponse = new DataResponse<>();
        dataResponse.setListData(activeBookings);
        dataResponse.setPageNumber(bookingPage.getNumber());
        dataResponse.setTotalElements(bookingPage.getTotalElements());
        dataResponse.setTotalPages(bookingPage.getTotalPages());

        return dataResponse;
    }

    public DataResponse<BookingResponsess> getAllBookingByConsulting(@RequestParam int page,
                                                                     @RequestParam int size,
                                                                     @RequestParam long tourId) {
        Page<Booking> bookingPage = bookingRepository.findByTourIdAndIsDeletedFalse(tourId, PageRequest.of(page, size));
        List<Booking> bookings = bookingPage.getContent();

        List<BookingResponsess> activeBookings = new ArrayList<>();
        for (Booking booking : bookings) {
            BookingResponsess bookingResponse = new BookingResponsess();
            bookingResponse.setBookingId(booking.getBookingId());
            bookingResponse.setEmail(booking.getEmail());
            bookingResponse.setFullName(booking.getFullName());
            bookingResponse.setStatus(booking.getStatus());
            bookingResponse.setAdult(booking.getAdult());
            bookingResponse.setInfant(booking.getInfant());
            bookingResponse.setPrice(booking.getPrice());
            bookingResponse.setPhone(booking.getPhone());
            bookingResponse.setTourId(booking.getTour().getId());
            bookingResponse.setCustomerId(booking.getAccount().getId());
            bookingResponse.setBookingDate(booking.getBookingDate());

            activeBookings.add(bookingResponse);
        }

        DataResponse<BookingResponsess> dataResponse = new DataResponse<>();
        dataResponse.setListData(activeBookings);
        dataResponse.setPageNumber(bookingPage.getNumber());
        dataResponse.setTotalElements(bookingPage.getTotalElements());
        dataResponse.setTotalPages(bookingPage.getTotalPages());

        return dataResponse;
    }

    public DataResponse<BookingsResponse> getAllBooking(@RequestParam int page, @RequestParam int size) {
        Page<Booking> bookingPage = bookingRepository.findAll(PageRequest.of(page, size));
        List<Booking> bookings = bookingPage.getContent();
        List<BookingsResponse> activeBookings = new ArrayList<>();
        for (Booking booking : bookings) {
            if (!booking.isDeleted()) {
                BookingsResponse bookingResponse = new BookingsResponse();
                bookingResponse.setBookingId(booking.getId());
                bookingResponse.setEmail(booking.getEmail());
                bookingResponse.setFullName(booking.getFullName());
                bookingResponse.setStatus(booking.getStatus());
                bookingResponse.setAdult(booking.getAdult());
                bookingResponse.setChild(booking.getChild());
                bookingResponse.setInfant(booking.getInfant());
                bookingResponse.setPhone(booking.getPhone());
                bookingResponse.setPrice(booking.getPrice());
                bookingResponse.setTourName(booking.getTour().getTourName());
                bookingResponse.setCustomerId(booking.getAccount().getId());

                activeBookings.add(bookingResponse);
            }
        }

        DataResponse<BookingsResponse> dataResponse = new DataResponse<>();
        dataResponse.setListData(activeBookings);
        dataResponse.setPageNumber(bookingPage.getNumber());
        dataResponse.setTotalElements(bookingPage.getTotalElements());
        dataResponse.setTotalPages(bookingPage.getTotalPages());

        return dataResponse;
    }

    public Long getTotalBookingsByMonthAndYear(int month, int year) {
        return bookingRepository.countBookingsByTourStartDate(month, year);
    }

    public Long getTotalPriceByMonthAndYear(int month, int year) {
        return bookingRepository.sumPriceByTourStartDate(month, year);
    }

    public Long getTotalDeletedBookingsByMonthAndYear(int month, int year) {
        return bookingRepository.countFailedBookingsByTourStartDate(month, year);
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


    public Booking deleteBooking(String Id) {
        Booking oldBooking = bookingRepository.findBookingByBookingId(Id);
        if (oldBooking == null) {
            throw new NotFoundException("Booking not found !");
        }
        oldBooking.setDeleted(true);
        return bookingRepository.save(oldBooking);
    }

    public String createUrl(String id) throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDateTime createDate = LocalDateTime.now();
        String formattedCreateDate = createDate.format(formatter);
        Booking booking = bookingRepository.findBookingByBookingId(id);

        if (booking == null) {
            throw new NotFoundException("Not found booking");
        }

        double money = booking.getPrice() * 100;
        String amount = String.valueOf((int) money);

        String tmnCode = "V3LITBWK";
        String secretKey = "S1OJUTMQOMLRDMI8D6HVHXCVKH97P33I";
        String vnpUrl = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
        String returnUrl = "http://localhost:5173/tourpayment-success?bookingID=" + booking.getBookingId();
        String currCode = "VND";

        Map<String, String> vnpParams = new TreeMap<>();
        vnpParams.put("vnp_Version", "2.1.0");
        vnpParams.put("vnp_Command", "pay");
        vnpParams.put("vnp_TmnCode", tmnCode);
        vnpParams.put("vnp_Locale", "vn");
        vnpParams.put("vnp_CurrCode", currCode);
        vnpParams.put("vnp_TxnRef", String.valueOf(booking.getBookingId()));
        vnpParams.put("vnp_OrderInfo", "Thanh toan cho ma GD: " + booking.getBookingId());
        vnpParams.put("vnp_OrderType", "other");
        vnpParams.put("vnp_Amount", amount);

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

    public Booking updateStatus(String id) {
        Booking booking = bookingRepository.findBookingByBookingId(id);
        if (booking == null) {
            throw new NotFoundException("Booking not found!");
        }
        booking.setStatus("PAID");
        return bookingRepository.save(booking);


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

    public String createTransactionId(String id) {
        Booking booking = bookingRepository.findBookingByBookingId(id);
        if (booking == null) {
            throw new NotFoundException("Booking not found");
        }

        // Create payment
        Payment payment = new Payment();
        payment.setBooking(booking);
        payment.setCreateAt(new Date());
        payment.setPayment_method(PaymentEnum.BANKING);

        Set<Transactions> transactionsSet = new HashSet<>();

        // Create transactions for customer
        Transactions transactions = new Transactions();
        Account customer = authenticationService.getCurrentAccount();
        transactions.setFrom(null); // nguoi nap tien la null
        transactions.setReceiver(booking.getAccount()); // nhan la customer
        transactions.setPayment(payment);
        transactions.setStatus(TransactionsEnum.SUCCESS); // Set trang thai
        transactions.setDescription("NAP TIEN TO CUSTOMER");
        transactionsSet.add(transactions);

        Transactions transactions1 = new Transactions();
        Account admin = accountRepository.findAccountByRole(Role.ADMIN);
        transactions1.setFrom(customer); //gui la customer
        transactions1.setReceiver(admin); // nhan la admin
        transactions1.setPayment(payment);
        transactions1.setStatus(TransactionsEnum.SUCCESS);
        transactions1.setDescription("CUSTOMER TO ADMIN");
        transactionsSet.add(transactions1);

        double newBalance = admin.getBalance() + booking.getPrice();
        admin.setBalance(newBalance);

        // set transaction vao payment
        payment.setTransactions(transactionsSet);

        // save all
        paymentRepository.save(payment); // save payment transactions
        accountRepository.save(admin);   // update admin

        EmailDetail emailDetail = new EmailDetail();
        emailDetail.setReceiver(customer);
        emailDetail.setSubject("Payment success");
        emailDetail.setLink("https://www.ansovatravel.com/send-an-inquiry/");
        emailService.sendEmail(emailDetail);

        return "Payment transaction created successfully for booking ID: " + id;
    }

    public Booking deleteBookings(String id) {
        Booking booking = bookingRepository.findBookingByBookingId(id);
        if (booking == null) {
            throw new NotFoundException("Booking not found");
        }
        bookingRepository.delete(booking);
        return booking;
    }


}
