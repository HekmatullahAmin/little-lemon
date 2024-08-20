package com.hekmatullahamin.littlelemon.screens

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.lifecycle.SavedStateHandle
import com.hekmatullahamin.littlelemon.R
import com.hekmatullahamin.littlelemon.data.models.User
import com.hekmatullahamin.littlelemon.data.repositories.AddressRepository
import com.hekmatullahamin.littlelemon.data.repositories.UserRepository
import com.hekmatullahamin.littlelemon.fake.FakeAddressRepository
import com.hekmatullahamin.littlelemon.fake.FakeUserRepository
import com.hekmatullahamin.littlelemon.onNodeWithStringId
import com.hekmatullahamin.littlelemon.ui.navigation.EditProfileDestination
import com.hekmatullahamin.littlelemon.ui.screens.EditProfileScreen
import com.hekmatullahamin.littlelemon.ui.screens.EditProfileViewModel
import com.hekmatullahamin.littlelemon.ui.theme.LittleLemonTheme
import com.hekmatullahamin.littlelemon.utils.Constants
import com.hekmatullahamin.littlelemon.utils.formatDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import org.junit.Rule
import org.junit.Test

class EditProfileScreenUiTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private fun setupEditProfileScreen(
        onNavigateUpClicked: () -> Unit = {},
        onAddressClicked: () -> Unit = {},
        userRepository: UserRepository = FakeUserRepository(),
        addressRepository: AddressRepository = FakeAddressRepository(),
        editProfileViewModel: EditProfileViewModel = EditProfileViewModel(
            savedStateHandle = SavedStateHandle(mapOf(EditProfileDestination.userIdArg to 1)),
            addressRepository = addressRepository,
            userRepository = userRepository
        )
    ) {
        composeTestRule.setContent {
            LittleLemonTheme {
                EditProfileScreen(
                    onNavigateUpClicked = onNavigateUpClicked,
                    onAddressClicked = onAddressClicked,
                    editProfileViewModel = editProfileViewModel
                )
            }
        }
    }

    @Test
    fun editProfileScreen_initialState_displayUserDetails() = runTest {
        val user = User(
            userId = 1,
            userFirstName = "John",
            userLastName = "Doe",
            userDateOfBirth = 631152000000, // 01/01/1990
            userEmailAddress = "john.doe@example.com",
            userPassword = "password123"
        )

        setupEditProfileScreen(
            userRepository = FakeUserRepository().apply {
                insertUser(user)
            }
        )

        composeTestRule.onNodeWithText(user.userFirstName).assertIsDisplayed()
        composeTestRule.onNodeWithText(user.userLastName).assertIsDisplayed()
        composeTestRule.onNodeWithText(user.userDateOfBirth.formatDate()).assertIsDisplayed()
        composeTestRule.onNodeWithText(user.userEmailAddress).assertIsDisplayed()
        composeTestRule.onNodeWithText(user.userPassword).assertIsDisplayed()
    }

    @Test
    fun editProfileScreen_emptyFields_disableSaveButton() = runTest {
        val user = User(
            userId = 1,
            userFirstName = "",
            userLastName = "Doe",
            userDateOfBirth = 631152000000, // 01/01/1990
            userEmailAddress = "john.doe@example.com",
            userPassword = ""
        )

        setupEditProfileScreen(
            userRepository = FakeUserRepository().apply {
                insertUser(user)
            }
        )

        composeTestRule.onNodeWithStringId(R.string.save_text_label).assertIsNotEnabled()
    }

    @Test
    fun editProfileScreen_clickDateOfBirth_opensDatePickerDialog() = runTest {
        val user = User(
            userId = 1,
            userFirstName = "",
            userLastName = "Doe",
            userDateOfBirth = 631152000000, // 01/01/1990
            userEmailAddress = "john.doe@example.com",
            userPassword = ""
        )

        setupEditProfileScreen(
            userRepository = FakeUserRepository().apply {
                insertUser(user)
            }
        )

//        we used user.userDateOfBirth.formatDate() because this date is shown in that
//                textfield
        composeTestRule.onNodeWithText(user.userDateOfBirth.formatDate())
            .performClick()
        composeTestRule.onNodeWithTag("DatePickerDialog").assertIsDisplayed()
    }

    @Test
    fun editProfileScreen_clickAddress_opensAddressScreen() = runTest {
        val user = User(
            userId = 1,
            userFirstName = "Alex",
            userLastName = "Doe",
            userDateOfBirth = 631152000000, // 01/01/1990
            userEmailAddress = "john.doe@example.com",
            userPassword = "Alex@231"
        )

        var addressClicked = false
        setupEditProfileScreen(
            onAddressClicked = { addressClicked = true },
            userRepository = FakeUserRepository().apply {
                insertUser(user)
            }
        )

        composeTestRule.onNodeWithText(Constants.SELECT_YOUR_ADDRESS).performClick()
        assert(addressClicked)
    }
}