package com.example.demo.api;

import com.example.demo.entity.Farm;
import com.example.demo.entity.KoiFish;

import com.example.demo.model.Request.KoiFishRequest;
import com.example.demo.model.Response.DataResponse;
import com.example.demo.model.Response.KoiFishByFarmResponse;
import com.example.demo.model.Response.KoiFishResponse;
import com.example.demo.service.KoiFishService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;


@RestController
@RequestMapping("/api/koi")
@CrossOrigin("*")
@SecurityRequirement(name = "api")
public class KoiFishAPI {

    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    private KoiFishService koiService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public ResponseEntity<KoiFish> create(@Valid @RequestBody KoiFishRequest koiFishRequest) {
            KoiFish newKoiFish = koiService.createNewKoi(koiFishRequest);
            simpMessagingTemplate.convertAndSend("topic/koi","CREATE NEW KOI");
            return ResponseEntity.ok(newKoiFish);
    }

    @GetMapping("/guest/get")
    public ResponseEntity<DataResponse<KoiFishResponse>> get(@RequestParam int page, @RequestParam int size){
        DataResponse<KoiFishResponse>dataResponse = koiService.getAllKoi(page, size);
        return ResponseEntity.ok(dataResponse);
    }
    @GetMapping("/list-koi/farm-id")
    public ResponseEntity<List<KoiFish>> get(long id){
        List<KoiFish>dataResponse = koiService.getKoiByFarmId(id);
        return ResponseEntity.ok(dataResponse);
    }

    @GetMapping("/list-koi/tour-id")
    public ResponseEntity<Set<KoiFish>> getKoiFish(String tourId) {
        Set<KoiFish> getFishes = koiService.getKoiByTourId(tourId);

        return ResponseEntity.ok(getFishes);
    }

    @GetMapping("/list-koi/booking-id")
    public ResponseEntity<List<KoiFishByFarmResponse>> getKoiFishByBooking(@RequestParam String bookingId) {
        List<KoiFishByFarmResponse> getFishes = koiService.getKoiByBookingId(bookingId);
        return ResponseEntity.ok(getFishes);
    }
    @GetMapping("/list-koi/custom-booking-id")
    public ResponseEntity<List<KoiFishByFarmResponse>> getKoiFishByBookings(@RequestParam String customBookingId) {
        List<KoiFishByFarmResponse> getFishes = koiService.getKoiByCustomBookingId(customBookingId);
        return ResponseEntity.ok(getFishes);
    }

    @GetMapping("/listKoiFish")
    public ResponseEntity<DataResponse<KoiFish>> getList(@RequestParam int page,@RequestParam int size ,String id){
        DataResponse<KoiFish> koiFishDataResponse = koiService.getListKoiFish(page, size, id);
        return ResponseEntity.ok(koiFishDataResponse);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("{id}")
    public ResponseEntity<KoiFish> updateKoiFish(@Valid @RequestBody KoiFishRequest koi, @PathVariable long id) {
            KoiFish updatedKoiFish = koiService.updateKoiFish(koi, id);
        simpMessagingTemplate.convertAndSend("topic/koi","UPDATE KOI");
            return ResponseEntity.ok(updatedKoiFish);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("{id}")
    public ResponseEntity<KoiFish> deleteKoi(@PathVariable long id) {
            KoiFish deletedKoiFish = koiService.deleteKoi(id);
        simpMessagingTemplate.convertAndSend("topic/koi","DELETE KOI");
            return ResponseEntity.ok(deletedKoiFish);
    }
}
