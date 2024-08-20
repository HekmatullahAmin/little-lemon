package com.hekmatullahamin.littlelemon.viewmodels

import androidx.lifecycle.SavedStateHandle
import com.hekmatullahamin.littlelemon.data.models.Address
import com.hekmatullahamin.littlelemon.data.repositories.AddressRepository
import com.hekmatullahamin.littlelemon.fake.FakeAddressRepository
import com.hekmatullahamin.littlelemon.rules.TestDispatcherRule
import com.hekmatullahamin.littlelemon.ui.navigation.AddressDestination
import com.hekmatullahamin.littlelemon.ui.screens.AddressViewModel
import com.hekmatullahamin.littlelemon.ui.state.AddressUiState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class AddressViewModelTest {
    @get:Rule
    val testDispatcherRule = TestDispatcherRule()

    private lateinit var viewModel: AddressViewModel
    private lateinit var fakeAddressRepository: AddressRepository

    @Before
    fun setup() {
        fakeAddressRepository = FakeAddressRepository()
        viewModel = AddressViewModel(
            savedStateHandle = SavedStateHandle(mapOf(AddressDestination.userIdArg to 1)),
            addressRepository = fakeAddressRepository
        )
    }

    @Test
    fun addressViewModel_Initialization_LoadingState() = runTest {
        assertEquals(AddressUiState.Loading, viewModel.addressUiState.value)
    }

    @Test
    fun addressViewModel_FetchAllAddresses_SuccessStateWithCorrectData() = runTest {
//        if there is address then it will return Success otherwise Error cause it is empty
//        so we should add an address before testing
        val address = Address(
            addressId = 1,
            userId = 1,
            addressLineOne = "Street 123",
            addressLineTwo = "City X",
            city = "City X",
            country = "Country Y",
            postalCode = "12345",
            isDefault = false // Initially false, but should be set to true
        )
        viewModel.insertAddress(address)
        advanceUntilIdle()
        assertTrue(viewModel.addressUiState.value is AddressUiState.Success)
        assertEquals(
            AddressUiState.Success(
                fakeAddressRepository.getAllAddresses(userId = 1).first()!!
            ),
//            AddressUiState.Success(listOf( address.copy(isDefault = true))),
            viewModel.addressUiState.value
        )

    }

    @Test
    fun addressViewModel_FetchAllAddress_EmptyListResultInErrorState() = runTest {
        advanceUntilIdle()
        assertEquals(AddressUiState.Error, viewModel.addressUiState.value)
    }

    @Test
    fun addressViewModel_InsertFirstAddress_ValidateInput_IsDefaultTrue() = runTest {
        val firstAddress = Address(
            addressId = 1,
            userId = 1,
            addressLineOne = "Street 123",
            addressLineTwo = "City X",
            city = "City X",
            country = "Country Y",
            postalCode = "12345",
            isDefault = false // Initially false, but should be set to true
        )

//        insert the first address
        viewModel.insertAddress(firstAddress)

//        Verify that the first address has isDefault set to true
        assertEquals(
            firstAddress.copy(isDefault = true),
//            This function collects the first value emitted by a Flow and then cancels the collection.
//            It works perfectly when you're interested in the initial or most recent state of a Flow that might not emit continuously or have an explicit end.
            fakeAddressRepository.getAddressStream(1).first()
        )
    }

    @Test
    fun addressViewModel_InsertSecondAddress_ValidateInput_IsDefaultFalse() = runTest {
        val firstAddress = Address(
            addressId = 1,
            userId = 1,
            addressLineOne = "Street 123",
            addressLineTwo = "City X",
            city = "City X",
            country = "Country Y",
            postalCode = "12345",
            isDefault = false // Initially false, but should be set to true
        )

        val secondAddress = Address(
            addressId = 2,
            userId = 1,
            addressLineOne = "Avenue 456",
            addressLineTwo = "City Y",
            city = "City Y",
            country = "Country Z",
            postalCode = "67890",
            isDefault = false // Should remain false
        )

//        insert the first address
        viewModel.insertAddress(firstAddress)
        viewModel.insertAddress(secondAddress)

//        Verify that the first address has isDefault set to true
        assertEquals(
            firstAddress.copy(isDefault = true),
            fakeAddressRepository.getAddressStream(1).first()
        )

//        Verify that the second address has isDefault set to false
        assertEquals(
            secondAddress.copy(isDefault = false),
            fakeAddressRepository.getAddressStream(2).first()
        )
    }

    @Test
    fun addressViewModel_UpdateAddress_ValidInput_UpdatesAddressSuccessfully() = runTest {
        val address = Address(
            addressId = 1,
            userId = 1,
            addressLineOne = "Street 123",
            addressLineTwo = "City X",
            city = "City X",
            country = "Country Y",
            postalCode = "12345",
            isDefault = false // Initially false, but should be set to true
        )
        viewModel.insertAddress(address)
        viewModel.updateAddress(address.copy(addressLineOne = "Street 4"))
//        Verify that our address updated successfully
        assertEquals(
            address.copy(addressLineOne = "Street 4"),
            fakeAddressRepository.getAddressStream(1).first()
        )
    }

    @Test
    fun addressViewModel_SetDefaultAddress_SetDefaultSuccessfully() = runTest {
        val firstAddress = Address(
            addressId = 1,
            userId = 1,
            addressLineOne = "Street 123",
            addressLineTwo = "City X",
            city = "City X",
            country = "Country Y",
            postalCode = "12345",
            isDefault = false // Initially false, but should be set to true
        )

        val secondAddress = Address(
            addressId = 2,
            userId = 1,
            addressLineOne = "Avenue 456",
            addressLineTwo = "City Y",
            city = "City Y",
            country = "Country Z",
            postalCode = "67890",
            isDefault = false // Should remain false
        )

        viewModel.insertAddress(firstAddress)
        viewModel.insertAddress(secondAddress)

        viewModel.setDefaultAddress(secondAddress)

        val firstAddressFromDB =
            fakeAddressRepository.getAddressStream(firstAddress.addressId).first()
        val secondAddressFromDB =
            fakeAddressRepository.getAddressStream(secondAddress.addressId).first()

//        Verify that our first address is set to false
        assertTrue(!firstAddressFromDB!!.isDefault)
//        Verify that our second address is our default address and it's isDefault is set to true
        assertTrue(secondAddressFromDB!!.isDefault)
    }

    @Test
    fun addressViewModel_DeleteAddress_DeleteSuccessfully() = runTest {
        val address = Address(
            addressId = 1,
            userId = 1,
            addressLineOne = "Street 123",
            addressLineTwo = "City X",
            city = "City X",
            country = "Country Y",
            postalCode = "12345",
            isDefault = false // Initially false, but should be set to true
        )
        viewModel.insertAddress(address)
//        We first verify that the address is inserted
        assertEquals(
            address.copy(isDefault = true),
            fakeAddressRepository.getAddressStream(1).first()
        )
        viewModel.deleteAddress(address)
//        Verify that there is no address after deleting that specific address
        assertTrue(fakeAddressRepository.getAddressCount(1) == 0)
    }
}