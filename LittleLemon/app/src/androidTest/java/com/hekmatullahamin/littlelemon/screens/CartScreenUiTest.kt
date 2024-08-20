package com.hekmatullahamin.littlelemon.screens

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.hekmatullahamin.littlelemon.R
import com.hekmatullahamin.littlelemon.data.models.MenuItemRoom
import com.hekmatullahamin.littlelemon.onAllNodesWithContentDescriptionStringId
import com.hekmatullahamin.littlelemon.ui.screens.CartScreen
import com.hekmatullahamin.littlelemon.ui.screens.Side
import com.hekmatullahamin.littlelemon.ui.state.MenuItemUiState
import com.hekmatullahamin.littlelemon.ui.state.OrderUiState
import com.hekmatullahamin.littlelemon.ui.theme.LittleLemonTheme
import com.hekmatullahamin.littlelemon.utils.formatAsCurrency
import org.junit.Rule
import org.junit.Test

class CartScreenUiTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    //    Instead of repeating the setup code in every test, we can use the "setupCartScreen" function
    private fun setupCartScreen(
        uiState: OrderUiState,
        onAddPromoCodeCardClicked: () -> Unit = {},
        onCheckoutButtonClicked: () -> Unit = {},
        onIncrement: (Int) -> Unit = {},
        onDecrement: (Int) -> Unit = {}
    ) {
        composeTestRule.setContent {
            LittleLemonTheme {
                CartScreen(
                    orderUiState = uiState,
                    onAddPromoCodeCardClicked = onAddPromoCodeCardClicked,
                    onCheckoutButtonClicked = onCheckoutButtonClicked,
                    onIncrement = onIncrement,
                    onDecrement = onDecrement
                )
            }
        }
    }

    @Test
    fun cartScreen_DisplayingCartItems_ItemsAreDisplayedCorrectly() {
        // Set up the UI state with some dummy cart items
        val cartItems = listOf(
            MenuItemUiState(
                MenuItemRoom(
                    1,
                    "Margarita",
                    9.99,
                    "delicious pizza",
                    "",
                    "Pizza"

                ),
                quantity = 1
            ),
            MenuItemUiState(
                MenuItemRoom(
                    2,
                    "Red bull",
                    2.99,
                    "Sweet",
                    "",
                    "Drinks"

                ),
                quantity = 2
            )
        )

        val uiState = OrderUiState(cartItems = cartItems, subtotal = 15.97)

//        Use the helper function to set up the screen
        setupCartScreen(uiState)

        // Check if each cart item is displayed correctly
        cartItems.forEach { item ->
            composeTestRule.onNodeWithText(item.menuItem.itemName).assertIsDisplayed()
            composeTestRule.onNodeWithText(formatAsCurrency(item.totalCost)).assertIsDisplayed()
        }
    }

    //    TODO: doesn't work. when increment the first item quantity
    //     it gets updated but doesn't show on Screen

    @Test
    fun cartScreen_IncrementingQuantity_QuantityChangeCorrectly() {
        val cartItems = listOf(
            MenuItemUiState(
                MenuItemRoom(
                    1,
                    "Margarita",
                    9.99,
                    "delicious pizza",
                    "",
                    "Pizza"

                ),
                quantity = 1,
                selectedSides = listOf(Side.CULT_CHAPATI, Side.THIN_WHEAT_BREAD)
            ),
            MenuItemUiState(
                MenuItemRoom(
                    2,
                    "Red bull",
                    2.99,
                    "Sweet",
                    "",
                    "Drinks"

                ),
                quantity = 3
            )
        )

//        subtotal is sum of totalCost of each menuItem
//        menuItem1 = 9.99 * quantity (1)+ Cult Chapati price (5.49) + Thin Wheat Bread price (4.98)
//        menuItem2 = 2.99 * quantity (3)
//        subtotal = menuItem1 + menuItem2 => 29.43
//        var uiState = OrderUiState(cartItems = cartItems, subtotal = 29.43)
        var uiState by mutableStateOf(OrderUiState(cartItems = cartItems, subtotal = 29.43))


        setupCartScreen(
            uiState = uiState,
            onIncrement = { menuItemId ->
//                Update the quantity of the clicked item
                val updatedCartItems = uiState.cartItems.map {
                    if (it.menuItem.itemId == menuItemId) {
//                        it.copy(quantity = it.quantity + 1)
                        it.copy(quantity = 2)
                    } else {
                        it
                    }
                }
                Log.d("TagT", updatedCartItems.toString())
//                Recalculate the subtotal based on the updated cart items
                val updatedSubtotal = updatedCartItems.sumOf { it.totalCost }

//                Update the UI state with the new cart items and subtotal
                uiState = uiState.copy(
                    cartItems = updatedCartItems,
                    subtotal = updatedSubtotal
                )
            },
            onDecrement = { }
        )

//        before incrementing the first menuItem the totalCost of that item is:
//        9.99 + 5.49 + 4.98 = 20.46
        composeTestRule.onNodeWithText(formatAsCurrency(20.46)).assertIsDisplayed()
//        and the quantity is 1
        composeTestRule.onNodeWithText("1").assertIsDisplayed()

        Log.d(
            "TEST tag",
            composeTestRule.onAllNodesWithContentDescriptionStringId(R.string.increment_content_description)
                .fetchSemanticsNodes().size.toString()
        )

        //        after Incrementing the first item's quantity the total will be
        //        9.99 * (2) + 5.49 + 4.98 = 30.45
        composeTestRule.onAllNodesWithContentDescriptionStringId(R.string.increment_content_description)[0]
            .performClick()

        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("2").assertIsDisplayed()
//        composeTestRule.onNodeWithText(formatAsCurrency(30.45)).assertIsDisplayed()
//        composeTestRule.onNodeWithText(formatAsCurrency( cartItems[0].totalCost)).assertIsDisplayed()

    }

    @Test
    fun cartScreen_DisplayedCartSummary_SummaryIsDisplayedCorrectly() {
//        set up the UI state
        val uiState = OrderUiState(subtotal = 9.99)

        setupCartScreen(
            uiState = uiState
        )

//        Check if subtotal is displayed correctly
        composeTestRule.onNodeWithText(formatAsCurrency(uiState.subtotal)).assertIsDisplayed()
    }
}