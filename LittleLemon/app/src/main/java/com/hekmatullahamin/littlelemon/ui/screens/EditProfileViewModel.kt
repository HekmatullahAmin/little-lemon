package com.hekmatullahamin.littlelemon.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hekmatullahamin.littlelemon.data.models.User
import com.hekmatullahamin.littlelemon.data.repositories.AddressRepository
import com.hekmatullahamin.littlelemon.data.repositories.UserRepository
import com.hekmatullahamin.littlelemon.ui.navigation.EditProfileDestination
import com.hekmatullahamin.littlelemon.ui.state.EditProfileUiState
import com.hekmatullahamin.littlelemon.utils.Constants
import com.hekmatullahamin.littlelemon.utils.Validations
import com.hekmatullahamin.littlelemon.utils.concatenateAddress
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import org.jetbrains.annotations.VisibleForTesting

class EditProfileViewModel(
    savedStateHandle: SavedStateHandle,
    private val userRepository: UserRepository,
    private val addressRepository: AddressRepository,
) : ViewModel() {
    private val userId: Int = checkNotNull(savedStateHandle[EditProfileDestination.userIdArg])

    var editProfileUiState by mutableStateOf(EditProfileUiState())
        private set

    init {
        viewModelScope.launch {
//            Using combine to merge the user and address streams allows you to handle both data sources together, making the state updates more coherent.
            combine(
                userRepository.getUserStreamUsingId(userId).filterNotNull(),
                addressRepository.getDefaultAddress(userId)
            ) { user, address ->
                val concatenatedAddress = address?.let {
                    concatenateAddress(it)
                } ?: Constants.SELECT_YOUR_ADDRESS // Use the default value (Select your address)

                // Update UI state
                editProfileUiState = editProfileUiState.copy(
                    userDetails = user,
                    defaultAddress = concatenatedAddress
                )
            }.collect()
        }
    }

    fun updateUiState(userDetails: User) {
        editProfileUiState = editProfileUiState.copy(userDetails = userDetails)
        validateInput(userDetails)
    }

    @VisibleForTesting
    fun validateInput(user: User): Boolean {
        val isEmailValid = Validations.isValidEmail(user.userEmailAddress)
        val isPasswordValid = Validations.isValidPassword(user.userPassword)
//        TODO: we will let user to updated their details even they don't select an address
//        val isUserAddressAvailable = editProfileUiState.defaultAddress.isNotBlank() &&
//                editProfileUiState.defaultAddress != Constants.SELECT_YOUR_ADDRESS
        val isDetailsValid = with(user) {
            userFirstName.isNotBlank() && userLastName.isNotBlank()
                    && userDateOfBirth > 0L
        }

        editProfileUiState = editProfileUiState.copy(
            emailError = if (isEmailValid) null else Constants.INVALID_EMAIL_ADDRESS_ERROR_MESSAGE,
            passwordError = if (isPasswordValid) null else Constants.INVALID_PASSWORD_ERROR_MESSAGE,
            isEntryValid = isEmailValid && isPasswordValid && isDetailsValid
        )

//        return isEmailValid && isPasswordValid && isDetailsValid && isUserAddressAvailable
        return isEmailValid && isPasswordValid && isDetailsValid
    }

    suspend fun updateUser() {
        if (validateInput(editProfileUiState.userDetails)) {
            userRepository.updateUser(editProfileUiState.userDetails)
        }
    }
}
