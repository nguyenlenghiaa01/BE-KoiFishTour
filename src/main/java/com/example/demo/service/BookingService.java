package com.example.demo.service;

import com.example.demo.entity.Account;
import com.example.demo.entity.Booking;
import com.example.demo.entity.OpenTour;
import com.example.demo.exception.DuplicateEntity;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.Request.BookingRequest;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.BookingRepository;
import com.example.demo.repository.OpenTourRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public Booking createNewBooking(BookingRequest bookingRequest) {
        Booking booking = modelMapper.map(bookingRequest, Booking.class);
        OpenTour openTour = openTourRepository.findById(bookingRequest.getOpenTourId()).
                orElseThrow(() -> new NotFoundException("Open-Tour not exist"));

        Account account = accountRepository.findById(bookingRequest.getAccountId()).
                orElseThrow(() -> new NotFoundException("Account not exist"));

        booking.setOpenTour(openTour);
        booking.setAccount(account);
        try {

            Booking newBooking = bookingRepository.save(booking);
            return newBooking;
        } catch (Exception e) {
            throw new DuplicateEntity("Duplicate booking id !");
        }

    }

    public List<Booking> getAllBooking() {
        // lay tat ca student trong DB
        List<Booking> bookings = bookingRepository.findBookingsByIsDeletedFalse();
        return bookings;
    }

    public Booking updateBooking(BookingRequest bookingRequest, long id) {

        Booking oldBooking = bookingRepository.findBookingById(id);
        if (oldBooking == null) {
            throw new NotFoundException("Booking not found !");//dung viec xu ly ngay tu day
        }
        //=> co breed co ton tai;

        oldBooking.setStatus(bookingRequest.getStatus());
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
}
