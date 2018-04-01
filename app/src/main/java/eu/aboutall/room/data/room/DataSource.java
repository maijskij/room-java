package eu.aboutall.room.data.room;

import android.arch.persistence.room.Room;
import android.content.Context;

/**
 * Created by denis on 25/08/2017.
 */

public class DataSource {

    public AppDatabase db;

    private static final String DB_NAME = "database-name";

    private static DataSource ds;

    public static DataSource getInstance(Context context){
        if (ds == null){
            ds = new DataSource(context);
        }

        return ds;
    }

    private DataSource(Context context){
        db = Room.databaseBuilder(context.getApplicationContext(),
                AppDatabase.class, DB_NAME).build();

    }

}

