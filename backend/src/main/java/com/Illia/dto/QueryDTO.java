package com.Illia.dto;

import java.time.LocalDateTime;

public class QueryDTO {
    private int id;
    private LocalDateTime addDatetime;
    private LocalDateTime startDatetime;
    private String email;
    private String startLocation;
    private String destination;
    private int driverExperience;
    private String carType;
    private int maxCarMileage;
    private String status;
    public QueryDTO(){};
    // Constructor that accepts all parameters
    public QueryDTO(int id, LocalDateTime addDatetime, LocalDateTime startDatetime, String email,
                    String startLocation, String destination, int driverExperience,
                    String carType, int maxCarMileage, String status) {
        this.id = id;
        this.addDatetime = addDatetime;
        this.startDatetime = startDatetime;
        this.email = email;
        this.startLocation = startLocation;
        this.destination = destination;
        this.driverExperience = driverExperience;
        this.carType = carType;
        this.maxCarMileage = maxCarMileage;
        this.status = status;
    }

    // Getters and Setters (if needed)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getAddDatetime() {
        return addDatetime;
    }

    public void setAddDatetime(LocalDateTime addDatetime) {
        this.addDatetime = addDatetime;
    }

    public LocalDateTime getStartDatetime() {
        return startDatetime;
    }

    public void setStartDatetime(LocalDateTime startDatetime) {
        this.startDatetime = startDatetime;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public int getDriverExperience() {
        return driverExperience;
    }

    public void setDriverExperience(int driverExperience) {
        this.driverExperience = driverExperience;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public int getMaxCarMileage() {
        return maxCarMileage;
    }

    public void setMaxCarMileage(int maxCarMileage) {
        this.maxCarMileage = maxCarMileage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // toString Method
    @Override
    public String toString() {
        return "QueryDTO{" +
                "id=" + id +
                ", addDatetime=" + addDatetime +
                ", startDatetime=" + startDatetime +
                ", email='" + email + '\'' +
                ", startLocation='" + startLocation + '\'' +
                ", destination='" + destination + '\'' +
                ", driverExperience=" + driverExperience +
                ", carType='" + carType + '\'' +
                ", maxCarMileage=" + maxCarMileage +
                ", status=" + status +
                '}';
    }
}
