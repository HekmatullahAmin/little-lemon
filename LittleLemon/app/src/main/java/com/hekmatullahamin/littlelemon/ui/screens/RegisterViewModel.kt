package com.hekmatullahamin.littlelemon.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.hekmatullahamin.littlelemon.data.models.User
import com.hekmatullahamin.littlelemon.data.repositories.UserRepository
import com.hekmatullahamin.littlelemon.ui.state.UserUiState
import com.hekmatullahamin.littlelemon.utils.Constants
import com.hekmatullahamin.littlelemon.utils.Validations
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first

class RegisterViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    //    the state that our Register screen will use
    var userUiState by mutableStateOf(UserUiState())
        private set

    //    after each typing to text fields we will update our userUiState and check for validation after
    //    each entry. if everything is ok then the esEntryValid will be true and then we will be
    //    able to create account
    fun updateUiState(userDetails: User) {
        userUiState = userUiState.copy(userDetails = userDetails)
        validateInput(userDetails)
    }

    //    we check that all of our text field is not empty and if not then we proceed
    private fun validateInput(user: User = userUiState.userDetails): Boolean {
        val isValidEmail = Validations.isValidEmail(user.userEmailAddress)
        val isValidPassword = Validations.isValidPassword(user.userPassword)
        val isDetailsValid = with(user) {
            userFirstName.isNotBlank() && userLastName.isNotBlank() && userDateOfBirth > 0L
        }

//        Update error in UserUiState
        userUiState = userUiState.copy(
            emailError = if (isValidEmail) null else Constants.INVALID_EMAIL_ADDRESS_ERROR_MESSAGE,
            passwordError = if (isValidPassword) null else Constants.INVALID_PASSWORD_ERROR_MESSAGE,
            isEntryValid = isValidEmail && isValidPassword && isDetailsValid
        )

        return isValidEmail && isValidPassword && isDetailsValid
    }

//    we can save and return the userId as long after insertion get completed
    suspend fun saveUser(user: User): User? {
        if (validateInput(user)) {
            userRepository.insertUser(user)
            // Fetch the inserted user back from the database
            return userRepository.getUserStream(user.userEmailAddress, user.userPassword)
                .filterNotNull()
                .first()
        }
        return null
    }
}