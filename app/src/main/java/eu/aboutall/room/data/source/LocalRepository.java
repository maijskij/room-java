package eu.aboutall.room.data.source;

import android.arch.persistence.room.Room;
import android.content.Context;

import eu.aboutall.room.data.source.room.LocalDatabase;


/**
 * Created by denis on 25/08/2017.
 */

public class LocalRepository {

    private static final String DB_NAME = "database-name";

    private static LocalRepository ds;

    private LocalDatabase db;

    public static LocalRepository getInstance(Context context ){
        if (ds == null){
            ds = new LocalRepository(context);
        }

        return ds;
    }

    public Repository getRepository(){

       return db.getDaoApi();
    }

    private LocalRepository(Context context){
        db = Room.databaseBuilder(context.getApplicationContext(), LocalDatabase.class, DB_NAME).build();
    }

}

