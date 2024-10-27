package com.example.demo.service;

import com.example.demo.Enum.PaymentEnum;
import com.example.demo.Enum.Role;
import com.example.demo.Enum.TransactionsEnum;
import com.example.demo.entity.*;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.EmailDetail;
import com.example.demo.model.Request.BookingRequest;
import com.example.demo.model.Response.BookingResponse;
import com.example.demo.model.Response.BookingsResponse;
import com.example.demo.model.Response.DataResponse;
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
    private ModelMapper modelMapper = new ModelMapper();
    // xu ly nhung logic lien qua
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
    NotificationService notificationService;

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
//            bookingResponse.setDuration(booking.getDuration());
//            bookingResponse.setAddress(booking.getAddress());
            bookingResponse.setFullName(booking.getFullName());
//            bookingResponse.setStartDate(booking.getStartDate());
            bookingResponse.setStatus(booking.getStatus());
            bookingResponse.setAdult(booking.getAdult());
            bookingResponse.setInfant(booking.getInfant());
            bookingResponse.setPrice(booking.getPrice());
            bookingResponse.setPhone(booking.getPhone());
            bookingResponse.setTourId(booking.getTour().getId());
            bookingResponse.setCustomerId(booking.getAccount().getId());

            activeBookings.add(bookingResponse);
        }

        DataResponse<BookingResponse> dataResponse = new DataResponse<>();
        dataResponse.setListData(activeBookings);
        dataResponse.setPageNumber(bookingPage.getNumber());
        dataResponse.setTotalElements(bookingPage.getTotalElements());
        dataResponse.setTotalPages(bookingPage.getTotalPages());

        return dataResponse;
    }
    public DataResponse<BookingResponse> getBookingByCustomer(@RequestParam int page,
                                                                   @RequestParam int size,
                                                                   @RequestParam String  id) {
        Account customer = accountRepository.findAccountByCode(id);
        Page<Booking> bookingPage = bookingRepository.findByAccountCode(id, PageRequest.of(page, size));
        List<Booking> bookings = bookingPage.getContent();

        List<BookingResponse> activeBookings = new ArrayList<>();
        for (Booking booking : bookings) {
            BookingResponse bookingResponse = new BookingResponse();
            bookingResponse.setBookingId(booking.getId());
            bookingResponse.setEmail(booking.getEmail());
//            bookingResponse.setDuration(booking.getDuration());
//            bookingResponse.setAddress(booking.getAddress());
            bookingResponse.setFullName(booking.getFullName());
//            bookingResponse.setStartDate(booking.getStartDate());
            bookingResponse.setStatus(booking.getStatus());
            bookingResponse.setAdult(booking.getAdult());
            bookingResponse.setInfant(booking.getInfant());
            bookingResponse.setPrice(booking.getPrice());
            bookingResponse.setPhone(booking.getPhone());
            bookingResponse.setTourId(booking.getTour().getId());
            bookingResponse.setCustomerId(booking.getAccount().getId());

            activeBookings.add(bookingResponse);
        }

        DataResponse<BookingResponse> dataResponse = new DataResponse<>();
        dataResponse.setListData(activeBookings);
        dataResponse.setPageNumber(bookingPage.getNumber());
        dataResponse.setTotalElements(bookingPage.getTotalElements());
        dataResponse.setTotalPages(bookingPage.getTotalPages());

        return dataResponse;
    }


    public DataResponse<BookingResponse> getAllBookingByConsulting(@RequestParam int page,
                                                                   @RequestParam int size,
                                                                   @RequestParam long tourId) {
        Page<Booking> bookingPage = bookingRepository.findByTourIdAndIsDeletedFalse(tourId, PageRequest.of(page, size));
        List<Booking> bookings = bookingPage.getContent();

        List<BookingResponse> activeBookings = new ArrayList<>();
        for (Booking booking : bookings) {
            BookingResponse bookingResponse = new BookingResponse();
            bookingResponse.setBookingId(booking.getId());
            bookingResponse.setEmail(booking.getEmail());
//            bookingResponse.setDuration(booking.getDuration());
//            bookingResponse.setAddress(booking.getAddress());
            bookingResponse.setFullName(booking.getFullName());
//            bookingResponse.setStartDate(booking.getStartDate());
            bookingResponse.setStatus(booking.getStatus());
            bookingResponse.setAdult(booking.getAdult());
            bookingResponse.setInfant(booking.getInfant());
            bookingResponse.setPrice(booking.getPrice());
            bookingResponse.setPhone(booking.getPhone());
            bookingResponse.setTourId(booking.getTour().getId());
            bookingResponse.setCustomerId(booking.getAccount().getId());

            activeBookings.add(bookingResponse);
        }

        DataResponse<BookingResponse> dataResponse = new DataResponse<>();
        dataResponse.setListData(activeBookings);
        dataResponse.setPageNumber(bookingPage.getNumber());
        dataResponse.setTotalElements(bookingPage.getTotalElements());
        dataResponse.setTotalPages(bookingPage.getTotalPages());

        return dataResponse;
    }

    public Booking getQuotation(long id){
        Quotation quotation = quotationRepository.findById(id).orElseThrow(() -> new NotFoundException("Quotation not found!"));
        Booking booking = bookingRepository.findById(quotation.getBooking().getId()).orElseThrow(() -> new NotFoundException("Booking not found!"));
        return booking;
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
//                bookingResponse.setDuration(booking.getDuration());
//                bookingResponse.setAddress(booking.getAddress());
                bookingResponse.setFullName(booking.getFullName());
//                bookingResponse.setStartDate(booking.getStartDate());
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
        return bookingRepository.countDeletedBookingsByTourStartDate(month, year);
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
        // Tìm booking cũ theo ID
        Booking oldBooking = bookingRepository.findBookingById(id);
        if (oldBooking == null) {
            throw new NotFoundException("Booking not found !");
        }

        // Cập nhật các thông tin từ bookingRequest
        oldBooking.setStatus(bookingRequest.getStatus());
//        oldBooking.setDuration(bookingRequest.getDuration());
//        oldBooking.setStartDate(bookingRequest.getStartDate());
        oldBooking.setPrice(bookingRequest.getPrice());
        oldBooking.setFullName(bookingRequest.getFullName());
        oldBooking.setEmail(bookingRequest.getEmail());
        oldBooking.setPhone(bookingRequest.getPhone());
//        oldBooking.setAddress(bookingRequest.getAddress());
        oldBooking.setAdult(bookingRequest.getAdult());
        oldBooking.setInfant(bookingRequest.getInfant());
        oldBooking.setChild(bookingRequest.getChild());

        // Lưu và trả về booking đã cập nhật
        return bookingRepository.save(oldBooking);
    }

    public Booking deleteBooking(long Id) {
        Booking oldBooking = bookingRepository.findBookingById(Id);
        if (oldBooking == null) {
            throw new NotFoundException("Booking not found !");
        }
        oldBooking.setDeleted(true);
        return bookingRepository.save(oldBooking);
    }

    public String createUrl(long id) throws  Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDateTime createDate = LocalDateTime.now();
        String formattedCreateDate = createDate.format(formatter);
        Booking booking = bookingRepository.findBookingById(id);
        if(booking == null){
            throw new NotFoundException("Not found booking");
        }
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

    public String createTransactionId(long id) {
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

        Transactions transactions1 = new Transactions();
        Account admin = accountRepository.findAccountByRole(Role.ADMIN);
        transactions1.setFrom(customer); // Người gửi là khách hàng
        transactions1.setReceiver(admin); // Người nhận là admin
        transactions1.setPayment(payment);
        transactions1.setStatus(TransactionsEnum.SUCCESS);
        transactions1.setDescription("CUSTOMER TO ADMIN");
        transactionsSet.add(transactions1);

        float newBalance = admin.getBalance() + booking.getPrice();
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
}
