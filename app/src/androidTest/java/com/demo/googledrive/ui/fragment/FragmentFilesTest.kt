package com.demo.googledrive.ui.fragment

import android.os.Bundle
import androidx.navigation.Navigation.setViewNavController
import androidx.navigation.testing.TestNavHostController
import androidx.paging.PagingSource
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.demo.drive.core.model.DriveFile
import com.demo.drive.core.network.AuthorizationProvider
import com.demo.drive.core.repository.DriveRepository
import com.demo.drive.data.di.AuthorizationProviderModule
import com.demo.drive.data.di.DriveRepositoryModule
import com.demo.drive.data.network.impl.MockAuthorizationProvider
import com.demo.drive.data.repository.MockDriveRepository
import com.demo.googledrive.R
import com.demo.googledrive.launchFragmentInHiltContainer
import com.demo.googledrive.waitUntilVisible
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.hamcrest.core.AllOf.allOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
@UninstallModules(value = [AuthorizationProviderModule::class, DriveRepositoryModule::class])
class FragmentFilesTest {

    private val driveFiles = listOf(
        DriveFile(
            mimeType = "application/vnd.google-apps.folder",
            name = "folder1",
            id = "id1",
            kind = null,
            thumbnailLink = null,
            modifiedTime = null,
            size = null
        ),
        DriveFile(
            mimeType = "application/vnd.google-apps.folder",
            name = "folder2",
            id = "id2",
            kind = null,
            thumbnailLink = null,
            modifiedTime = null,
            size = null
        ),
        DriveFile(
            mimeType = "image/jpg",
            name = "image.jpg",
            id = "id3",
            kind = null,
            thumbnailLink = "https://picsum.photos/200",
            modifiedTime = null,
            size = null
        ),
    )

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @BindValue
    val authorizationProvider: AuthorizationProvider = MockAuthorizationProvider()

    @BindValue
    lateinit var driveRepository: DriveRepository
    private val mockDriveRepository: MockDriveRepository get() = driveRepository as MockDriveRepository

    private lateinit var navHostController: TestNavHostController

    @Before
    fun setup() {
        hiltRule.inject()

        navHostController = TestNavHostController(ApplicationProvider.getApplicationContext())
        driveRepository = MockDriveRepository()
    }

    @Test
    fun fileListIsDisplayedCorrectlyWhenSuccessResponse() {
        //set result that repository will return when list() is called
        mockDriveRepository.setLoadResultForList(PagingSource.LoadResult.Page(
            data = driveFiles,
            nextKey = null,
            prevKey = null
        ))

        launchFragmentInHiltContainer(Bundle(), createFragment = {
            FragmentFiles()
        }, onLaunched = { fragment ->
            val inflater = navHostController.navInflater
            val navGraph = inflater.inflate(R.navigation.default_navigation)
            navGraph.setStartDestination(R.id.fragmentLogin)
            navHostController.graph = navGraph
            setViewNavController(fragment.requireView(), navHostController)
        })

        onView(withId(R.id.recyclerView)).waitUntilVisible(5000)

        onView(withText("folder1")).check(matches(isDisplayed()))
        onView(withText("folder2")).check(matches(isDisplayed()))
        onView(withText("image.jpg")).check(matches(isDisplayed()))
    }

    @Test
    fun errorMessageIsDisplayedWhenApiReturnsError() {
        mockDriveRepository.setLoadResultForList(PagingSource.LoadResult.Error(Exception("error message")))

        launchFragmentInHiltContainer(Bundle(), createFragment = {
            FragmentFiles()
        }, onLaunched = { fragment ->
            val inflater = navHostController.navInflater
            val navGraph = inflater.inflate(R.navigation.default_navigation)
            navGraph.setStartDestination(R.id.fragmentLogin)
            navHostController.graph = navGraph
            setViewNavController(fragment.requireView(), navHostController)
        })

        onView(allOf(withId(R.id.textViewError), withText("error message"), isDisplayed())).waitUntilVisible(1000)
    }
}