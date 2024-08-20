package com.hekmatullahamin.littlelemon.screens

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.hekmatullahamin.littlelemon.R
import com.hekmatullahamin.littlelemon.data.repositories.UserRepository
import com.hekmatullahamin.littlelemon.fake.FakeUserRepository
import com.hekmatullahamin.littlelemon.onNodeWithStringId
import com.hekmatullahamin.littlelemon.ui.screens.RegisterScreen
import com.hekmatullahamin.littlelemon.ui.screens.RegisterViewModel
import com.hekmatullahamin.littlelemon.utils.Constants
import com.hekmatullahamin.littlelemon.utils.formatDate
import org.junit.Rule
import org.junit.Test

class RegisterScreenUiTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private fun setupRegisterScreen(
        onCreateMyAccountButtonClicked: (Int) -> Unit = {},
        onLoginTextClicked: () -> Unit = {},
        fakeUserRepository: UserRepository = FakeUserRepository(),
        registerViewModel: RegisterViewModel = RegisterViewModel(
            userRepository = fakeUserRepository
        )
    ) {
        composeTestRule.setContent {
            RegisterScreen(
                onCreateMyAccountButtonClicked = onCreateMyAccountButtonClicked,
                onLoginTextClicked = onLoginTextClicked,
                registerViewModel = registerViewModel
            )
        }
    }

    @Test
    fun registerScreen_validInput_enablesCreateMyAccountButton() {
//        Launch the RegisterScreen
        val registerViewModel = RegisterViewModel(
            userRepository = FakeUserRepository()
        )
        setupRegisterScreen(
            registerViewModel = registerViewModel
        )
//        Enter valid first name
        composeTestRule.onNodeWithStringId(R.string.enter_first_name_placeholder)
            .performTextInput("Sultan")
//        Enter valid last name
        composeTestRule.onNodeWithStringId(R.string.enter_last_name_placeholder)
            .performTextInput("Rasooli")
//        Simulate selecting a date from the DatePickerDialog
        registerViewModel.updateUiState(
            registerViewModel.userUiState.userDetails.copy(
                userDateOfBirth = 631152000000L // 01/01/1990
            )
        )

//        Enter valid email address
        composeTestRule.onNodeWithStringId(R.string.enter_your_email_placeholder)
            .performTextInput("hekmat@gmail.com")
        //        Enter valid password
        composeTestRule.onNodeWithStringId(R.string.enter_your_password_placeholder)
            .performTextInput("hekmat@1")
//        Verify that the Create My Account button is enabled
        composeTestRule.onNodeWithStringId(R.string.button_create_my_account_label)
            .assertIsEnabled()
    }

    @Test
    fun registerScreen_inValidEmail_showsEmailError() {
        setupRegisterScreen()
//        Enter invalid email address
        composeTestRule.onNodeWithStringId(R.string.enter_your_email_placeholder)
            .performTextInput("hekmat@gmail")
//        Verify that the error is displayed
        composeTestRule.onNodeWithText(Constants.INVALID_EMAIL_ADDRESS_ERROR_MESSAGE)
            .assertIsDisplayed()
    }

    @Test
    fun registerScreen_inValidPassword_showsPasswordError() {
        setupRegisterScreen()
//        Enter invalid password(password with no symbol)
        composeTestRule.onNodeWithStringId(R.string.enter_your_password_placeholder)
            .performTextInput("hekmat1")
//        Verify that the error is displayed
        composeTestRule.onNodeWithText(Constants.INVALID_PASSWORD_ERROR_MESSAGE)
            .assertIsDisplayed()
    }
}