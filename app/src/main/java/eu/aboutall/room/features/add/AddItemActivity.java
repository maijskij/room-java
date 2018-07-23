package eu.aboutall.room.features.add;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import eu.aboutall.room.Injection;
import eu.aboutall.room.R;
import eu.aboutall.room.data.Item;

public class AddItemActivity extends AppCompatActivity implements  AddItemContract.View {


    private AddItemContract.Presenter mPresenter;

    private TextView mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab_add_item_done);
        fab.setOnClickListener(view -> mPresenter.saveItem(mTitle.getText().toString()));

        mTitle = findViewById(R.id.name);

        mTitle.setText( Item.getRandomName() );


        // Create the presenter
        mPresenter = new AddItemPresenter(
                Injection.provideItemsRepository(getApplicationContext()),
                this,
                Injection.provideSchedulerProvider());

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
    public void showItemIsEmpty() {
        showToast( getString(R.string.empty_task_message) );
    }

    @Override
    public void showItemsList() {
        setResult(Activity.RESULT_OK);
        finish();
    }

    @Override
    public void setName(String name) {
        mTitle.setText(name);
    }

    @Override
    public void showDbError() {
        showToast( getString(R.string.db_error) );
    }


    private void showToast(String message){
        Snackbar.make(mTitle, message, Snackbar.LENGTH_LONG).show();
    }


}