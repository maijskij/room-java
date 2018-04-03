package eu.aboutall.room.data.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import eu.aboutall.room.data.Item;

/**
 * Created by denis on 25/08/2017.
 */

@Database(entities = {Item.class}, version = 1)
public abstract class LocalDatabase extends RoomDatabase {
    public abstract DaoApi getDaoApi();
}
