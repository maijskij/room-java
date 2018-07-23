package eu.aboutall.room.add;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import eu.aboutall.room.data.Item;
import eu.aboutall.room.data.source.Repository;
import eu.aboutall.room.features.add.AddItemContract;
import eu.aboutall.room.features.add.AddItemPresenter;
import eu.aboutall.room.utils.schedulers.BaseSchedulerProvider;
import eu.aboutall.room.utils.schedulers.ImmediateSchedulerProvider;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AddItemPresenterTest {

    @Mock
    private Repository mRepository;

    @Mock
    private AddItemContract.View mView;

    private BaseSchedulerProvider mSchedulerProvider;

    private AddItemPresenter mPresenter;

    @Before
    public void setupMocksAndView() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

        mSchedulerProvider = new ImmediateSchedulerProvider();
    }

    @Test
    public void saveNewItemToRepository_showsSuccessMessageUi() {
        // Get a reference to the class under test
        mPresenter = new AddItemPresenter(
                mRepository, mView, mSchedulerProvider);

        // When the presenter is asked to save a item
        mPresenter.saveItem( "New item");

        // Then a item is saved in the repository and the view updated
        verify(mRepository).insert(any(Item.class)); // saved to the model
        verify(mView).showItemsList(); // shown in the UI
    }

    @Test
    public void saveItem_emptyTaskShowsErrorUi() {
        // Get a reference to the class under test
        mPresenter = new AddItemPresenter(
                mRepository, mView, mSchedulerProvider);

        // When the presenter is asked to save an empty item
        mPresenter.saveItem("");

        // Then an empty not error is shown in the UI
        verify(mView).showItemIsEmpty();
    }

    @Test
    public void populateItem_callsRepoAndUpdatesViewOnError() {

        // Get a reference to the class under test
        mPresenter = new AddItemPresenter(
                mRepository, mView, mSchedulerProvider);

        // when mRepository.insert(0 is called, generate exception
        when(mRepository.insert(any(Item.class))).thenThrow(NullPointerException.class);

        // When the presenter is asked to save a item
        mPresenter.saveItem( "New item 2");

        // Then a item is saved in the repository and the view updated
        verify(mRepository).insert(any(Item.class)); // attempt to saved to the model
        verify(mView).showDbError(); // shown error in the UI

        verify(mView, never()).showItemIsEmpty();
        verify(mView, never()).showItemsList();
    }

}
