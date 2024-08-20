package com.hekmatullahamin.littlelemon.viewmodels

import android.view.MenuItem
import androidx.lifecycle.SavedStateHandle
import com.hekmatullahamin.littlelemon.data.models.MenuItemRoom
import com.hekmatullahamin.littlelemon.data.repositories.LocalMenuItemsRepository
import com.hekmatullahamin.littlelemon.fake.FakeLocalMenuItemRepository
import com.hekmatullahamin.littlelemon.rules.TestDispatcherRule
import com.hekmatullahamin.littlelemon.ui.navigation.MenuItemDestination
import com.hekmatullahamin.littlelemon.ui.screens.MenuItemViewModel
import com.hekmatullahamin.littlelemon.ui.screens.Side
import com.hekmatullahamin.littlelemon.ui.screens.SpiceLevel
import com.hekmatullahamin.littlelemon.ui.state.MenuItemUiState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assert.assertFalse
import org.junit.Rule

class MenuItemViewModelTest {
    @get:Rule
    val testDispatcherRule = TestDispatcherRule()
    private lateinit var viewModel: MenuItemViewModel
    private lateinit var fakeLocalMenuItemRepository: LocalMenuItemsRepository

    @Before
    fun setup() {
        fakeLocalMenuItemRepository = FakeLocalMenuItemRepository()
        viewModel = MenuItemViewModel(
            savedStateHandle = SavedStateHandle(mapOf(MenuItemDestination.menuItemIdArg to 1)),
            localMenuItemsRepository = fakeLocalMenuItemRepository
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun menuItemsViewModel_Initialization_MenuItemExists_UpdateUiState() = runTest {
        val menuItems = listOf(
            MenuItemRoom(
                itemId = 0,
                itemName = "Turnip",
                itemCategory = "Vegetables",
                itemImage = "",
                itemDescription = "Very delicious",
                itemPrice = 10.0
            ),
            MenuItemRoom(
                itemId = 1,
                itemName = "Pepsi",
                itemCategory = "Drink",
                itemImage = "",
                itemDescription = "Sugar free",
                itemPrice = 20.0
            )
        )

        fakeLocalMenuItemRepository.insertAll(menuItems)
        //        By introducing a delay, you gave enough time for the collectLatest operation to process the newly inserted menuItems and update the menuItemUiState.
        advanceTimeBy(3000L)

        assertEquals(menuItems[1], viewModel.menuItemUiState.value.menuItem)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun menuItemViewModel_Initialization_MenuItemsDoesNotExists_SetsDefaultUiState() = runTest {
        advanceUntilIdle()
        assertEquals(MenuItemUiState(), viewModel.menuItemUiState.value)
    }

    @Test
    fun menuItemViewModel_UpdateSpiceLevel_ValidSpiceLevel_UpdatesUiState() {
//        we set our spice level to hot
        viewModel.updateSpiceLevel(SpiceLevel.HOT)

//        verify that our spice level is set to HOT
        assertEquals(SpiceLevel.HOT, viewModel.menuItemUiState.value.spiceLevel)
    }

    @Test
    fun menuItemViewModel_UpdateSideSelection_SideSelected_AddSideToUiState() {
        viewModel.updateSideSelection(side = Side.THIN_WHEAT_BREAD, isSelected = true)
//        verify that our side is added
        assertTrue(viewModel.menuItemUiState.value.selectedSides.contains(Side.THIN_WHEAT_BREAD))
    }

    @Test
    fun menuItemViewModel_UpdateSideSelection_SideDeselected_RemovesSideFromUiState() {
        viewModel.updateSideSelection(side = Side.CULT_CHAPATI, isSelected = true)
//        verify that our side is added
        assertTrue(viewModel.menuItemUiState.value.selectedSides.contains(Side.CULT_CHAPATI))

        viewModel.updateSideSelection(side = Side.CULT_CHAPATI, isSelected = false)
//        verify that our side is deselected after it got added
        assertFalse(viewModel.menuItemUiState.value.selectedSides.contains(Side.CULT_CHAPATI))
    }

    @Test
    fun menuItemViewModel_ValidQuantity_UpdatesUiState() {
        viewModel.updateQuantity(2)
        assertTrue(viewModel.menuItemUiState.value.quantity == 2)
    }
}