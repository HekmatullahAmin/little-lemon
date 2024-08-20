package com.hekmatullahamin.littlelemon

import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import com.hekmatullahamin.littlelemon.ui.navigation.LoginDestination
import com.hekmatullahamin.littlelemon.ui.navigation.RegisterDestination
import com.hekmatullahamin.littlelemon.ui.navigation.StartDestination
import org.junit.Before
import org.junit.Rule
import org.junit.Test

//TODO: implement test for this class
class LittleLemonNavigationTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var navController: TestNavHostController

    //    Instead of having to manually call the setupCupcakeNavHost() function for every test to set up the nav controller,
    //    you can make that happen automatically using the @Before annotation
    //    When a method is annotated with @Before, it runs before every method annotated with @Test.
    @Before
    fun setupLittleLemonNavHost() {
        composeTestRule.setContent {
            navController = TestNavHostController(LocalContext.current).apply {
                navigatorProvider.addNavigator(ComposeNavigator())
            }
            MyLittleLemonApp(navController = navController)
        }
    }

    //    confirm that the nav controller's initial destination route is the Start Screen.
    @Test
    fun littleLemonNavHost_verifyStartDestination() {
//        assertEquals(
//            StartDestination.route,
//            navController.currentBackStackEntry?.destination?.route
//        )
//        OR
        navController.assertCurrentRouteName(StartDestination.route)
    }

    //    confirms the Start screen doesn't have an Up button
    @Test
    fun littleLemonNavHost_verifyBackNavigationNotShownOnStartScreen() {
        val backText = composeTestRule.activity.getString(R.string.back_button_content_description)
        composeTestRule.onNodeWithContentDescription(backText).assertDoesNotExist()
    }

    @Test
    fun littleLemonNavHost_loginButtonClicked_navigatesToLoginScreenFromStartScreen() {
        navigateToLoginScreen()
        navController.assertCurrentRouteName(LoginDestination.route)
    }

    @Test
    fun littleLemonNavHost_createAccountButtonClicked_navigatesToRegisterScreenFromStartScreen() {
        navigateToRegisterScreen()
        navController.assertCurrentRouteName(RegisterDestination.route)
    }

//    TODO: implement the rest of navigation unit test

    private fun navigateToRegisterScreen() {
        composeTestRule.onNodeWithStringId(R.string.button_create_account_label)
            .performClick()
    }

    private fun navigateToLoginScreen() {
        composeTestRule.onNodeWithStringId(R.string.button_login_label)
            .performClick()
//        the Log In button on the Login screen will not be clickable
//        until email and password is typed.
    }
}