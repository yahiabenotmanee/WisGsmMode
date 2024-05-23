package com.yahiee.gsmmode;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {PhoneNumber.class}, version = 1)
public abstract class PhoneNumberDatabase extends RoomDatabase {
    private static PhoneNumberDatabase instance;

    public abstract PhoneNumberDao phoneNumberDao();

    public static synchronized PhoneNumberDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            PhoneNumberDatabase.class, "phone_number_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
