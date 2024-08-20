package com.hekmatullahamin.littlelemon.ui.state

import com.hekmatullahamin.littlelemon.data.models.MenuItemRoom

sealed interface HomeUiState {
    data class Success(val menuItems: List<MenuItemRoom>) : HomeUiState
    data object Error : HomeUiState
    data object Loading : HomeUiState
}