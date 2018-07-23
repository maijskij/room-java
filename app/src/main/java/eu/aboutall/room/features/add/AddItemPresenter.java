package eu.aboutall.room.features.add;

import android.support.annotation.NonNull;

import eu.aboutall.room.data.Item;
import eu.aboutall.room.data.source.Repository;
import eu.aboutall.room.utils.espresso.EspressoIdlingResource;
import eu.aboutall.room.utils.schedulers.BaseSchedulerProvider;
import io.reactivex.Completable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

import static com.google.common.base.Preconditions.checkNotNull;

public class AddItemPresenter implements AddItemContract.Presenter {

    @NonNull
    private final Repository mRepository;

    @NonNull
    private final AddItemContract.View mView;

    @NonNull
    private final BaseSchedulerProvider mSchedulerProvider;

    @NonNull
    private CompositeDisposable mCompositeDisposable;



    public AddItemPresenter(Repository repository, AddItemContract.View addItemActivity, BaseSchedulerProvider schedulerProvider) {

        mRepository = checkNotNull(repository);

        mView = checkNotNull(addItemActivity);

        mSchedulerProvider = checkNotNull(schedulerProvider, "schedulerProvider cannot be null!");

        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {
        mCompositeDisposable.clear();
    }

    @Override
    public void saveItem(String name) {

        if (name.isEmpty()) {
            mView.showItemIsEmpty();

        } else {

            Item item = new Item( );
            item.setName(name);

            EspressoIdlingResource.increment(); // App is busy until further notice

            mCompositeDisposable.clear();
            Disposable disposable = Completable.fromAction(() -> mRepository.insert(item))
                    .subscribeOn( mSchedulerProvider.io() )
                    .observeOn( mSchedulerProvider.ui() )
                    .doFinally( () -> {
                        if (!EspressoIdlingResource.getIdlingResource().isIdleNow()) {
                            EspressoIdlingResource.decrement(); // Set app as idle.
                        }
                    })
                    .subscribe(
                            // onSuccess
                            () ->  mView.showItemsList(),
                            // onError
                            throwable -> mView.showDbError() );

            mCompositeDisposable.add(disposable);

        }
    }
}
