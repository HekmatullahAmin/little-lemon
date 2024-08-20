package com.hekmatullahamin.littlelemon.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.hekmatullahamin.littlelemon.data.models.User
import com.hekmatullahamin.littlelemon.data.repositories.UserRepository
import com.hekmatullahamin.littlelemon.ui.state.PasswordUserUiState
import kotlinx.coroutines.flow.first

class LoginViewModel(private val userRepository: UserRepository) : ViewModel() {

    var uiState: PasswordUserUiState by mutableStateOf(PasswordUserUiState())
        private set

    fun updatePasswordUiState(userDetails: User) {
        uiState = PasswordUserUiState(
            userDetails = userDetails,
            isEntryValid = validateInput(userDetails)
        )
    }

    private fun validateInput(user: User = uiState.userDetails): Boolean {
        return with(user) {
            userEmailAddress.isNotBlank() && userPassword.isNotBlank()
        }
    }

    suspend fun getUser(user: User): User? {
        return userRepository.getUserStream(
            userEmail = user.userEmailAddress,
            userPassword = user.userPassword
        ).first()
    }
}