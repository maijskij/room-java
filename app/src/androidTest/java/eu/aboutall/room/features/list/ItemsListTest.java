package eu.aboutall.room.features.list;

import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;
import org.junit.runner.RunWith;

import eu.aboutall.room.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;


/**
 * Tests for the items list screen, the main screen which contains a list of all items.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class ItemsListTest {

    /**
     * Prepare your test fixture for this test. In this case we register an IdlingResources with
     * Espresso. IdlingResource resource is a great way to tell Espresso when your app is in an
     * idle state. This helps Espresso to synchronize your test actions, which makes tests significantly
     * more reliable.
     */
  /*  @Before
    public void setUp() throws Exception {
        IdlingRegistry.getInstance().register(
                mTasksActivityTestRule.getActivity().getCountingIdlingResource());
    }
*/
    /**
     * Unregister your Idling Resource so it can be garbage collected and does not leak any memory.
     */
 /*   @After
    public void tearDown() throws Exception {
        IdlingRegistry.getInstance().unregister(
                mTasksActivityTestRule.getActivity().getCountingIdlingResource());
    }
*/

    @Test
    public void clickAddTaskButton_oneNewItemAppears() {

        createItem();

        onView (withId (R.id.item_list)).check (ViewAssertions.matches (withListSize (1)));
    }

    private void createItem() {
        // Click on the add item button
        onView(withId(R.id.fab_new)).perform(click());
    }

    private void editItem() {
        // Click on the add item button
        onView(withId(R.id.fab_new)).perform(click());
    }

    private void deleteItem() {
        // Click on the add item button
        onView(withId(R.id.fab_new)).perform(click());

    }


    public static Matcher<View> withListSize(final int size) {
        return new TypeSafeMatcher<View>() {
            @Override
            public boolean matchesSafely(final View view) {
                return ((RecyclerView) view).getAdapter().getItemCount() == size;
            }

            @Override
            public void describeTo(final Description description) {
                description.appendText("ListView should have " + size + " items");
            }
        };
    }

}
