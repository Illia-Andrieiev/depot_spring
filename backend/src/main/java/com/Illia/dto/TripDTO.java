package com.Illia.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class TripDTO {
    private int id;
    private int queryId;
    private UUID dispatcherId;
    private int driverId;
    private String email;
    private String startLocation;
    private String destination;
    private LocalDateTime startDatetime;
    private LocalDateTime endDatetime;
    private Boolean endCarServiceability;
    private Integer mileage;

    public TripDTO() {}

    public TripDTO(int id, int queryId, UUID dispatcherId, int driverId, String startLocation,
                   String destination, LocalDateTime startDatetime, LocalDateTime endDatetime,
                   Boolean endCarServiceability, Integer mileage,String email) {
        this.id = id;
        this.queryId = queryId;
        this.dispatcherId = dispatcherId;
        this.driverId = driverId;
        this.startLocation = startLocation;
        this.destination = destination;
        this.startDatetime = startDatetime;
        this.endDatetime = endDatetime;
        this.endCarServiceability = endCarServiceability;
        this.mileage = mileage;
        this.email = email;
    }

    // Getters and Setters
    public String getEmail(){
        return email;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQueryId() {
        return queryId;
    }

    public void setQueryId(int queryId) {
        this.queryId = queryId;
    }

    public UUID getDispatcherId() {
        return dispatcherId;
    }

    public void setDispatcherId(UUID dispatcherId) {
        this.dispatcherId = dispatcherId;
    }

    public int getDriverId() {
        return driverId;
    }

    public void setDriverId(int driverId) {
        this.driverId = driverId;
    }

    public String getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(String startLocation) {
        this.startLocation = startLocation;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public LocalDateTime getStartDatetime() {
        return startDatetime;
    }

    public void setStartDatetime(LocalDateTime startDatetime) {
        this.startDatetime = startDatetime;
    }

    public LocalDateTime getEndDatetime() {
        return endDatetime;
    }

    public void setEndDatetime(LocalDateTime endDatetime) {
        this.endDatetime = endDatetime;
    }

    public Boolean getEndCarServiceability() {
        return endCarServiceability;
    }

    public void setEndCarServiceability(Boolean endCarServiceability) {
        this.endCarServiceability = endCarServiceability;
    }

    public Integer getMileage() {
        return mileage;
    }

    public void setMileage(Integer mileage) {
        this.mileage = mileage;
    }

    // Dynamic field setter
    public void setField(String fieldName, Object value) {
        switch (fieldName) {
            case "id" -> this.id = (int) value;
            case "queryId" -> this.queryId = (int) value;
            case "dispatcherId" -> this.dispatcherId = (UUID) value;
            case "driverId" -> this.driverId = (int) value;
            case "startLocation" -> this.startLocation = (String) value;
            case "email" -> this.email = (String) value;
            case "destination" -> this.destination = (String) value;
            case "startDatetime" -> this.startDatetime = (LocalDateTime) value;
            case "endDatetime" -> this.endDatetime = (LocalDateTime) value;
            case "endCarServiceability" -> this.endCarServiceability = (Boolean) value;
            case "mileage" -> this.mileage = (Integer) value;
            default -> throw new IllegalArgumentException("Unknown field: " + fieldName);
        }
    }

    // toString
    @Override
    public String toString() {
        return "TripDTO{" +
                "id=" + id +
                ", queryId=" + queryId +
                ", dispatcherId=" + dispatcherId +
                ", driverId=" + driverId +
                ", startLocation='" + startLocation + '\'' +
                ", destination='" + destination + '\'' +
                ", startDatetime=" + startDatetime +
                ", endDatetime=" + endDatetime +
                ", endCarServiceability=" + endCarServiceability +
                ", mileage=" + mileage +
                '}';
    }
}
