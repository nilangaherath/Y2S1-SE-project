package com.example.busreservation.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

/** Request payload for creating or updating a schedule. */
public class ScheduleRequest {
    private Integer busId;
    private Integer routeId;
    private LocalDate travelDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private BigDecimal fare;

    public Integer getBusId() {
        return busId;
    }

    public void setBusId(Integer busId) {
        this.busId = busId;
    }

    public Integer getRouteId() {
        return routeId;
    }

    public void setRouteId(Integer routeId) {
        this.routeId = routeId;
    }

    public LocalDate getTravelDate() {
        return travelDate;
    }

    public void setTravelDate(LocalDate travelDate) {
        this.travelDate = travelDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public BigDecimal getFare() {
        return fare;
    }

    public void setFare(BigDecimal fare) {
        this.fare = fare;
    }
}
