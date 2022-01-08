package com.torrescalazans.githubclient.presentation.home

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.torrescalazans.githubclient.core.MockWebServerTest
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith

@kotlinx.coroutines.ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class HomeActivityTest: MockWebServerTest() {

    @Test
    fun retryButton_receivedSocketTimeoutExceptionResponse_retryButtonVisible() {
        runTest {
//            enqueueTimeoutMockResponse()
//
//            launchActivity<HomeActivity>()
//
//            onView(withId(R.id.progressbar_repository_load_state)).check(matches(not(isDisplayed())))
//            onView(withId(R.id.textview_empty_list)).check(matches(not(isDisplayed())))
//            onView(withId(R.id.recyclerview_repositories)).check(matches(not(isDisplayed())))
//
//            onView(withId(R.id.textview_error_message)).check(matches(isDisplayed()))
//            onView(withId(R.id.button_retry_request)).check(matches(isDisplayed()))
        }
    }

    @Test
    fun retryButton_receivedSocketTimeoutExceptionResponse_retryButtonClick() {
        runTest {
//            enqueueTimeoutMockResponse()
//            enqueueMockResponse(GitHubServiceTest.MOCK_GITHUB_PAGE_1_RESPONSE_FILE_PATH)
//
//            launchActivity<HomeActivity>()
//
//            onView(withId(R.id.progressbar_repository_load_state)).check(matches(not(isDisplayed())))
//            onView(withId(R.id.textview_empty_list)).check(matches(not(isDisplayed())))
//            onView(withId(R.id.recyclerview_repositories)).check(matches(not(isDisplayed())))
//
//            onView(withId(R.id.textview_error_message)).check(matches(isDisplayed()))
//            onView(withId(R.id.button_retry_request)).check(matches(isDisplayed()))
//
//            onView(withId(R.id.button_retry_request)).perform(click())
//
//            val request = server.takeRequest()
//            assertThat(request.path).isEqualTo(REQUEST_PATH)
        }
    }
}
