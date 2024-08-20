package com.hekmatullahamin.littlelemon.screens

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.lifecycle.SavedStateHandle
import com.hekmatullahamin.littlelemon.R
import com.hekmatullahamin.littlelemon.data.repositories.LocalMenuItemsRepository
import com.hekmatullahamin.littlelemon.data.repositories.UserRepository
import com.hekmatullahamin.littlelemon.fake.FakeDataSource
import com.hekmatullahamin.littlelemon.fake.FakeLocalMenuItemRepository
import com.hekmatullahamin.littlelemon.fake.FakeMenuItemsRepository
import com.hekmatullahamin.littlelemon.fake.FakeNetworkMenuItemsRepository
import com.hekmatullahamin.littlelemon.fake.FakeUserRepository
import com.hekmatullahamin.littlelemon.network.NetworkMenuItemsRepository
import com.hekmatullahamin.littlelemon.onNodeWithStringId
import com.hekmatullahamin.littlelemon.ui.navigation.HomeDestination
import com.hekmatullahamin.littlelemon.ui.screens.HomeScreen
import com.hekmatullahamin.littlelemon.ui.screens.HomeScreenViewModel
import com.hekmatullahamin.littlelemon.ui.screens.MenuSectionsButtons
import com.hekmatullahamin.littlelemon.ui.theme.LittleLemonTheme
import com.hekmatullahamin.littlelemon.utils.formatAsCurrency
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class HomeScreenUiTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private fun setupHomeScreen(
        onProfilePictureClicked: () -> Unit = {},
        onMenuItemClicked: (Int) -> Unit = {},
        onCartCardClicked: () -> Unit = {},
        userRepository: UserRepository = FakeUserRepository(),
        localMenuItemsRepository: LocalMenuItemsRepository = FakeLocalMenuItemRepository(),
        networkMenuItemsRepository: NetworkMenuItemsRepository = FakeNetworkMenuItemsRepository(),
        homeScreenViewModel: HomeScreenViewModel = HomeScreenViewModel(
            savedStateHandle = SavedStateHandle(mapOf(HomeDestination.userIdArg to 1)),
            userRepository = userRepository,
            menuItemsRepository = FakeMenuItemsRepository(
                fakeNetworkMenuItemsRepository = networkMenuItemsRepository,
                fakeLocalMenuItemsRepository = localMenuItemsRepository
            )
        )
    ) {
        composeTestRule.setContent {
            LittleLemonTheme {
                HomeScreen(
                    onProfilePictureClicked = onProfilePictureClicked,
                    onMenuItemClicked = onMenuItemClicked,
                    onCartCardClicked = onCartCardClicked,
                    homeScreenViewModel = homeScreenViewModel
                )
            }
        }
    }

    //    we used FakeDataSource.localMenuItemsList because our fakeNetworkMenuItemsRepository
//    return this list and after that fakeLocalMenuItemsRepository insert all of them
    @Test
    fun homeScreen_displaysMenuItemsFetchedFromServerSavedInDB() {
        setupHomeScreen()
        FakeDataSource.localMenuItemsList.forEach { menuItem ->
            composeTestRule.onNodeWithText(menuItem.itemName)
                .assertIsDisplayed()
            composeTestRule.onNodeWithText(menuItem.itemDescription).isDisplayed()
            composeTestRule.onNodeWithText(formatAsCurrency(menuItem.itemPrice)).assertIsDisplayed()
        }
    }


    @Test
    fun homeScreen_displaySearchResults() {
        setupHomeScreen()
//        Enter a search phrase
        composeTestRule.onNodeWithStringId(R.string.enter_item_name_placeholder)
            .performTextInput(FakeDataSource.localMenuItemsList[0].itemName)
//        Assert that the filtered menu item is displayed(here we check the item description)
//        FakeDataSource.localMenuItemsList[0].itemName = "Rice"
        composeTestRule.onNodeWithText(FakeDataSource.localMenuItemsList[0].itemDescription)
            .assertIsDisplayed()
    }

    @Test
    fun menuSectionsButtons_displaysCategories_andRespondToClick() {
        val categories = listOf("Appetizers", "Desserts", "Beverages")
        var selectedCategory: String? = null
        composeTestRule.setContent {
            MenuSectionsButtons(
                categories = categories,
                onCategoryClicked = { selectedCategory = it }
            )
        }

//        Assert that all categories are displayed
        categories.forEach {category ->
            composeTestRule.onNodeWithText(category).assertIsDisplayed()
        }

//        Click on the "Desserts" category
        composeTestRule.onNodeWithText("Desserts").performClick()

//        Assert that the correct category was selected
        assertEquals("Desserts", selectedCategory)
    }
}