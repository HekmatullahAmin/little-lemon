package com.hekmatullahamin.littlelemon.screens

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.isNotDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.lifecycle.SavedStateHandle
import com.hekmatullahamin.littlelemon.R
import com.hekmatullahamin.littlelemon.data.models.Address
import com.hekmatullahamin.littlelemon.fake.FakeAddressRepository
import com.hekmatullahamin.littlelemon.onNodeWithStringId
import com.hekmatullahamin.littlelemon.ui.navigation.CheckoutDestination
import com.hekmatullahamin.littlelemon.ui.screens.BillingSummary
import com.hekmatullahamin.littlelemon.ui.screens.CheckoutScreen
import com.hekmatullahamin.littlelemon.ui.screens.CheckoutViewModel
import com.hekmatullahamin.littlelemon.ui.screens.PaymentMethod
import com.hekmatullahamin.littlelemon.ui.screens.PaymentMethodCard
import com.hekmatullahamin.littlelemon.ui.theme.LittleLemonTheme
import com.hekmatullahamin.littlelemon.utils.Constants
import com.hekmatullahamin.littlelemon.utils.formatAsCurrency
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

/*Information

assertDoesNotExist():
This checks whether a particular node (UI element) is entirely absent from the Compose tree. If the element was never created, or if it was removed due to some state change, this assertion will pass.
Use this when you expect the UI element to be completely removed from the UI structure.

assertIsNotDisplayed():
This checks whether a node is present in the Compose tree but is not currently visible on the screen. The element might still exist in the hierarchy but might be invisible due to various reasons (e.g., hidden by another element, off-screen, or set to invisible).
Use this when you expect the UI element to still exist but not be visible to the user.

In practice, when you expect the element to be removed entirely, use assertDoesNotExist(). When you expect the element to still be part of the UI structure but not visible, use assertIsNotDisplayed().*/

/*assertIsDisplayed():
assertIsDisplayed() checks whether a specific UI element (node) exists in the current Compose tree and is visible to the user on the screen.
Usage Context:
Use assertIsDisplayed() when you expect the element to not only be part of the UI hierarchy but also visible on the screen. This assertion is useful for ensuring that the element is correctly rendered and visible to the user.

assertExist():
assertExists() checks whether a specific UI element (node) exists in the current Compose tree, regardless of whether it is visible on the screen.
Usage Context:
Use assertExists() when you expect the element to be part of the UI hierarchy, even if it might not be currently visible. This assertion is useful for ensuring that the element is present in the UI tree but does not necessarily need to be displayed.*/

class CheckoutScreenUiTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private fun setupCheckoutScreen(
        onSelectedAddressClicked: () -> Unit = {},
        subtotal: Double = 0.0,
        onPayNowButtonClicked: () -> Unit = {},
        checkoutViewModel: CheckoutViewModel = CheckoutViewModel(
            savedStateHandle = SavedStateHandle(mapOf(CheckoutDestination.userIdArg to 1)),
            addressRepository = FakeAddressRepository()
        )
    ) {
        composeTestRule.setContent {
            LittleLemonTheme {
                CheckoutScreen(
                    onSelectAddressClicked = onSelectedAddressClicked,
                    subtotal = subtotal,
                    onPayNowButtonClicked = onPayNowButtonClicked,
                    checkoutViewModel = checkoutViewModel
                )
            }
        }
    }

    //    Verify that the checkout screen renders correctly with the default payment method (Card)
    @Test
    fun checkoutScreen_rendersCorrectly_withDefaultPaymentMethod() {
        setupCheckoutScreen(
            subtotal = 100.0
        )

//        Check if the "Pay by card" radio button is selected by default
        composeTestRule.onNodeWithTag("Method Card").assertIsSelected()
    }

    /*    Verify that the "Pay Now" button is enabled when all required card fields are filled
        and an address is provided*/
    @Test
    fun checkoutScreen_payNowButtonEnabled_whenCardDetailsAndAddressProvided() = runTest {
        setupCheckoutScreen(
            checkoutViewModel = CheckoutViewModel(
                savedStateHandle = SavedStateHandle(mapOf(CheckoutDestination.userIdArg to 1)),
                addressRepository = FakeAddressRepository().apply {
                    insertAddress(
                        Address(
                            addressId = 1, userId = 1, addressLineOne = "Street 1",
                            addressLineTwo = "Building 2", city = "London",
                            country = "England", postalCode = "0000", isDefault = true
                        )
                    )
                }
            )
        )

        composeTestRule.onNodeWithTag("Method Card").performClick()

//        Fill card details
        composeTestRule.onNodeWithStringId(R.string.name_on_card_placeholder)
            .performTextInput("Hekmat")
        composeTestRule.onNodeWithStringId(R.string.card_number_placeholder)
            .performTextInput("1234567890123456")
        composeTestRule.onNodeWithStringId(R.string.expires_end_placeholder)
            .performTextInput("12/25")
        composeTestRule.onNodeWithStringId(R.string.cvv_code_placeholder)
            .performTextInput("123")

//        Check if the Pay Now button is enabled
        composeTestRule.onNodeWithStringId(R.string.pay_now_button_label).assertIsEnabled()
    }

    //    Verify that the "Pay Now" button is disabled when required fields are missing
    @Test
    fun checkoutScreen_payNowButtonDisabled_whenAddressIsMissingInPayByCash() {
        setupCheckoutScreen()

        composeTestRule.onNodeWithTag("Method Cash")
            .performClick()

//        No address was inserted so the address field will be "Select your address" and button will be disabled
        composeTestRule.onNodeWithText(Constants.SELECT_YOUR_ADDRESS).assertIsDisplayed()
//        Check if the Pay Now button is disabled
        composeTestRule.onNodeWithStringId(R.string.pay_now_button_label).assertIsNotEnabled()
    }

    //    Verify that the delivery fee is calculated correctly as 10% of the subtotal
    @Test
    fun billingSummary_deliveryFeeCalculatedCorrectly() {
        val subtotal = 200.0
        composeTestRule.setContent {
            LittleLemonTheme {
                BillingSummary(
                    deliveryAddress = "123 Main Street",
                    onSelectAddressClicked = { },
                    subtotal = subtotal,
                    deliveryFee = subtotal * 0.10
                )
            }
        }

//        Check if the delivery fee text is correctly displayed
        composeTestRule.onNodeWithText(formatAsCurrency(20.0)).assertExists()
    }

    //    Verify that the address selection opens the correct UI when clicked
    @Test
    fun billingSummary_addressSelectionOpensCorrectUI() {
        var addressSelectionClicked = false
        composeTestRule.setContent {
            LittleLemonTheme {
                BillingSummary(
                    deliveryAddress = "123 Main Street",
                    onSelectAddressClicked = { addressSelectionClicked = true },
                    subtotal = 100.0,
                    deliveryFee = 10.0
                )
            }
        }

//        Perform click on the delivery address text
        composeTestRule.onNodeWithText("123 Main Street").performClick()

//        Check if the address selection was triggered
        assert(addressSelectionClicked)
    }

    //    Verify that switching between payment methods updates the UI accordingly
    @Test
    fun paymentMethodCard_switchingBetweenMethods_updatesUI() {
        setupCheckoutScreen()

//        Initially, the card fields should be displayed
        composeTestRule.onNodeWithStringId(R.string.name_on_card_placeholder).assertIsDisplayed()
        composeTestRule.onNodeWithStringId(R.string.card_number_placeholder).assertIsDisplayed()

//        Switch to Cash payment method
        composeTestRule.onNodeWithTag("Method Cash").performClick()
//        Check if card fields are hidden
        composeTestRule.onNodeWithStringId(R.string.name_on_card_placeholder).assertIsNotDisplayed()
    }

    //    Verify that the total is correctly calculated as the sum of subtotal and delivery fee
    @Test
    fun billingSummary_totalCalculatedCorrectly() {
        val subtotal = 200.0
        composeTestRule.setContent {
            LittleLemonTheme {
                BillingSummary(
                    deliveryAddress = "123 Main Street",
                    onSelectAddressClicked = { },
                    subtotal = subtotal,
                    deliveryFee = 20.0
                )
            }
        }

//            Check if the total text is correctly displayed
        composeTestRule.onNodeWithText(formatAsCurrency(220.0)).assertExists()
    }
}