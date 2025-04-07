package com.Illia.dto;

public class ProfileDTO {
    private String email;
    private String address;
    private String phone;
    private int birthDay;
    private int birthMonth;
    private int birthYear;
    private String firstName;
    private String lastName;
    public ProfileDTO(){};
    public ProfileDTO(String email, String address, String phone, int birthDay, int birthMonth, int birthYear, String firstName, String lastName) {
        this.email = email;
        this.address = address;
        this.phone = phone;
        this.birthDay = birthDay;
        this.birthMonth = birthMonth;
        this.birthYear = birthYear;
        this.firstName = firstName;
        this.lastName = lastName;
    }
    public void setEmail(String email) {this.email = email;}
    public void setAddress(String address) { this.address = address;}
    public void setPhone(String phone) {this.phone = phone;}
    public void setBirthDay(int birthDay) {this.birthDay = birthDay;}
    public void setBirthMonth(int birthMonth) {this.birthMonth = birthMonth;}
    public void setBirthYear(int birthYear) { this.birthYear = birthYear; }
    public void setFirstName(String firstName) {this.firstName = firstName;}
    public void setLastName(String lastName) {this.lastName = lastName;}
    public String getEmail() { return email; }
    public String getAddress() { return address; }
    public String getPhone() { return phone; }
    public int getBirthDay() { return birthDay; }
    public int getBirthMonth() { return birthMonth; }
    public int getBirthYear() { return birthYear; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public void setField(String fieldName, String value) {
        switch (fieldName.toLowerCase()) {
            case "email":
                setEmail(value);
                break;
            case "address":
                setAddress(value);
                break;
            case "phone":
                setPhone(value);
                break;
            case "birthmonth":
                setBirthMonth(Integer.parseInt(value));
                break;
            case "birthday":
                setBirthDay(Integer.parseInt(value));
                break;
            case "day":
                setBirthDay(Integer.parseInt(value));
                break;
            case "month":
                setBirthMonth(Integer.parseInt(value));
                break;
            case "birthyear":
                setBirthYear(Integer.parseInt(value));
                break;
            case "year":
                setBirthYear(Integer.parseInt(value));
                break;
            case "firstname":
                setFirstName(value);
                break;
            case "lastname":
                setLastName(value);
                break;
            default:
                System.out.println("Unknown field: " + fieldName);
                break;
        }
    }
    @Override
    public String toString() {
    return "User{" +
            "email='" + email + '\'' +
            ", address='" + address + '\'' +
            ", phone='" + phone + '\'' +
            ", birthDay=" + birthDay +
            ", birthMonth=" + birthMonth +
            ", birthYear=" + birthYear +
            ", firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            '}';
}
public boolean isAnyFieldEmpty() {
    return email == null || email.isEmpty()
        || address == null || address.isEmpty()
        || phone == null || phone.isEmpty()
        || firstName == null || firstName.isEmpty()
        || lastName == null || lastName.isEmpty()
        || birthDay == 0
        || birthMonth == 0
        || birthYear == 0;
}
}
