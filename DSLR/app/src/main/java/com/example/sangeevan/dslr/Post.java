package com.example.sangeevan.dslr;

public class Post {

    public String profilename;
    public String profileimageurl;
    public String placename;
    public String placedescription;
    public String placeimageurl;

    public Post() {
    }

    public Post(String profilename, String profileimageurl, String placename, String placedescription, String placeimageurl) {
        this.profilename = profilename;
        this.profileimageurl = profileimageurl;
        this.placename = placename;
        this.placedescription = placedescription;
        this.placeimageurl = placeimageurl;
    }

    public String getProfilename() {
        return profilename;
    }

    public void setProfilename(String profilename) {
        this.profilename = profilename;
    }

    public String getProfileimageurl() {
        return profileimageurl;
    }

    public void setProfileimageurl(String profileimageurl) {
        this.profileimageurl = profileimageurl;
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
}
