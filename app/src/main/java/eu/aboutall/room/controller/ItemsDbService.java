package eu.aboutall.room.controller;

/**
 * Created by denis on 25/08/2017.
 */

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import eu.aboutall.room.db.DataSource;
import eu.aboutall.room.model.Item;

public class ItemsDbService extends IntentService {


    public static final String ACTION_UPDATE_ITEM   = "eu.aboutall.daojava.controller.ACTION_UPDATE_ITEM";
    public static final String ACTION_REMOVE_ITEM   = "eu.aboutall.daojava.controller.ACTION_REMOVE_ITEM";
    public static final String ACTION_ADD_ITEM      = "eu.aboutall.daojava.controller.ACTION_ADD_ITEM";

    public static final String EXTRA_ITEM           = "eu.aboutall.daojava.controller.EXTRA_ITEM";

    public static void addRecord(Context context, Item item){

        Intent intent = new Intent(context, ItemsDbService.class);
        intent.setAction(ACTION_ADD_ITEM);
        intent.putExtra(EXTRA_ITEM, item);
        context.startService(intent);
    }

    public static void updateRecord(Context context, Item item){

        Intent intent = new Intent(context, ItemsDbService.class);
        intent.setAction(ACTION_UPDATE_ITEM);
        intent.putExtra(EXTRA_ITEM, item);
        context.startService(intent);
    }


    public static void removeRecord(Context context, Item item) {

        Intent intent = new Intent(context, ItemsDbService.class);
        intent.setAction(ACTION_REMOVE_ITEM);
        intent.putExtra(EXTRA_ITEM, item);
        context.startService(intent);
    }

    public ItemsDbService() {
        super("ItemsDbService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {

            final String action = intent.getAction();
            final Item item = intent.getParcelableExtra(EXTRA_ITEM);

            if (ACTION_UPDATE_ITEM.equals(action)) {
                updateExistingRecord(item);
            } else if (ACTION_REMOVE_ITEM.equals(action)) {
                removeExistingRecord(item);
            } else if (ACTION_ADD_ITEM.equals(action)) {
                addRecord(item);
            }
        }
    }


    private void updateExistingRecord( Item item ) {
        DataSource.getInstance( getApplicationContext() ).db.itemsDao().updateItem(item);
    }

    private void removeExistingRecord( Item item ) {
        DataSource.getInstance( getApplicationContext() ).db.itemsDao().delete(item.getUuid());
    }

    private void addRecord( Item item  ) {
        long count = DataSource.getInstance( getApplicationContext() ).db.itemsDao().insert( item );
    }

}

