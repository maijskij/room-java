package eu.aboutall.room.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Random;
import java.util.UUID;

/**
 * Created by denis on 25/08/2017.
 */

@Entity(tableName = "items")
public class Item implements Parcelable {

    private static final String DEFAULT_CAPTION = "New item ";

    @PrimaryKey
    private String uuid;
    private String name;

    @Ignore
    public Item() {

        this.uuid = UUID.randomUUID().toString();
        setRandomName();
    }

    public Item(String uuid, String name) {

        this.uuid = uuid;
        this.name = name;
    }

    public String getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRandomName(){
        Random rand = new Random();
        this.name = DEFAULT_CAPTION + rand.nextInt(100);
    }

    // Parcelable implementation

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(uuid);
        parcel.writeString(name);
    }

    protected Item(Parcel in) {
        uuid = in.readString();
        name = in.readString();
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

}
