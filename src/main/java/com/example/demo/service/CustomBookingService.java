package com.example.demo.service;

import com.example.demo.Enum.QuotationEnum;
import com.example.demo.entity.*;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.Request.CustomBookingRequest;
import com.example.demo.model.Request.CustomBookingRequests;
import com.example.demo.model.Response.*;
import com.example.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
public class CustomBookingService {
    @Autowired
    QuotationRepository quotationRepository;
    @Autowired
    CustomBookingRepository customBookingRepository;
    @Autowired
    CustomTourRepository customTourRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    AuthenticationService authenticationService;
    @Autowired
    FarmRepository farmRepository;

    public CustomBooking createNewCusBooking(CustomBookingRequest customBookingRequest){
        CustomTour customTour = customTourRepository.findById(customBookingRequest.getCustomTourId()).orElseThrow(() -> new NotFoundException("Custom tour id not found!"));
        Account sale = authenticationService.getCurrentAccount();
        Account consulting = accountRepository.findAccountById(customBookingRequest.getConsultingId());
        if(consulting == null){
            throw  new NotFoundException("Consulting not found");
        }

        CustomTour newCustomTour = customTourRepository.findById(customBookingRequest.getCustomTourId())
                .orElseThrow(() -> new NotFoundException("Customize Tour not found!"));

        CustomBooking customBooking= new CustomBooking();
        customBooking.setAccount(sale);
        customBooking.setConsulting(consulting);
        customBooking.setPrice(customBookingRequest.getPrice());
        customBooking.setStatus("PENDING");
        customBooking.setCreateAt(new Date());
        customBooking.setCustomTour(newCustomTour);

        return customBookingRepository.save(customBooking);
    }

    public CustomBooking updateCus(CustomBookingRequests customBookingRequests, long id){
        CustomBooking oldCus = customBookingRepository.findById(id).orElseThrow(() -> new NotFoundException("Customer not found!"));
        oldCus.setPrice(customBookingRequests.getPrice());
        oldCus.setStatus(customBookingRequests.getStatus());
        return customBookingRepository.save(oldCus);
    }
    public DataResponse<CustomBookingResponse>getAllCusBookingByCustomerId(@RequestParam int page, @RequestParam int size, String id){
        Account customer = accountRepository.findAccountByCode(id);
        if(customer ==null){
            throw  new NotFoundException("Customer not found");
        }
        Page<CustomBooking> cusPage = customBookingRepository.findAll(
                PageRequest.of(page, size, Sort.by(Sort.Order.asc("createAt")))
        );
        List<CustomBooking> customTours = cusPage.getContent();
        List<CustomBookingResponse> customTourResponses = new ArrayList<>();

        for(CustomBooking customBooking : customTours) {
            CustomBookingResponse customTourResponse = new CustomBookingResponse();
            customTourResponse.setId(customBooking.getId());
            customTourResponse.setCusBookingId(customBooking.getCustomBookingId());
            customTourResponse.setPrice(customBooking.getPrice());
            customTourResponse.setAddress(customBooking.getCustomTour().getAddress());
            customTourResponse.setDuration(customBooking.getCustomTour().getDuration());
            customTourResponse.setStartDate(customBooking.getCustomTour().getStartDate());
            customTourResponse.setEmail(customBooking.getCustomTour().getEmail());
            customTourResponse.setPhone(customBooking.getCustomTour().getPhone());
            customTourResponse.setAdult(customBooking.getCustomTour().getAdult());
            customTourResponse.setChild(customBooking.getCustomTour().getChild());
            customTourResponse.setInfant(customBooking.getCustomTour().getInfant());
            customTourResponse.setFarm(customBooking.getCustomTour().getFarms());
            customTourResponse.setStatus(customBooking.getStatus());
            customTourResponse.setFullName(customer.getFullName());
            customTourResponse.setCustomTour(customBooking.getCustomTour());

            customTourResponses.add(customTourResponse);
        }

        DataResponse<CustomBookingResponse> dataResponse = new DataResponse<>();
        dataResponse.setListData(customTourResponses);
        dataResponse.setTotalElements(cusPage.getTotalElements());
        dataResponse.setPageNumber(cusPage.getNumber());
        dataResponse.setTotalPages(cusPage.getTotalPages());

        return dataResponse;
    }

