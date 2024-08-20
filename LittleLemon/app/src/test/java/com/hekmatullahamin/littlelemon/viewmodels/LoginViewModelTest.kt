package com.hekmatullahamin.littlelemon.viewmodels

import com.hekmatullahamin.littlelemon.data.models.User
import com.hekmatullahamin.littlelemon.data.repositories.UserRepository
import com.hekmatullahamin.littlelemon.fake.FakeUserRepository
import com.hekmatullahamin.littlelemon.rules.TestDispatcherRule
import com.hekmatullahamin.littlelemon.ui.screens.LoginViewModel
import com.hekmatullahamin.littlelemon.ui.state.PasswordUserUiState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.Assert.assertTrue
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Rule

class LoginViewModelTest {

    @get:Rule
    val testDispatcherRule = TestDispatcherRule()
    private lateinit var fakeUserRepository: UserRepository
    private lateinit var viewModel: LoginViewModel

    @Before
    fun setup() {
        fakeUserRepository = FakeUserRepository()
        viewModel = LoginViewModel(
            userRepository = fakeUserRepository
        )
    }

    @Test
    fun loginViewModel_Initialization_DefaultUiState_IsEmpty() {
        assertEquals(
            PasswordUserUiState(),
            viewModel.uiState
        )
    }

    @Test
    fun loginViewModel_UpdatePasswordUiState_ValidDetails_EntryValidIsTrue() {
        val user = User(
            userEmailAddress = "hekmat@gmail.com",
            userPassword = "hekmat@1"
        )

        viewModel.updatePasswordUiState(user)

        assertTrue(viewModel.uiState.isEntryValid)
    }

    @Test
    fun loginViewModel_UpdatePasswordUiState_EmptyEmail_EntryValidIsFalse() {
        val user = User(
            userEmailAddress = "",
            userPassword = "hekmat@1"
        )

        viewModel.updatePasswordUiState(user)

//        Verify that isEntryValid is false
        assertFalse(viewModel.uiState.isEntryValid)
    }

    @Test
    fun loginViewModel_UpdatePasswordUiState_EmptyPassword_EntryValidIsFalse() {
        val user = User(
            userEmailAddress = "",
            userPassword = "hekmat@1"
        )

        viewModel.updatePasswordUiState(user)

        assertFalse(viewModel.uiState.isEntryValid)
    }

    @Test
    fun loginViewModel_UpdatePasswordUiState_BothEmailAndPasswordEmpty_EntryValidIsFalse() {
        val user = User(
            userEmailAddress = "",
            userPassword = "hekmat@1"
        )

        viewModel.updatePasswordUiState(user)
        assertFalse(viewModel.uiState.isEntryValid)
    }

    @Test
    fun loginViewModel_GetUser_ValidUser_ReturnsUser() = runTest {
        val user = User(
            userEmailAddress = "hekmat@gmail.com",
            userPassword = "hekmat@1"
        )
        fakeUserRepository.insertUser(user)
        val userFromDb = viewModel.getUser(user)
        assertEquals(user, userFromDb)
    }

    @Test
    fun loginViewModel_GetUser_InvalidUser_ReturnsNull() = runTest {
        val user = User(
            userEmailAddress = "hekmat@gmail.com",
            userPassword = "hekmat@1"
        )
        fakeUserRepository.insertUser(user)
//        if user enter the wrong email then they can not fetch the user
        val userFromDb = viewModel.getUser(user.copy(userEmailAddress = "mahmood@yahoo.com"))
        assertEquals(null, userFromDb)
    }
}