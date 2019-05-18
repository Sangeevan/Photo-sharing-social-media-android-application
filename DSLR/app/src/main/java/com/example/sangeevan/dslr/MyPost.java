package com.example.sangeevan.dslr;

import com.google.firebase.database.Exclude;

public class MyPost {

    public String placename;
    public String placedescription;
    public String placeimageurl;

    private String mkey;

    public MyPost() {
    }

    public MyPost(String placename, String placedescription, String placeimageurl) {
        this.placename = placename;
        this.placedescription = placedescription;
        this.placeimageurl = placeimageurl;
    }

    public String getPlacename() {
        return placename;
    }

    public void setPlacename(String placename) {
        this.placename = placename;
    }

    public String getPlacedescription() {
        return placedescription;
    }

    public void setPlacedescription(String placedescription) {
        this.placedescription = placedescription;
    }

    public String getPlaceimageurl() {
        return placeimageurl;
    }

    public void setPlaceimageurl(String placeimageurl) {
        this.placeimageurl = placeimageurl;
    }


    @Exclude
    public String getMkey() {
        return mkey;
    }

    @Exclude
    public void setMkey(String key) {
        this.mkey = key;
    }
}
