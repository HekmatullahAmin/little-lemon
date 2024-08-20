package com.hekmatullahamin.littlelemon.screens

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.lifecycle.SavedStateHandle
import com.hekmatullahamin.littlelemon.R
import com.hekmatullahamin.littlelemon.data.models.Address
import com.hekmatullahamin.littlelemon.data.repositories.AddressRepository
import com.hekmatullahamin.littlelemon.fake.FakeAddressRepository
import com.hekmatullahamin.littlelemon.onAllNodesWithStringId
import com.hekmatullahamin.littlelemon.onNodeWithStringId
import com.hekmatullahamin.littlelemon.ui.navigation.AddressDestination
import com.hekmatullahamin.littlelemon.ui.screens.AddressScreen
import com.hekmatullahamin.littlelemon.ui.screens.AddressViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class AddressScreenUiTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var fakeAddressRepository: AddressRepository
    private lateinit var viewModel: AddressViewModel
    private lateinit var address: Address

    @Before
    fun setup() = runTest {
        fakeAddressRepository = FakeAddressRepository()
        viewModel = AddressViewModel(
            savedStateHandle = SavedStateHandle(mapOf(AddressDestination.userIdArg to 1)),
            addressRepository = fakeAddressRepository
        )
        address =
            Address(addressId = 1, 1, "123 Main St", "Apt 4", "City", "Country", "12345", false)
        viewModel.insertAddress(address)

        composeTestRule.setContent {
            AddressScreen(addressViewModel = viewModel)
        }
    }

    @Test
    fun addressScreen_AddAddressButtonClicked_BottomSheetShown() {
//        Click the add address button
        composeTestRule.onNodeWithStringId(R.string.add_address_button_label).performClick()

//        Verify that the bottom sheet is shown by checking for the bottom sheet title
        composeTestRule.onNodeWithStringId(R.string.add_new_address_title_label).assertIsDisplayed()
    }

    @Test
    fun addressScreen_AddressCardEditButtonClicked_BottomSheetPopulatedWithAddressDetails() {
//            first we click the edit button on Address card
        composeTestRule.onNodeWithStringId(R.string.edit_label).performClick()

//            Verify that the bottom sheet with Edit my address title is shown and the first textField has the addressLineTwo is displayed
        composeTestRule.onNodeWithStringId(R.string.edit_my_address_title_label)
            .assertIsDisplayed()
        composeTestRule.onNodeWithText(address.addressLineTwo).assertIsDisplayed()
    }


    @Test
    fun addressScreen_AddressCardRemoveButtonClicked_AddressRemoved() {
//        Click the remove button on the address card
        composeTestRule.onNodeWithStringId(R.string.remove_text_button_label).performClick()

//        Verify that the address is removed from the list
        composeTestRule.onNodeWithText(address.addressLineTwo).assertDoesNotExist()
    }

    @Test
    fun addressScreen_DefaultAddressLabelDisplayed() {
//        Verify that the "Default Address" label is displayed if is only one address in our list
//        and the "Set as Default" is not displayed
        composeTestRule.onNodeWithStringId(R.string.default_address_label).assertIsDisplayed()
        composeTestRule.onNodeWithStringId(R.string.set_as_default_text_button_label)
            .assertIsNotDisplayed()
    }

    @Test
    fun addressScreen_SetAsDefaultButtonClicked_AddressSetAsDefault() = runTest {
        val secondAddress = Address(
            2,
            1,
            "Mercure Hotel",
            "5-7 Welshback stree",
            "Bristol",
            "England",
            "3210",
            false
        )
        viewModel.insertAddress(secondAddress)

        val thirdAddress = Address(
            3,
            1,
            "Mercure Hotel",
            "5-7 Welshback stree",
            "Bristol",
            "England",
            "3210",
            false
        )
        viewModel.insertAddress(thirdAddress)

//        Initially, the first address should have the "Default Address" label
        composeTestRule.onAllNodesWithStringId(R.string.default_address_label)[0]
            .assertIsDisplayed()

        /*        onAllNodesWithStringId(R.string.set_as_default_text_button_label)[1] fails if
                there are fewer nodes than expected,
                if nodes aren't fully composed,
                or if they aren't visible when the test tries to interact with them.*/

        /*composeTestRule.onAllNodesWithStringId(R.string.default_address_label).fetchSemanticsNodes().size.toString() has size of 1
0 indicates the first node.*/
        /*composeTestRule.onAllNodesWithStringId(R.string.set_as_default_text_button_label).fetchSemanticsNodes().size.toString() has size of 2
0 indicates the first node and 1 indicates the second node.
which in here [0] indicates the second address in UI and [1] indicates to third address in UI */

        //        The second and third address in the UI  should have the "Set as Default" button

        composeTestRule.onAllNodesWithStringId(R.string.set_as_default_text_button_label)[0]
            .assertIsDisplayed()
        composeTestRule.onAllNodesWithStringId(R.string.set_as_default_text_button_label)[1]
            .assertIsDisplayed()

        /*Click the "Set as Default" button on the second address in the UI, which base on onAllNodesWithStringId it is the first node [0]
        and third address in the UI, is node [1] base on onAllNodesWithStringId*/
        composeTestRule.onAllNodesWithStringId(R.string.set_as_default_text_button_label)[0]
            .performClick()

//        we remove the first address in the Ui. base on onAllNodesWithStringId(R.string.remove_text_button_label) it has 3 children having remove button
        composeTestRule.onAllNodesWithStringId(R.string.remove_text_button_label)[0]
            .performClick()

//        now the size of nodes having "Default Address" label and "Set as Default" button label are 1 after removing the first address

//        Verify that the first address is removed
        composeTestRule.onNodeWithText(
            /*            we pass address like this because we set it in this form
                                inside our AddressCard*/
            "${address.addressLineOne}\n" +
                    "${address.addressLineTwo}\n" +
                    "${address.city}, ${address.postalCode}\n" +
                    address.country
        ).assertIsNotDisplayed()

//        Verify that the second address which is now first address after deleting the first address
//        has the "Default Address" label
        composeTestRule.onAllNodesWithStringId(R.string.default_address_label)[0]
            .assertIsDisplayed()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun addressScreen_InsertAddress_NewAddressDisplayed() = runTest {
        val newAddress = Address(
            addressLineOne = "456 Another St",
            addressLineTwo = "Suite 5",
            city = "New City",
            country = "New Country",
            postalCode = "67890"
        )
//        Click the add address button
        composeTestRule.onNodeWithStringId(R.string.add_address_button_label)
            .performClick()

//        Enter new address details
        composeTestRule.onNodeWithStringId(R.string.enter_address_line_one_placeholder)
            .performTextInput(newAddress.addressLineOne)
        composeTestRule.onNodeWithStringId(R.string.enter_address_line_two_placeholder)
            .performTextInput(newAddress.addressLineTwo)
        composeTestRule.onNodeWithStringId(R.string.enter_city_or_town_placeholder)
            .performTextInput(newAddress.city)
        composeTestRule.onNodeWithStringId(R.string.enter_country_placeholder)
            .performTextInput(newAddress.country)
        composeTestRule.onNodeWithStringId(R.string.enter_postal_code_placeholder)
            .performTextInput(newAddress.postalCode)

//        Click the save button
        composeTestRule.onNodeWithStringId(R.string.save_address_button_label)
            .performClick()

        advanceUntilIdle()

//        Verify that the new address is displayed in the list
        composeTestRule.onNodeWithText(
            /*            we pass address like this because we set it in this form
                    inside our AddressCard*/
            "${newAddress.addressLineOne}\n" +
                    "${newAddress.addressLineTwo}\n" +
                    "${newAddress.city}, ${newAddress.postalCode}\n" +
                    newAddress.country
        ).assertIsDisplayed()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun addressScreen_AddressCardEditButtonClicked_AddressUpdated() = runTest {
//        Click the edit button on the address card
        composeTestRule.onNodeWithStringId(R.string.edit_label)
            .performClick()

//        Modify the address details
        composeTestRule.onNodeWithText(address.addressLineTwo)
            .performTextClearance()
//        after clearing the textfield nothing will be remained so empty "" is used here
        val newTextForAddLine2 = "Block 4"
        composeTestRule.onNodeWithText("")
            .performTextInput(newTextForAddLine2)

//        Click the edit button
        composeTestRule.onNodeWithStringId(R.string.edit_address_button_label)
            .performClick()
//        Verify that the updated address is displayed

        advanceUntilIdle()
        address = address.copy(addressLineTwo = newTextForAddLine2)
        composeTestRule.onNodeWithText(
            "${address.addressLineOne}\n" +
                    "${address.addressLineTwo}\n" +
                    "${address.city}, ${address.postalCode}\n" +
                    address.country
        ).assertIsDisplayed()
    }

    @Test
    fun addressScreen_LazyColumnLoaded_AddressesDisplayed() = runTest {
/*        in addition to our first address which got added in the setup function
        we add to other addresses*/
        val addressList = listOf(
            address,
            Address(
                addressId = 2,
                userId = 1,
                "Mercure Hotel",
                "5-7 Welshback stree",
                "Bristol",
                "England",
                "3210",
                false
            ),
            Address(
                addressId = 3,
                userId = 1,
                "Hotel 1",
                "Napy street",
                "Folkestone",
                "England",
                "1234",
                false
            )
        )

        addressList.forEachIndexed { index, address ->
            if (index != 0)
                viewModel.insertAddress(address)
        }

//        Verify that the addresses are displayed in the LazyColumn
        addressList.forEach { address ->
            composeTestRule.onNodeWithText(
                "${address.addressLineOne}\n" +
                        "${address.addressLineTwo}\n" +
                        "${address.city}, ${address.postalCode}\n" +
                        address.country
            ).assertIsDisplayed()
        }
    }
}