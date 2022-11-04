package com.demo.googledrive

import android.R
import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import com.demo.googledrive.HiltTestActivity
import junit.framework.AssertionFailedError
import java.util.concurrent.TimeoutException

fun <T: Fragment> launchFragmentInHiltContainer(
    fragmentArgs: Bundle,
    createFragment: () -> T,
    onLaunched: (fragment: T) -> Unit
) {
    val startActivityIntent = Intent.makeMainActivity(
        ComponentName(
            ApplicationProvider.getApplicationContext(),
            HiltTestActivity::class.java
        )
    )
    ActivityScenario.launch<Activity>(startActivityIntent).onActivity { activity: Activity ->
        val fragment: T = createFragment()
        fragment.arguments = fragmentArgs
        (activity as HiltTestActivity).supportFragmentManager
            .beginTransaction()
            .add(R.id.content, fragment, "")
            .commitNow()
        onLaunched(fragment)
    }
}

fun ViewInteraction.waitUntilVisible(timeout: Long): ViewInteraction {
    val startTime = System.currentTimeMillis()
    val endTime = startTime + timeout

    do {
        try {
            check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
            return this
        } catch (e: AssertionFailedError) {
            Thread.sleep(50)
        }
    } while (System.currentTimeMillis() < endTime)

    throw TimeoutException()
}
