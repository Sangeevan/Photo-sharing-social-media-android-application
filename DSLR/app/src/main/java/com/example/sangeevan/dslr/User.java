package com.example.sangeevan.dslr;

public class User {

    public String name;
    public String mailid;
    public String imageurl;

    public User(){
    }

    public User(String name,String mailid,String imageurl){
        this.name=name;
        this.mailid=mailid;
        this.imageurl=imageurl;
    }

    public String getName() {
        return name;
    }

    public String getMailid() {
        return mailid;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMailid(String mailid) {
        this.mailid = mailid;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

}
