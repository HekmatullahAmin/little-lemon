package com.hekmatullahamin.littlelemon.ui.screens

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hekmatullahamin.littlelemon.data.repositories.LocalMenuItemsRepository
import com.hekmatullahamin.littlelemon.ui.navigation.MenuItemDestination
import com.hekmatullahamin.littlelemon.ui.state.MenuItemUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MenuItemViewModel(
    savedStateHandle: SavedStateHandle,
    private val localMenuItemsRepository: LocalMenuItemsRepository
) : ViewModel() {
    private val menuItemId: Int = checkNotNull(savedStateHandle[MenuItemDestination.menuItemIdArg])
    private var _menuItemUiState: MutableStateFlow<MenuItemUiState> =
        MutableStateFlow(MenuItemUiState())
    val menuItemUiState: StateFlow<MenuItemUiState> = _menuItemUiState.asStateFlow()

    init {
        getMenuItemFromDB()
    }

    private fun getMenuItemFromDB() {
        viewModelScope.launch {
            localMenuItemsRepository.getMenuItem(menuItemId)
                .collectLatest {
                    _menuItemUiState.value = if (it != null) {
                        MenuItemUiState(
                            menuItem = it
                        )
                    } else {
                        MenuItemUiState()
                    }
                }
        }
    }

    fun updateSpiceLevel(spiceLevel: SpiceLevel) {
        _menuItemUiState.value = _menuItemUiState.value.copy(
            spiceLevel = spiceLevel
        )
    }

    fun updateSideSelection(side: Side, isSelected: Boolean) {
        val updateSides = if (isSelected) {
            _menuItemUiState.value.selectedSides + side
        } else {
            _menuItemUiState.value.selectedSides - side
        }
        _menuItemUiState.value = _menuItemUiState.value.copy(selectedSides = updateSides)
    }

    fun updateQuantity(quantity: Int) {
        _menuItemUiState.value = _menuItemUiState.value.copy(quantity = quantity)
    }
}