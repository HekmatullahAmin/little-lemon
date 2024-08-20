package com.hekmatullahamin.littlelemon.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.hekmatullahamin.littlelemon.data.models.Address
import com.hekmatullahamin.littlelemon.data.models.User
import com.hekmatullahamin.littlelemon.data.repositories.AddressRepository
import com.hekmatullahamin.littlelemon.data.repositories.UserRepository
import com.hekmatullahamin.littlelemon.fake.FakeAddressRepository
import com.hekmatullahamin.littlelemon.fake.FakeUserRepository
import com.hekmatullahamin.littlelemon.rules.TestDispatcherRule
import com.hekmatullahamin.littlelemon.ui.navigation.EditProfileDestination
import com.hekmatullahamin.littlelemon.ui.screens.EditProfileViewModel
import com.hekmatullahamin.littlelemon.utils.Constants
import com.hekmatullahamin.littlelemon.utils.concatenateAddress
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule

@OptIn(ExperimentalCoroutinesApi::class)
class EditProfileViewModelTest {
    @get:Rule
    val testDispatcherRule = TestDispatcherRule()

    private lateinit var userRepository: UserRepository
    private lateinit var addressRepository: AddressRepository
    private lateinit var viewModel: EditProfileViewModel

    @Before
    fun setup() {
        userRepository = FakeUserRepository()
        addressRepository = FakeAddressRepository()
        viewModel = EditProfileViewModel(
            savedStateHandle = SavedStateHandle(mapOf(EditProfileDestination.userIdArg to 1)),
            userRepository = userRepository,
            addressRepository = addressRepository
        )
    }

    @Test
    fun editProfileViewModel_Initialization_UserAndAddressLoaded_CorrectUiStateSet() = runTest {
        val user = User(
            userId = 1,
            userFirstName = "Max",
            userLastName = "Rooney",
            userEmailAddress = "Max@gmail.com",
            userPassword = "MaxRon@123",
            userDateOfBirth = 1723312057492,
            userImageUri = ""
        )
        val address = Address(
            addressId = 1,
            userId = 1,
            addressLineOne = "Street 123",
            addressLineTwo = "City X",
            city = "City X",
            country = "Country Y",
            postalCode = "12345",
            isDefault = true
        )
        userRepository.insertUser(user)
        addressRepository.insertAddress(address)

        advanceTimeBy(3000L)

        assertEquals(user, viewModel.editProfileUiState.userDetails)
        assertEquals(concatenateAddress(address), viewModel.editProfileUiState.defaultAddress)
    }

    @Test
    fun editProfileViewModel_UpdateUiState_UserDetailsUpdated_CorrectUiStateSet() {
        val user = User(
            userId = 1,
            userFirstName = "Max",
            userLastName = "Rooney",
            userEmailAddress = "Max@gmail.com",
            userPassword = "MaxRon@123",
            userDateOfBirth = 1723312057492,
            userImageUri = ""
        )

        viewModel.updateUiState(user)
        assertEquals(user, viewModel.editProfileUiState.userDetails)
    }

    @Test
    fun editProfileViewModel_ValidateInput_ValidUserDetails_EntryValid() = runTest {

        val user = User(
            userId = 1,
            userFirstName = "Max",
            userLastName = "Rooney",
            userEmailAddress = "Max@gmail.com",
            userPassword = "MaxRon@123",
            userDateOfBirth = 1723312057492,
            userImageUri = ""
        )
        val address = Address(
            addressId = 1,
            userId = 1,
            addressLineOne = "Street 123",
            addressLineTwo = "City X",
            city = "City X",
            country = "Country Y",
            postalCode = "12345",
            isDefault = true
        )

        userRepository.insertUser(user)
        addressRepository.insertAddress(address)

        advanceTimeBy(5000L)

        viewModel.validateInput(user)
        assertTrue(viewModel.editProfileUiState.isEntryValid)
    }

