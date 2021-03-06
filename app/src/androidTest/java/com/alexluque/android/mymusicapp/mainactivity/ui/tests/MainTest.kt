package com.alexluque.android.mymusicapp.mainactivity.ui.tests

import android.app.Application
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.SystemClock
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.test.InstrumentationRegistry.getTargetContext
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import com.alexluque.android.mymusicapp.mainactivity.R
import com.alexluque.android.mymusicapp.mainactivity.ui.common.DaggerUiTestComponent
import com.alexluque.android.mymusicapp.mainactivity.ui.main.MainActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@ExperimentalCoroutinesApi
@ExperimentalStdlibApi
class MainTest {

    @get:Rule
    val activityTestRule = ActivityTestRule(MainActivity::class.java, false, false)

    @get:Rule
    val grantPermissionRule: GrantPermissionRule =
        GrantPermissionRule.grant("android.permission.ACCESS_COARSE_LOCATION")

    private lateinit var artistName: String

    @Before
    fun setUp() {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        val app = instrumentation.targetContext.applicationContext as Application
        val intent = Intent(instrumentation.targetContext, MainActivity::class.java)
        val component = DaggerUiTestComponent.factory().create(app)

        runBlockingTest { artistName = component.localDataSource.getFavouriteArtists().first().name }

        activityTestRule.launchActivity(intent)
    }


    @Test
    fun clickFavouriteArtist_NavigatesToDetail() {
        // Click on first element of favourite artists list
        onView(
            withId(R.id.artists_recycler_view)
        ).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(FIRST_ARTIST, click())
        )

        // Wait until DetailActivity loads data
        SystemClock.sleep(2000)

        // Verify that Detail activity's toolbar's has the artist name as its title
        onView(
            withId(
                R.id.toolbar
            )
        ).check(
            matches(
                hasDescendant(
                    withText(
                        artistName
                    )
                )
            )
        )
    }

    @Test
    fun clickRecommendArtists_NavigatesToRecommendations() {
        // Click on the recommendations button
        onView(
            withId(R.id.action_recommend)
        ).perform(
            click()
        )

        // Verify that recently opened activity has the title of the Recommendations activity
        onView(
            withId(
                R.id.toolbar
            )
        ).check(
            matches(
                hasDescendant(
                    withText(
                        R.string.recommendations_activity_title
                    )
                )
            )
        )
    }

    @Test
    fun clickSearch_OpensDialog() {
        // Click on search button
        onView(
            withId(R.id.action_search)
        ).perform(
            click()
        )

        // Dialog is opened
        onView(
            withId(R.id.artistName_editText)
        ).inRoot(
            isDialog()
        ).check(
            matches(
                isDisplayed()
            )
        )

        // Click on cancel artist
        onView(
            withText(R.string.cancel_button)
        ).perform(
            click()
        )
    }

    @Test
    fun clickSearchAndSetArtistName_NavigatesToDetail() {
        // Click on search button
        onView(
            withId(R.id.action_search)
        ).perform(
            click()
        )

        // Writes down artist name inside dialog's edit text
        onView(
            withId(R.id.artistName_editText)
        ).perform(
            ViewActions.typeText(artistName)
        )

        // Click on search artist
        onView(
            withText(R.string.search_button)
        ).perform(
            click()
        )

        // Wait until DetailActivity loads data
        SystemClock.sleep(2000) // Waits until SearchActivity loads data

        // Verify that Detail activity's toolbar's has the artist name as its title
        onView(
            withId(
                R.id.toolbar
            )
        ).check(
            matches(
                hasDescendant(
                    withText(
                        artistName
                    )
                )
            )
        )
    }

    @Test
    fun clickSearchArtistWhenNameIsEmpty_DoesNotNavigateToDetail() {
        // Verify that we are in the MainActivity
        checkIsMainActivity()

        // Open Search dialog
        onView(
            withId(R.id.action_search)
        ).perform(
            click()
        )

        // Try to search without typing anything
        onView(
            withText(R.string.search_button)
        ).perform(
            click()
        )

        // Verify that we still being in the MainActivity
        checkIsMainActivity()
    }

    private fun checkIsMainActivity() {
        onView(
            withId(
                R.id.toolbar
            )
        ).check(
            matches(
                hasDescendant(
                    withText(
                        R.string.main_activity_title
                    )
                )
            )
        )
    }

    @Test
    fun allActivitiesHaveSameStatusBarBackgroundColor() {
        // Check MainActivity's status bar background color
        checkStatusBarColor(STATUS_BAR_COLOR)

        // Click on the recommendations button
        onView(
            withId(R.id.action_recommend)
        ).perform(
            click()
        )

        // Check RecommendationsActivity's status bar background color
        checkStatusBarColor(STATUS_BAR_COLOR)

        // Click back button to return to MainActivity
        Espresso.pressBackUnconditionally()

        // Click on first element of favourite artists list
        onView(
            withId(R.id.artists_recycler_view)
        ).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(FIRST_ARTIST, click())
        )

        // Check DetailActivity's status bar background color
        checkStatusBarColor(STATUS_BAR_COLOR)
    }

    @Test
    fun infoElementsAreGoneWhenFavouriteListNotEmpty() {
        // Image is gone
        onView(
            withId(R.id.image_emptyList)
        ).check(
            matches(
                not(isDisplayed())
            )
        )

        // Title is gone
        onView(
            withId(R.id.text_emptyList)
        ).check(
            matches(
                not(isDisplayed())
            )
        )

        // Message is gone
        onView(
            withId(R.id.text_emptyMessage)
        ).check(
            matches(
                not(isDisplayed())
            )
        )
    }

    private fun checkStatusBarColor(color: Int) {
        onView(
            withId(android.R.id.statusBarBackground)
        ).check(
            matches(
                withBackgroundColor(color)
            )
        )
    }

    private fun withBackgroundColor(colorId: Int): Matcher<View?>? {
        val colorFromResource = ContextCompat.getColor(getTargetContext(), colorId)
        return object : BoundedMatcher<View?, View>(View::class.java) {
            override fun matchesSafely(view: View): Boolean {
                val backGroundColor = (view.background as ColorDrawable).color
                return colorFromResource == backGroundColor
            }

            override fun describeTo(description: Description?) {}
        }
    }

    private companion object {
        private const val FIRST_ARTIST = 0
        private const val STATUS_BAR_COLOR = R.color.colorPrimaryDark
    }
}