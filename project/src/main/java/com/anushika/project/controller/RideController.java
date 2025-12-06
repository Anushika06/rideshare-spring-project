package com.anushika.project.controller;

import com.anushika.project.dto.CreateRideRequest;
import com.anushika.project.model.Ride;
import com.anushika.project.service.RideService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;


import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class RideController {

    private final RideService rideService;

    public RideController(RideService rideService) {
        this.rideService = rideService;
    }

    // USER: request a ride
    @PostMapping("/rides")
    public ResponseEntity<Ride> createRide(@Valid @RequestBody CreateRideRequest request,
                                           Authentication authentication) {
        Ride ride = rideService.createRide(request, authentication);
        return ResponseEntity.ok(ride);
    }

    // USER: get own rides
    @GetMapping("/user/rides")
    public ResponseEntity<List<Ride>> getUserRides(Authentication authentication) {
        return ResponseEntity.ok(rideService.getUserRides(authentication));
    }

    // DRIVER: view pending ride requests (status = REQUESTED)
    @GetMapping("/driver/rides/requests")
    public ResponseEntity<List<Ride>> getPendingRequests() {
        return ResponseEntity.ok(rideService.getPendingRides());
    }

    // DRIVER: accept a ride
    @PostMapping("/driver/rides/{rideId}/accept")
    public ResponseEntity<Ride> acceptRide(@PathVariable String rideId,
                                           Authentication authentication) {
        return ResponseEntity.ok(rideService.acceptRide(rideId, authentication));
    }

    // USER or DRIVER: complete a ride
    @PostMapping("/rides/{rideId}/complete")
    public ResponseEntity<Ride> completeRide(@PathVariable String rideId,
                                             Authentication authentication) {
        return ResponseEntity.ok(rideService.completeRide(rideId, authentication));
    }
}