    public CustomBooking getCusBooking(String id){
        CustomBooking customBooking = customBookingRepository.findCustomBookingByCustomBookingId(id);
        if(customBooking ==null){
            throw  new NotFoundException("Custom booking not found");
        }
        return customBooking;
    }

    public DataResponse<CustomBooking> getAllBooking(int page, int size) {
        Page bookingPage = customBookingRepository.findAll(PageRequest.of(page, size));
        List<CustomBooking> customBookingList = bookingPage.getContent();
        List<CustomBooking> customBookingList1 = new ArrayList<>();

        for (CustomBooking booking : customBookingList) {
            CustomTour customBooking = customTourRepository.findCustomTourById(booking.getCustomTour().getId());
            Account customer = accountRepository.findAccountById(customBooking.getCustomer().getId());
            if (!booking.isDeleted()) {
                customBookingList1.add(booking);
            }
        }

        DataResponse<CustomBooking> dataResponse = new DataResponse<CustomBooking>();
        dataResponse.setListData(customBookingList1);
        dataResponse.setTotalElements(bookingPage.getTotalElements());
        dataResponse.setPageNumber(bookingPage.getNumber());
        dataResponse.setTotalPages(bookingPage.getTotalPages());;

        return dataResponse;
    }

    public CustomBooking getQuotation(long id){
        Quotation quotation = quotationRepository.findById(id).orElseThrow(() -> new NotFoundException("Quotation not found!"));
        CustomBooking customBooking = customBookingRepository.findById(quotation.getCustomBooking().getId()).orElseThrow(() -> new NotFoundException("Custom Booking not found!"));
        return customBooking;
    }

    public CustomBooking deleteCusBooking(long id){
        CustomBooking oldCus = customBookingRepository.findById(id).orElseThrow(() -> new NotFoundException("Breed not found!"));

        oldCus.setDeleted(true);
        return customBookingRepository.save(oldCus);
    }

    public String createUrl(String id) throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDateTime createDate = LocalDateTime.now();
        String formattedCreateDate = createDate.format(formatter);
        CustomBooking booking = customBookingRepository.findCustomBookingByCustomBookingId(id);

        if (booking == null) {
            throw new NotFoundException("Not found booking");
        }

        double money = (booking.getQuotation().getPerAdultPrice()
                *booking.getCustomTour().getAdult()
                +booking.getQuotation().getPerChildPrice()* booking.getCustomTour().getChild()
                +booking.getPrice()) * 100;
        String amount = String.valueOf((int) money);

        String tmnCode = "V3LITBWK";
        String secretKey = "S1OJUTMQOMLRDMI8D6HVHXCVKH97P33I";
        String vnpUrl = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
        String returnUrl = "http://localhost:5173/customtour-payment-success?bookingID=" + booking.getCustomBookingId();
        String currCode = "VND";

        Map<String, String> vnpParams = new TreeMap<>();
        vnpParams.put("vnp_Version", "2.1.0");
        vnpParams.put("vnp_Command", "pay");
        vnpParams.put("vnp_TmnCode", tmnCode);
        vnpParams.put("vnp_Locale", "vn");
        vnpParams.put("vnp_CurrCode", currCode);
        vnpParams.put("vnp_TxnRef", String.valueOf(booking.getCustomBookingId()));
        vnpParams.put("vnp_OrderInfo", "Thanh toan cho ma GD: " + booking.getCustomBookingId());
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

    public CustomBooking updateStatus(String id) {
        CustomBooking booking = customBookingRepository.findCustomBookingByCustomBookingId(id);
        Quotation quotation = quotationRepository.findQuotationById(booking.getQuotation().getId());
        if (booking == null) {
            throw new NotFoundException("Booking not found!");
        }
        booking.setStatus("PAID");
        quotation.setStatus(QuotationEnum.PAID);
        quotationRepository.save(quotation);
        return customBookingRepository.save(booking);
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
}
