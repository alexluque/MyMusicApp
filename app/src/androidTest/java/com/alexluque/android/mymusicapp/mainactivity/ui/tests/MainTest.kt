package com.alexluque.android.mymusicapp.mainactivity.ui.tests

import android.app.Application
import android.content.Intent
import android.os.SystemClock
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
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

    private companion object {
        private const val FIRST_ARTIST = 0
    }
}