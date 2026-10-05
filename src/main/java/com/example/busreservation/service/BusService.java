package com.example.busreservation.service;

import com.example.busreservation.model.Bus;
import com.example.busreservation.repository.BusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BusService {

    @Autowired
    private BusRepository busRepository;

    public List<Bus> listAllBuses() {
        return busRepository.findAll();
    }

    public Bus createBus(Bus bus) {
        bus.setRegistrationNo(normalizeRegistrationNumber(bus.getRegistrationNo()));
        validateSeatCount(bus.getNoOfSeats());
        return busRepository.save(bus);
    }

    public Optional<Bus> updateBus(Integer id, Bus updates) {
        return busRepository.findById(id).map(existing -> {
            if (updates.getRegistrationNo() != null) existing.setRegistrationNo(normalizeRegistrationNumber(updates.getRegistrationNo()));
            if (updates.getNoOfSeats() != null) {
                validateSeatCount(updates.getNoOfSeats());
                existing.setNoOfSeats(updates.getNoOfSeats());
            }
            if (updates.getType() != null) existing.setType(updates.getType());
            return busRepository.save(existing);
        });
    }

    public boolean deleteBus(Integer id) {
        try {
            return busRepository.findById(id).map(bus -> {
                busRepository.delete(bus);
                busRepository.flush();
                return true;
            }).orElse(false);
        } catch (DataIntegrityViolationException e) {
            return false;
        }
    }

    public Optional<Bus> findById(Integer id) {
        return busRepository.findById(id);
    }

    private String normalizeRegistrationNumber(String registrationNumber) {
        if (registrationNumber == null) {
            throw new IllegalArgumentException("Registration number is required");
        }

        String normalized = registrationNumber.trim().toUpperCase();
        boolean isCurrentFormat = normalized.matches("^[NGHJ][A-Z]-\\d{4}$");
        boolean isOlderFormat = normalized.matches("^\\d{2}-\\d{4}$");
        if (!isCurrentFormat && !isOlderFormat) {
            throw new IllegalArgumentException(
                "Registration number must use NB-1234 (starting with N, G, H, or J) or the older format 23-1234");
        }
        return normalized;
    }

    private void validateSeatCount(Integer seatCount) {
        if (seatCount == null || seatCount < 1 || seatCount > 100) {
            throw new IllegalArgumentException("Number of seats must be a whole number from 1 to 100");
        }
    }
}

