package eu.aboutall.room.controller;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;


import eu.aboutall.room.R;
import eu.aboutall.room.db.DataSource;
import eu.aboutall.room.model.Item;


import java.util.ArrayList;
import java.util.List;

public class ItemsActivity extends AppCompatActivity implements ItemListEventsCallbacks, LoaderManager.LoaderCallbacks<List<Item>>{

    private static final int ITEMS_LOADER = 1;

    private ItemsListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewItem(new Item());
            }
        });

        RecyclerView recyclerView = findViewById(R.id.item_list);
        assert recyclerView != null;
        setupRecyclerView(recyclerView);

    }

    @Override
    protected void onResume() {
        super.onResume();

        restartLoader();
    }


    private void addNewItem(Item item) {

        ItemsDbService.addRecord(this, item );
        mAdapter.add(item);
    }


    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {

        mAdapter = new ItemsListAdapter(new ArrayList<Item>(), this);
        recyclerView.setAdapter(mAdapter);
    }

    private void restartLoader(){
        getSupportLoaderManager().restartLoader(ITEMS_LOADER, null, this);
    }



    // ItemListEventsCallbacks implementation

    @Override
    public void onDeleteItem(Item item, int position) {
        ItemsDbService.removeRecord(this, item );
        mAdapter.removeAtPosition(position);
    }

    @Override
    public void onEditItem(Item item, int position) {

        ItemsDbService.updateRecord(this, item );
        mAdapter.dataChanged(position, item);
    }


    // ItemsLoader

    @NonNull
    @Override
    public Loader<List<Item>> onCreateLoader(int id, Bundle args) {
        return new ItemsLoader(this);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Item>> loader, List<Item> data) {
        if (loader.getId() == ITEMS_LOADER) {
            mAdapter.setData(data);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Item>> loader) {
        mAdapter.clearData();
    }
}
