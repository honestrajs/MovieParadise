package com.example.movieparadise;

public class helper {
    public helper() {
    }

    String Name,Phone,Password,profile_url,age;

    public helper(String name,String phone,String pass,String prof_url,String uage){
        this.Name=name;
        this.Phone=phone;
        this.Password=pass;
        this.profile_url=prof_url;
        this.age=uage;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getProfile_url() {
        return profile_url;
    }

    public void setProfile_url(String profile_url) {
        this.profile_url = profile_url;
    }
}
