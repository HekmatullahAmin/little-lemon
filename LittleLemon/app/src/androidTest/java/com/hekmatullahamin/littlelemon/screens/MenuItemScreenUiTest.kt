package com.hekmatullahamin.littlelemon.screens

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.filterToOne
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onChildAt
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.lifecycle.SavedStateHandle
import com.hekmatullahamin.littlelemon.R
import com.hekmatullahamin.littlelemon.data.models.MenuItemRoom
import com.hekmatullahamin.littlelemon.data.repositories.LocalMenuItemsRepository
import com.hekmatullahamin.littlelemon.fake.FakeLocalMenuItemRepository
import com.hekmatullahamin.littlelemon.onAllNodesWithContentDescriptionStringId
import com.hekmatullahamin.littlelemon.onNodeWithContentDescriptionStringId
import com.hekmatullahamin.littlelemon.onNodeWithStringId
import com.hekmatullahamin.littlelemon.ui.navigation.MenuItemDestination
import com.hekmatullahamin.littlelemon.ui.screens.MenuItemScreen
import com.hekmatullahamin.littlelemon.ui.screens.MenuItemViewModel
import com.hekmatullahamin.littlelemon.ui.screens.OptionalSides
import com.hekmatullahamin.littlelemon.ui.screens.RequiredSpiceLevel
import com.hekmatullahamin.littlelemon.ui.screens.Side
import com.hekmatullahamin.littlelemon.ui.screens.SpiceLevel
import com.hekmatullahamin.littlelemon.ui.state.MenuItemUiState
import com.hekmatullahamin.littlelemon.utils.formatAsCurrency
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class MenuItemScreenUiTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private fun setupMenuItemScreen(
        navigateUp: () -> Unit = {},
        onAddToBagButtonClicked: (MenuItemUiState) -> Unit = {},
        fakeLocalMenuItemRepository: LocalMenuItemsRepository = FakeLocalMenuItemRepository(),
        menuItemViewModel: MenuItemViewModel = MenuItemViewModel(
            savedStateHandle = SavedStateHandle(mapOf(MenuItemDestination.menuItemIdArg to 1)),
            localMenuItemsRepository = fakeLocalMenuItemRepository
        )
    ) {
        composeTestRule.setContent {
            MenuItemScreen(
                navigateUp = navigateUp,
                onAddToBagButtonClicked = onAddToBagButtonClicked,
                menuItemViewModel = menuItemViewModel
            )
        }
    }

    @Test
    fun optionalSides_initiallyCollapsed() {
        composeTestRule.setContent {
            OptionalSides(
                selectedSides = listOf(
                    Side.THIN_WHEAT_BREAD,
                    Side.CULT_CHAPATI,
                    Side.LAYERED_FLAT_BREAD
                ),
                onSelectedSide = { _, _ -> }
            )
        }
        composeTestRule.onNodeWithText("Cult Chapati (+ $5.49)").assertDoesNotExist()
    }

    @Test
    fun requiredSpiceLevel_collapseRequiredSpiceLevel_hideRequiredSpiceLevel() {
        composeTestRule.setContent {
            RequiredSpiceLevel(onSelectedSpiceLevel = {})
        }
        composeTestRule.onNodeWithContentDescriptionStringId(R.string.arrow_down_content_description)
            .performClick()
        composeTestRule.onNodeWithText(SpiceLevel.HOT.name).assertIsNotDisplayed()
    }

    @Test
    fun menuItemsScreen_selectSpiceLevel_updatesSelectedSpiceLevel() {
        setupMenuItemScreen()

//        first check that the first radio button is not selected
        composeTestRule.onAllNodesWithTag("radioTag")[0]
            .assertIsNotSelected()
//        click the first radio button out of 3
        composeTestRule.onAllNodesWithTag("radioTag")[0]
            .performClick()
//        verify that the first radio button is selected after click
        composeTestRule.onAllNodesWithTag("radioTag")[0]
            .assertIsSelected()
    }

    @Test
    fun menuItemsScreen_selectSide_updatesSelectedSide() {
        setupMenuItemScreen()
//        There are two Arrow icon with the same content description. So, we perform click in
//        the second one because it is located in "OptionalSides" section
        composeTestRule.onAllNodesWithContentDescriptionStringId(R.string.arrow_down_content_description)[1]
            .performClick()
//        Then we select the second checkbox
        composeTestRule.onAllNodesWithTag("checkboxTag")[1]
            .performClick()
//        Verify that the second checkbox is selected after click
        composeTestRule.onAllNodesWithTag("checkboxTag")[1]
            .assertIsOn()
    }

    @Test
    fun menuItemScreen_spiceLevelSelected_addToCartButtonEnabled() = runTest {
        setupMenuItemScreen(
            fakeLocalMenuItemRepository = FakeLocalMenuItemRepository().apply {
                insertAll(
                    listOf(
                        MenuItemRoom(itemId = 1, itemPrice = 20.0),
                    )
                )
            }
        )

//        click the second radio button out of 3
        composeTestRule.onAllNodesWithTag("radioTag")[1]
            .performClick()
//        Verify that the "add to bag" button is enabled
//        Add to Cart - %s is the button text
        composeTestRule.onNodeWithText("Add to Cart - ${formatAsCurrency(20.0)}")
            .assertIsEnabled()
    }
}