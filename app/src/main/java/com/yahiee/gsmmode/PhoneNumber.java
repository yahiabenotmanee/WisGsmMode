package com.yahiee.gsmmode;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "phone_number_table")
public class PhoneNumber {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String phoneNumber;

    // Constructor, getters, and setters
    public PhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
