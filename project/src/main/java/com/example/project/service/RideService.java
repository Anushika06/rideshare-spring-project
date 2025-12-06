package com.example.project.service;

import com.example.project.dto.CreateRideRequest;
import com.example.project.exception.BadRequestException;
import com.example.project.exception.NotFoundException;
import com.example.project.model.Ride;
import com.example.project.repository.RideRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class RideService {

    private final RideRepository rideRepository;

    public RideService(RideRepository rideRepository) {
        this.rideRepository = rideRepository;
    }

    // USER: request a new ride
    public Ride createRide(CreateRideRequest req, Authentication authentication) {
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();

        if (!"ROLE_USER".equals(user.getRole())) {
            throw new BadRequestException("Only ROLE_USER can request rides");
        }

        Ride ride = new Ride();
        ride.setUserId(user.getId());
        ride.setPickupLocation(req.getPickupLocation());
        ride.setDropLocation(req.getDropLocation());
        ride.setStatus("REQUESTED");
        ride.setCreatedAt(new Date());

        return rideRepository.save(ride);
    }

    // DRIVER: see all rides with status REQUESTED
    public List<Ride> getPendingRides() {
        return rideRepository.findByStatus("REQUESTED");
    }

    // DRIVER: accept a ride
    public Ride acceptRide(String rideId, Authentication auth) {
        CustomUserDetails driver = (CustomUserDetails) auth.getPrincipal();

        if (!"ROLE_DRIVER".equals(driver.getRole())) {
            throw new BadRequestException("Only ROLE_DRIVER can accept rides");
        }

        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new NotFoundException("Ride not found"));

        if (!"REQUESTED".equals(ride.getStatus())) {
            throw new BadRequestException("Ride is not in REQUESTED state");
        }

        ride.setDriverId(driver.getId());
        ride.setStatus("ACCEPTED");

        return rideRepository.save(ride);
    }

    // USER or DRIVER: complete a ride
    public Ride completeRide(String rideId, Authentication auth) {
        CustomUserDetails user = (CustomUserDetails) auth.getPrincipal();

        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new NotFoundException("Ride not found"));

        if (!"ACCEPTED".equals(ride.getStatus())) {
            throw new BadRequestException("Ride must be ACCEPTED to complete");
        }


        boolean isPassenger = ride.getUserId().equals(user.getId());
        boolean isDriver = ride.getDriverId() != null && ride.getDriverId().equals(user.getId());

        if (!isPassenger && !isDriver) {
            throw new BadRequestException("You are not part of this ride");
        }

        ride.setStatus("COMPLETED");
        return rideRepository.save(ride);
    }

    // USER: see own rides
    public List<Ride> getUserRides(Authentication auth) {
        CustomUserDetails user = (CustomUserDetails) auth.getPrincipal();
        return rideRepository.findByUserId(user.getId());
    }
}
