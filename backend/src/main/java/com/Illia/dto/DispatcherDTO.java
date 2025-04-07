package com.Illia.dto;

public class DispatcherDTO {
    private String email;
    private String uuid;
    public DispatcherDTO(){}
    public DispatcherDTO(String email, String uuid){
        this.email = email;
        this.uuid = uuid;
    }
    public void setUUID(String uuid){
        this.uuid = uuid;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public String getUUID(){
        return uuid;
    }
    public String getEmail(){
        return email;
    }
    public String toString(){
        return "email: " + email + " uuid: " + uuid;
    }
}
