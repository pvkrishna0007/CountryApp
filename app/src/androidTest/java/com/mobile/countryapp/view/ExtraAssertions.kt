package com.mobile.countryapp.view

import android.view.View
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.BaseMatcher
import org.hamcrest.Description

object ExtraAssertions {
    val isVisible: ViewAssertion
        get() = ViewAssertion { view, _ ->
            ViewMatchers.assertThat<View>(
                view,
                VisibilityMatcher(View.VISIBLE)
            )
        }

    val isGone: ViewAssertion
        get() = ViewAssertion { view, _ ->
            ViewMatchers.assertThat<View>(
                view,
                VisibilityMatcher(View.GONE)
            )
        }

    val isInvisible: ViewAssertion
        get() = ViewAssertion { view, _ ->
            ViewMatchers.assertThat<View>(
                view,
                VisibilityMatcher(View.INVISIBLE)
            )
        }

    private class VisibilityMatcher internal constructor(private val visibility: Int) :
        BaseMatcher<View?>() {
        override fun describeTo(description: Description) {
            val visibilityName: String =
                if (visibility == View.GONE) "GONE" else if (visibility == View.VISIBLE) "VISIBLE" else "INVISIBLE"
            description.appendText("View visibility must has equals $visibilityName")
        }

        override fun matches(any: Any): Boolean {
            require(any is View) { "Object must be instance of View. Object is instance of $any" }
            return any.visibility == visibility
        }

    }
}