    @Test
    fun editProfileViewModel_ValidateInput_InvalidEmail_EmailErrorShown() {
        val user = User(
            userId = 1,
            userFirstName = "Max",
            userLastName = "Rooney",
//            Invalid email results in error message
            userEmailAddress = "Maxgmail.com",
            userPassword = "MaxRon@123",
            userDateOfBirth = 1723312057492,
            userImageUri = ""
        )

        viewModel.updateUiState(user)
        assertEquals(
            Constants.INVALID_EMAIL_ADDRESS_ERROR_MESSAGE,
            viewModel.editProfileUiState.emailError
        )
    }

    @Test
    fun editProfileViewModel_ValidateInput_InvalidPassword_PasswordErrorShown() {
        val user = User(
            userId = 1,
            userFirstName = "Max",
            userLastName = "Rooney",
            userEmailAddress = "Max@gmail.com",
            // Invalid password results in error message(no number or less than 6 or no symbol)
            userPassword = "MaxRon@",
            userDateOfBirth = 1723312057492,
            userImageUri = ""
        )

        viewModel.updateUiState(user)
        assertEquals(
            Constants.INVALID_PASSWORD_ERROR_MESSAGE,
            viewModel.editProfileUiState.passwordError
        )
    }

    @Test
    fun editProfileViewModel_ValidateInput_BlankFirstName_EntryInvalid() {
        val user = User(
            userId = 1,
//            blank name will return false
            userFirstName = "",
            userLastName = "Rooney",
            userEmailAddress = "Max@gmail.com",
            userPassword = "MaxRon@1",
            userDateOfBirth = 1723312057492,
            userImageUri = ""
        )

        viewModel.updateUiState(user)
        assertFalse(viewModel.editProfileUiState.isEntryValid)
    }

    @Test
    fun editProfileViewModel_ValidateInput_BlankLastName_EntryInvalid() {
        val user = User(
            userId = 1,
            userFirstName = "Max",
            //            blank last name will return false
            userLastName = "",
            userEmailAddress = "Max@gmail.com",
            userPassword = "MaxRon@1",
            userDateOfBirth = 1723312057492,
            userImageUri = ""
        )

        viewModel.updateUiState(user)
        assertFalse(viewModel.editProfileUiState.isEntryValid)
    }

    @Test
    fun editProfileViewModel_ValidateInput_InvalidDateOfBirth_EntryInvalid() {
        val user = User(
            userId = 1,
            userFirstName = "Max",
            userLastName = "Rooney",
            userEmailAddress = "Max@gmail.com",
            userPassword = "MaxRon@1",
            userDateOfBirth = 0,
            userImageUri = ""
        )

        viewModel.updateUiState(user)
        assertFalse(viewModel.editProfileUiState.isEntryValid)
    }

    @Test
    fun editProfileViewModel_ValidateInput_NoDefaultAddress_EntryInvalid() {
        val user = User(
            userId = 1,
            userFirstName = "Max",
            userLastName = "Rooney",
            userEmailAddress = "Max@gmail.com",
            userPassword = "MaxRon@1",
            userDateOfBirth = 0,
            userImageUri = ""
        )
        viewModel.updateUiState(user)

        val isValid = viewModel.validateInput(user)

        assertFalse(isValid)
    }

    @Test
    fun editProfileViewModel_UpdateUser_ValidInput_UserUpdateSuccessfully() = runTest {
        val user = User(
            userId = 1,
            userFirstName = "Max",
            userLastName = "Rooney",
            userEmailAddress = "Max@gmail.com",
            userPassword = "MaxRon@123",
            userDateOfBirth = 1723312057492,
            userImageUri = ""
        )
        val address = Address(
            addressId = 1,
            userId = 1,
            addressLineOne = "Street 123",
            addressLineTwo = "City X",
            city = "City X",
            country = "Country Y",
            postalCode = "12345",
            isDefault = true
        )
        userRepository.insertUser(user)
        addressRepository.insertAddress(address)

        advanceTimeBy(3000L)

        viewModel.updateUser()
        advanceUntilIdle()

//        if user is valid and also there is a valid address then it will get updated
        val updatedUser = userRepository.getUserStreamUsingId(1).first()
        assertEquals(user, updatedUser)
    }
}