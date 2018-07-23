package eu.aboutall.room.features.list;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.test.espresso.IdlingResource;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import eu.aboutall.room.Injection;
import eu.aboutall.room.R;
import eu.aboutall.room.data.Item;
import eu.aboutall.room.features.add.AddItemActivity;
import eu.aboutall.room.utils.espresso.EspressoIdlingResource;

public class ItemsActivity extends AppCompatActivity implements  ItemsContract.View {

    protected static final int REQUEST_ADD_NOTE = 1;

    private ItemsAdapter mAdapter;

    private ItemsContract.UserActionsListener mPresenter;

    private View mNoTasksView;

    private RecyclerView mListView;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mNoTasksView = findViewById(R.id.noTasks);

        mListView = findViewById(R.id.item_list);

        mSwipeRefreshLayout = findViewById(R.id.refresh_layout);

        setupActionBar();

        setupRecyclerView();

        setupFloatingActionBar();

        setupSwipeRefreshLayout();

        setupActionBar();

        // Create the presenter
        mPresenter = new ItemsPresenter(
                Injection.provideItemsRepository(getApplicationContext() ),
                this,
                Injection.provideSchedulerProvider() );

    }

    private ItemsAdapter.Events adapterEvents = new ItemsAdapter.Events() {
        @Override
        public void onItemLongClick(Item item) {
            mPresenter.deleteItem( item );
        }

        @Override
        public void onItemClick(Item item) {
            mPresenter.editItem( item  );
        }
    };

    private void setupRecyclerView() {

        assert mListView != null;

        mAdapter = new ItemsAdapter(new ArrayList<>(), adapterEvents);
        mListView.setAdapter(mAdapter);
        mListView.setLayoutManager( new LinearLayoutManager(this) );
    }

    private void setupSwipeRefreshLayout() {

        mSwipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(this, R.color.colorPrimary),
                ContextCompat.getColor(this, R.color.colorAccent),
                ContextCompat.getColor(this, R.color.colorPrimaryDark));

        mSwipeRefreshLayout.setOnRefreshListener( () -> mPresenter.refreshItems() );
    }

    private void setupFloatingActionBar() {
        FloatingActionButton fab = findViewById(R.id.fab_new);
        fab.setOnClickListener( view -> mPresenter.addNewItem() );
    }

    private void setupActionBar(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
    public void setLoadingIndicator(boolean active) {

        // Make sure setRefreshing() is called after the layout is done with everything else.
        mSwipeRefreshLayout.post(() -> mSwipeRefreshLayout.setRefreshing(active));
    }

    @Override
    public void replaceListItems(List<Item> items) {
        mAdapter.replaceData( items );

        mListView.setVisibility(View.VISIBLE);
        mNoTasksView.setVisibility(View.GONE);
    }

    @Override
    public void showLoadingTasksError() {
        showMessage(getString(R.string.loading_tasks_error));
    }

    @Override
    public void showNoTasks() {
        mListView.setVisibility(View.GONE);
        mNoTasksView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showAddItemView() {
        Intent intent = new Intent(this, AddItemActivity.class);
        startActivityForResult(intent, REQUEST_ADD_NOTE);
    }

    @Override
    public void showSuccessfullySavedMessage() {
        showMessage(getString(R.string.successfully_saved_task_message));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mPresenter.result(requestCode, resultCode);
    }

    @Override
    public void showMessage(String message) {
        Snackbar.make(mListView, getString(R.string.successfully_saved_item_message),
                Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void updateItemFromList(Item item) {
        mAdapter.updateItem( item );
    }

    @Override
    public void deleteItemFromList(Item item) {
        mAdapter.removeItem(item );
    }

    // Espresso

    @VisibleForTesting
    public IdlingResource getCountingIdlingResource() {
        return EspressoIdlingResource.getIdlingResource();
    }

    @Override
    public void showDbError() {
        showMessage( getString(R.string.db_error) );
    }


}
