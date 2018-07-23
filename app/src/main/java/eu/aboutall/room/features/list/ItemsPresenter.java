package eu.aboutall.room.features.list;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.google.common.collect.Lists;

import java.util.List;

import eu.aboutall.room.data.Item;
import eu.aboutall.room.data.source.Repository;
import eu.aboutall.room.utils.espresso.EspressoIdlingResource;
import eu.aboutall.room.utils.schedulers.BaseSchedulerProvider;
import io.reactivex.Completable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

import static com.google.common.base.Preconditions.checkNotNull;


public class ItemsPresenter implements ItemsContract.UserActionsListener {


    @NonNull
    private final Repository mRepository;

    @NonNull
    private final ItemsContract.View mView;

    @NonNull
    private final BaseSchedulerProvider mSchedulerProvider;

    @NonNull
    private CompositeDisposable mCompositeDisposable;


    public ItemsPresenter(@NonNull Repository itemsRepository,
                   @NonNull ItemsContract.View itemsView,
                   @NonNull BaseSchedulerProvider schedulerProvider){

        mRepository = checkNotNull(itemsRepository, "itemsRepository cannot be null");
        mView = checkNotNull(itemsView, "itemsView cannot be null!");
        mSchedulerProvider = checkNotNull(schedulerProvider, "schedulerProvider cannot be null");

        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void subscribe() {
        loadItems();
    }

    @Override
    public void unsubscribe() {
        mCompositeDisposable.clear();
    }

    @Override
    public void result(int requestCode, int resultCode) {
        // If a note was successfully added, show snackbar
       if (ItemsActivity.REQUEST_ADD_NOTE == requestCode && Activity.RESULT_OK == resultCode) {
            mView.showSuccessfullySavedMessage();
        }
    }

    @Override
    public void refreshItems() {
        loadItems();
    }

    @Override
    public void addNewItem() {
        mView.showAddItemView();
    }

    @Override
    public void deleteItem(Item item) {
        mView.deleteItemFromList( item );
        deleteItemFromDb( item );
    }

    @Override
    public void editItem(Item item) {
        mView.updateItemFromList( item );
        updateItemFromDb( item );
    }

    private void loadItems() {

        mView.setLoadingIndicator(true);

        // The network request might be handled in a different thread so make sure Espresso knows
        // that the app is busy until the response is handled.
        EspressoIdlingResource.increment(); // App is busy until further notice

        mCompositeDisposable.clear();
        Disposable disposable = mRepository
                .getAll()
                .firstOrError()
                .subscribeOn( mSchedulerProvider.io() )
                .observeOn( mSchedulerProvider.ui() )
                .doFinally(() -> {
                    if (!EspressoIdlingResource.getIdlingResource().isIdleNow()) {
                        EspressoIdlingResource.decrement(); // Set app as idle.
                    }
                })
                .subscribe(
                        // onNext
                        tasks -> {
                            processTasks(tasks);
                            mView.setLoadingIndicator(false);
                        },
                        // onError
                        throwable ->  mView.showLoadingTasksError() );

        mCompositeDisposable.add(disposable);
    }

    private void processTasks(@NonNull List<Item> items) {
        if (items.isEmpty()) {
            // Show a message indicating there are no tasks.
            mView.showNoTasks();
        } else {
            // Show the list of tasks
            mView.replaceListItems(Lists.reverse(items));
        }
    }

    private void deleteItemFromDb(Item item) {

        EspressoIdlingResource.increment(); // App is busy until further notice

        mCompositeDisposable.clear();
        Disposable disposable = Completable.fromAction(() -> mRepository.delete( item.getUuid() ))
                .subscribeOn( mSchedulerProvider.io() )
                .observeOn( mSchedulerProvider.ui() )
                .doFinally( () -> {
                    if (!EspressoIdlingResource.getIdlingResource().isIdleNow()) {
                        EspressoIdlingResource.decrement(); // Set app as idle.
                    }
                })
                .subscribe(
                        // onSuccess
                        () ->  {} , // do nothing
                        // onError
                        throwable -> mView.showDbError() );

        mCompositeDisposable.add(disposable);
    }

    private void updateItemFromDb(Item item) {

        EspressoIdlingResource.increment(); // App is busy until further notice

        mCompositeDisposable.clear();
        Disposable disposable = Completable.fromAction(() -> mRepository.updateItem( item ))
                .subscribeOn( mSchedulerProvider.io() )
                .observeOn( mSchedulerProvider.ui() )
                .doFinally( () -> {
                    if (!EspressoIdlingResource.getIdlingResource().isIdleNow()) {
                        EspressoIdlingResource.decrement(); // Set app as idle.
                    }
                })
                .subscribe(
                        // onSuccess
                        () ->  {} , // do nothing
                        // onError
                        throwable -> mView.showDbError() );

        mCompositeDisposable.add(disposable);
    }

}
