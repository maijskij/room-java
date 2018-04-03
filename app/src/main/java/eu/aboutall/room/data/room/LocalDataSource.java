package eu.aboutall.room.data.room;

import android.arch.persistence.room.Room;
import android.content.Context;

/**
 * Created by denis on 25/08/2017.
 */

public class LocalDataSource {

    public LocalDatabase db;

    private static final String DB_NAME = "database-name";

    private static LocalDataSource ds;

    public static LocalDataSource getInstance(Context context){
        if (ds == null){
            ds = new LocalDataSource(context);
        }

        return ds;
    }

    private LocalDataSource(Context context){
        db = Room.databaseBuilder(context.getApplicationContext(),
                LocalDatabase.class, DB_NAME).build();
    }

}

