package com.bookstore.dto;


public class UpdateProfileDetailsRequest {


private String fullName;
private String phone;
private String address;
private String city;
private String country;


public String getFullName() {
return fullName;
}


public String getPhone() {
return phone;
}


public String getAddress() {
return address;
}


public String getCity() {
return city;
}


public String getCountry() {
return country;
}


public void setFullName(String fullName) {
this.fullName = fullName;
}


public void setPhone(String phone) {
this.phone = phone;
}


public void setAddress(String address) {
this.address = address;
}


public void setCity(String city) {
this.city = city;
}


public void setCountry(String country) {
this.country = country;
}
}