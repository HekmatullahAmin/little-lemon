package com.hekmatullahamin.littlelemon.screens

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.hekmatullahamin.littlelemon.ui.screens.OrderCompleteScreen
import com.hekmatullahamin.littlelemon.utils.formatAsCurrency
import org.junit.Rule
import org.junit.Test

class OrderCompleteUiTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun orderCompleteScreen_displayTotalAmountCorrectly() {
        composeTestRule.setContent {
            OrderCompleteScreen(totalAmount = 75.50, onOrderAgainButtonClicked = { })
        }

//        Assert that the total amount is displayed correctly
//        Paid %s
        composeTestRule.onNodeWithText("Paid ${formatAsCurrency(75.50)}")
            .assertIsDisplayed()
    }
}