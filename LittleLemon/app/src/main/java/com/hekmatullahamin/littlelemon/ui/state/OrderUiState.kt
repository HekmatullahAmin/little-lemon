package com.hekmatullahamin.littlelemon.ui.state

import com.hekmatullahamin.littlelemon.ui.state.MenuItemUiState

data class OrderUiState(
    val cartItems: List<MenuItemUiState> = listOf(),
    val subtotal: Double = 0.0
)