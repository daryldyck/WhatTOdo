package com.denite.whattodo.models;

import java.io.Serializable;
import java.util.List;

public class Attraction implements Serializable
{
    private int id;
    private String name;
    private String address;
    private List<String> photosList;
    private long phone;
    private String website;
    private String description;
    private String price;
    private int rating;
    private boolean wishList;

    public Attraction()
    {
    }

    public Attraction(int id, String name, String address, List<String> photosList, long phone, String website, String description, String price)
    {
        this.id = id;
        this.name = name;
        this.address = address;
        this.photosList = photosList;
        this.phone = phone;
        this.website = website;
        this.description = description;
        this.price = price;
        this.rating = 0;
        this.wishList = false;
    }

    public Attraction(int id, String name, String address, List<String> photosList, long phone, String website, String description, String price, int rating, boolean wishList)
    {
        this.id = id;
        this.name = name;
        this.address = address;
        this.photosList = photosList;
        this.phone = phone;
        this.website = website;
        this.description = description;
        this.price = price;
        this.rating = rating;
        this.wishList = wishList;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public List<String> getPhotosList()
    {
        return photosList;
    }

    public void setPhotosList(List<String> photosList)
    {
        this.photosList = photosList;
    }

    public long getPhone()
    {
        return phone;
    }

    public void setPhone(long phone)
    {
        this.phone = phone;
    }

    public String getWebsite()
    {
        return website;
    }

    public void setWebsite(String website)
    {
        this.website = website;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getPrice()
    {
        return price;
    }

    public void setPrice(String price)
    {
        this.price = price;
    }

    public int getRating()
    {
        return rating;
    }

    public void setRating(int rating)
    {
        this.rating = rating;
    }

    public boolean isWishList()
    {
        return wishList;
    }

    public void setWishList(boolean wishList)
    {
        this.wishList = wishList;
    }
}
