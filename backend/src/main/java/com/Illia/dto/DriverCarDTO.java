package com.Illia.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.lang.reflect.Field;

public class DriverCarDTO {
    // from driver
    private int id;

    private String email;

    private int age;

    private int experience;

    @JsonProperty("driver_licence_type")
    private String driverLicenceType;

    @JsonProperty("additional_information")
    private String additionalInformation;

    // from car
    private String vin;

    private String brand;

    @JsonProperty("year_of_manufacture")
    private int yearOfManufacture;

    @JsonProperty("car_type")
    private String type;

    @JsonProperty("fuel_type")
    private String fuelType;

    private int mileage;

    private String number;

    @JsonProperty("is_serviceable")
    private boolean isServiceable;

    public DriverCarDTO() {}

    // Getters и Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public int getExperience() { return experience; }
    public void setExperience(int experience) { this.experience = experience; }

    public String getDriverLicenceType() { return driverLicenceType; }
    public void setDriverLicenceType(String driverLicenceType) { this.driverLicenceType = driverLicenceType; }

    public String getAdditionalInformation() { return additionalInformation; }
    public void setAdditionalInformation(String additionalInformation) { this.additionalInformation = additionalInformation; }

    public String getVin() { return vin; }
    public void setVin(String vin) { this.vin = vin; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public int getYearOfManufacture() { return yearOfManufacture; }
    public void setYearOfManufacture(int yearOfManufacture) { this.yearOfManufacture = yearOfManufacture; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getFuelType() { return fuelType; }
    public void setFuelType(String fuelType) { this.fuelType = fuelType; }

    public int getMileage() { return mileage; }
    public void setMileage(int mileage) { this.mileage = mileage; }

    public String getNumber() { return number; }
    public void setNumber(String number) { this.number = number; }

    public boolean isServiceable() { return isServiceable; }
    public void setServiceable(boolean serviceable) { isServiceable = serviceable; }

    @Override
    public String toString() {
        return "DriverCarDTO{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", age=" + age +
                ", experience=" + experience +
                ", driverLicenceType='" + driverLicenceType + '\'' +
                ", additionalInformation='" + additionalInformation + '\'' +
                ", vin='" + vin + '\'' +
                ", brand='" + brand + '\'' +
                ", yearOfManufacture=" + yearOfManufacture +
                ", type='" + type + '\'' +
                ", fuelType='" + fuelType + '\'' +
                ", mileage=" + mileage +
                ", number='" + number + '\'' +
                ", isServiceable=" + isServiceable +
                '}';
    }

    public void setField(String fieldName, Object value) {
        try {
            Field field = this.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(this, value);
        } catch (NoSuchFieldException e) {
            System.out.println("No such field: " + fieldName);
        } catch (IllegalAccessException e) {
            System.out.println("Cannot access field: " + fieldName);
        } catch (Exception e) {
            System.out.println("Error setting field: " + fieldName + ", with value: " + value);
            e.printStackTrace();
        }
    }
}
