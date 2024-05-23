package com.yahiee.gsmmode;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface PhoneNumberDao {
    @Insert
    void insert(PhoneNumber phoneNumber);

    @Query("SELECT * FROM phone_number_table LIMIT 1")
    PhoneNumber getPhoneNumber();

    @Query("DELETE FROM phone_number_table")
    void clearPhoneNumbers();
}
