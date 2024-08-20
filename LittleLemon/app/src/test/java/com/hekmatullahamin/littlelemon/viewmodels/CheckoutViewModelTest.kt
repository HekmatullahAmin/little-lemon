package com.hekmatullahamin.littlelemon.viewmodels

import androidx.lifecycle.SavedStateHandle
import com.hekmatullahamin.littlelemon.data.models.Address
import com.hekmatullahamin.littlelemon.data.repositories.AddressRepository
import com.hekmatullahamin.littlelemon.fake.FakeAddressRepository
import com.hekmatullahamin.littlelemon.rules.TestDispatcherRule
import com.hekmatullahamin.littlelemon.ui.navigation.CheckoutDestination
import com.hekmatullahamin.littlelemon.ui.screens.CheckoutViewModel
import com.hekmatullahamin.littlelemon.ui.screens.PaymentMethod
import com.hekmatullahamin.littlelemon.utils.Constants
import com.hekmatullahamin.littlelemon.utils.concatenateAddress
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule

@OptIn(ExperimentalCoroutinesApi::class)
class CheckoutViewModelTest {

    @get:Rule
    val testDispatcherRule = TestDispatcherRule()

    private lateinit var fakeAddressRepository: AddressRepository
    private lateinit var viewModel: CheckoutViewModel

    @Before
    fun setup() {
        fakeAddressRepository = FakeAddressRepository()
        viewModel = CheckoutViewModel(
            savedStateHandle = SavedStateHandle(mapOf(CheckoutDestination.userIdArg to 1)),
            addressRepository = fakeAddressRepository
        )
    }

    //    init block
    @Test
    fun checkoutViewModel_Initialization_DefaultAddress_IsSetCorrectly() = runTest {
//        we should pass isDefault to true because when directly calling the repository function it will not our first address to isDefault
//        like addressViewModel so we have to set it manually

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
        //        the initial collection of the address to miss this inserted address.
        fakeAddressRepository.insertAddress(address)

//        By introducing a delay, you gave enough time for the collectLatest operation to process the newly inserted address and update the checkoutUiState.
        advanceTimeBy(1000L)
        assertEquals(concatenateAddress(address), viewModel.checkoutUiState.value.defaultAddress)
    }

    @Test
    fun checkoutViewModel_Initialization_NoDefaultAddress_SetsDefaultToSelectYourAddress() =
        runTest {
            // Given: No addresses in the repository

            // When
            advanceUntilIdle() // Ensure the view model has processed the initialization

            // Then
            assertEquals(
                Constants.SELECT_YOUR_ADDRESS,
                viewModel.checkoutUiState.value.defaultAddress
            )
        }

    //    updatePaymentMethod function
    @Test
    fun checkoutViewModel_UpdatePaymentMethod_ValidCard_UpdatesStateCorrectly() {
        // Given: A valid card payment method
        val validCard = PaymentMethod.Card(
            nameOnCard = "John Doe",
            cardNumber = "1234567812345678",
            expiresDate = "12/24",
            cvv = "123"
        )
        viewModel.updatePaymentMethod(validCard)

        // Then
        assertEquals(
            validCard,
            viewModel.checkoutUiState.value.paymentMethod
        )
    }

    @Test
    fun checkoutViewModel_UpdatePaymentMethod_InvalidCard_DoesNotUpdateState() {
        // Given: A valid card payment method
        val validCard = PaymentMethod.Card(
            nameOnCard = "",
            cardNumber = "",
            expiresDate = "",
            cvv = ""
        )
        viewModel.updatePaymentMethod(validCard)

        // Then
        assertEquals(
            validCard,
            viewModel.checkoutUiState.value.paymentMethod
        )
    }

    @Test
    fun checkoutViewModel_UpdatePaymentMethod_Cash_UpdatesStateCorrectly() {
//        first wanna sure that our default payment method is Card
        assertEquals(
            PaymentMethod.Card(),
            viewModel.checkoutUiState.value.paymentMethod
        )

        val cashPayment = PaymentMethod.Cash
        viewModel.updatePaymentMethod(cashPayment)

//        Then we check that our payment method is changed to cash
        assertEquals(
            cashPayment,
            viewModel.checkoutUiState.value.paymentMethod
        )
    }

    //    validateEntries function
    @Test
    fun checkoutViewModel_ValidateEntries_ValidCard_EntriesAreValid() = runTest {
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
        fakeAddressRepository.insertAddress(address)

        advanceTimeBy(5000L)

        // Given: Valid card details
        val validCard = PaymentMethod.Card(
            nameOnCard = "John Doe",
            cardNumber = "1234567812345678",
            expiresDate = "12/24",
            cvv = "123"
        )
        viewModel.updatePaymentMethod(validCard)

        // Then
        assertTrue(viewModel.checkoutUiState.value.isEntryValid)
    }

    @Test
    fun checkoutViewModel_ValidateEntries_InvalidCard_EntriesAreInvalid() = runTest {
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
        fakeAddressRepository.insertAddress(address)

        advanceTimeBy(1000L)
        // Given: Invalid card details
        val invalidCard = PaymentMethod.Card(
            nameOnCard = "",
            cardNumber = "",
            expiresDate = "",
            cvv = ""
        )
        viewModel.updatePaymentMethod(invalidCard)

        // Then
        assertFalse(viewModel.checkoutUiState.value.isEntryValid)
    }

    @Test
    fun checkoutViewModel_ValidateEntries_CashWithAddress_EntriesAreValid() = runTest {
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
//        we insert this address cause for validation we need it in our viewmodel's validateEntry function
        fakeAddressRepository.insertAddress(address)

        advanceTimeBy(10000L)

        viewModel.updatePaymentMethod(PaymentMethod.Cash)

        assertTrue(viewModel.checkoutUiState.value.isEntryValid)
    }

    @Test
    fun checkoutViewModel_ValidateEntries_CashWithoutAddress_EntriesAreInvalid() {
//        if there is no address or no address is added then our validateEntry function returns false
        viewModel.updatePaymentMethod(PaymentMethod.Cash)
        assertFalse(viewModel.checkoutUiState.value.isEntryValid)
    }
}