package com.hekmatullahamin.littlelemon.viewmodels

import com.hekmatullahamin.littlelemon.data.models.User
import com.hekmatullahamin.littlelemon.data.repositories.UserRepository
import com.hekmatullahamin.littlelemon.fake.FakeUserRepository
import com.hekmatullahamin.littlelemon.ui.screens.RegisterViewModel
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assert.assertFalse

class RegisterViewModelTest {
    private lateinit var fakeUserRepository: UserRepository
    private lateinit var viewModel: RegisterViewModel

    @Before
    fun setup() {
        fakeUserRepository = FakeUserRepository()
        viewModel = RegisterViewModel(
            userRepository = fakeUserRepository
        )
    }

    @Test
    fun registerViewModel_UpdateUiState_UserUiStateUpdated() {
        val user = User(
            userId = 1,
            userFirstName = "hekmat",
            userLastName = "amin",
            userEmailAddress = "hekmat@gmail.com",
            userPassword = "hekmat@1",
            userImageUri = "",
            userDateOfBirth = 10000000L
        )

        viewModel.updateUiState(user)
        assertEquals(user, viewModel.userUiState.userDetails)
    }

    @Test
    fun registerViewModel_ValidInputProvided_InputValidationSucceeds() {
        val user = User(
            userId = 1,
            userFirstName = "hekmat",
            userLastName = "amin",
            userEmailAddress = "hekmat@gmail.com",
            userPassword = "hekmat@1",
            userImageUri = "",
            userDateOfBirth = 10000000L
        )

        viewModel.updateUiState(user)
        assertTrue(viewModel.userUiState.isEntryValid)
    }

    @Test
    fun registerViewModel_BlankNameFieldsProvided_InputValidationFails() {
        val user = User(
            userId = 1,
            userFirstName = "",
            userLastName = "amin",
            userEmailAddress = "hekmat@gmail.com",
            userPassword = "hekmat@1",
            userImageUri = "",
            userDateOfBirth = 10000000L
        )

        viewModel.updateUiState(user)
        assertFalse(viewModel.userUiState.isEntryValid)
    }

    @Test
    fun registerViewModel_SaveUser_ValidUserDetails_UserSavedAndReturned() = runTest {
        val user = User(
            userId = 1,
            userFirstName = "hekmat",
            userLastName = "amin",
            userEmailAddress = "hekmat@gmail.com",
            userPassword = "hekmat@1",
            userImageUri = "",
            userDateOfBirth = 10000000L
        )

        val insertedUser = viewModel.saveUser(user)
        assertEquals(user, insertedUser)
    }

    @Test
    fun registerViewModel_SaveUser_InvalidUserDetails_UserNotSavedAndReturnsNull() = runTest{
        val user = User(
            userId = 1,
            userFirstName = "",
            userLastName = "amin",
            userEmailAddress = "hekmat@gmail.com",
            userPassword = "hekmat@1",
            userImageUri = "",
            userDateOfBirth = 10000000L
        )

        val insertedUser = viewModel.saveUser(user)
        assertEquals(null, insertedUser)
    }
}