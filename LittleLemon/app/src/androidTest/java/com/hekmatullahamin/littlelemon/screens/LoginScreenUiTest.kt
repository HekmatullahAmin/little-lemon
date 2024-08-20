package com.hekmatullahamin.littlelemon.screens

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import com.hekmatullahamin.littlelemon.R
import com.hekmatullahamin.littlelemon.data.models.User
import com.hekmatullahamin.littlelemon.fake.FakeUserRepository
import com.hekmatullahamin.littlelemon.onNodeWithStringId
import com.hekmatullahamin.littlelemon.ui.screens.LoginScreen
import com.hekmatullahamin.littlelemon.ui.screens.LoginViewModel
import com.hekmatullahamin.littlelemon.ui.theme.LittleLemonTheme
import org.junit.Rule
import org.junit.Test

class LoginScreenUiTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private fun setupLoginScreen(
        onLoginButtonClicked: (User?) -> Unit = {},
        onJoinTextClicked: () -> Unit = {},
        loginViewModel: LoginViewModel = LoginViewModel(
            userRepository = FakeUserRepository()
        )
    ) {
        composeTestRule.setContent {
            LoginScreen(
                onLoginButtonClicked = onLoginButtonClicked,
                onJoinTextClicked = onJoinTextClicked,
                loginViewModel = loginViewModel
            )
        }
    }

    @Test
    fun loginButton_isDisabled_whenFieldAreEmpty() {
        setupLoginScreen()
//        Ensure that the login button is disabled when fields are empty
        composeTestRule.onNodeWithStringId(R.string.button_login_label)
            .assertIsNotEnabled()
    }

    @Test
    fun loginButton_isEnabled_whenFieldsAreFilled() {
        setupLoginScreen()

        // Input valid email and password
        composeTestRule.onNodeWithStringId(R.string.email_placeholder)
            .performTextInput("test@example.com")

        composeTestRule.onNodeWithStringId(R.string.password_placeholder)
            .performTextInput("password123")

        // Check if the login button is enabled
        composeTestRule.onNodeWithStringId(R.string.button_login_label)
            .assertIsEnabled()
    }
}