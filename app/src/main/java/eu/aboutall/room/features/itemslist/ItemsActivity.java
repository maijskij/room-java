package eu.aboutall.room.features.itemslist;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;


import eu.aboutall.room.R;
import eu.aboutall.room.data.room.DataSource;
import eu.aboutall.room.data.Item;


import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class ItemsActivity extends AppCompatActivity implements ItemListEventsCallbacks, ItemsContract.View {


    private ItemsListAdapter mAdapter;

    private ItemsPresenter mPresenter;

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

                mPresenter.addNewItem();
            }
        });

        RecyclerView recyclerView = findViewById(R.id.item_list);
        assert recyclerView != null;
        setupRecyclerView(recyclerView);

        // Create the presenter
        mPresenter = new ItemsPresenter(
                DataSource.getInstance(this.getApplicationContext()).db.itemsDao(),
                this);
    }

    @Override
    public void addNewItem(Item item) {
        mAdapter.add(item);
    }

    @Override
    public void updateItem(Item item, int position) {
        mAdapter.dataChanged(position, item);
    }

    @Override
    public void deleteItem(int position) {
        mAdapter.removeAtPosition(position);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.subscribe();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.unsubscribe();
    }

    @Override
    public void setPresenter(@NonNull ItemsContract.Presenter presenter) {
       // mPresenter = checkNotNull(presenter);
    }


    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {

        mAdapter = new ItemsListAdapter(new ArrayList<Item>(), this);
        recyclerView.setAdapter(mAdapter);
    }

    // ItemListEventsCallbacks implementation

    @Override
    public void onDeleteItem(Item item, int position) {
        mPresenter.deleteItem(item, position);
    }

    @Override
    public void onEditItem(Item item, int position) {
        mPresenter.updateItem(item, position);
    }

    @Override
    public void setLoadingIndicator(boolean active) {

    }

    @Override
    public void showItems(List<Item> items) {
        mAdapter.setData( items );
    }

    @Override
    public void showLoadingTasksError() {

    }

    @Override
    public void showNoTasks() {
        mAdapter.clearData();
    }
}
