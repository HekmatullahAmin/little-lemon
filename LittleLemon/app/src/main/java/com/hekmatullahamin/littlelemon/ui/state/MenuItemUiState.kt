package com.hekmatullahamin.littlelemon.ui.state

import com.hekmatullahamin.littlelemon.data.models.MenuItemRoom
import com.hekmatullahamin.littlelemon.ui.screens.Side
import com.hekmatullahamin.littlelemon.ui.screens.SpiceLevel

data class MenuItemUiState(
    val menuItem: MenuItemRoom = MenuItemRoom(),
    val quantity: Int = 1,
    val spiceLevel: SpiceLevel? = null,
    val selectedSides: List<Side> = emptyList()
) {
    //    we use this variable to enable the visibility of action button and change the text of
//    it accordingly and vice versa
    val isSpiceLevelSelected: Boolean
        get() = spiceLevel != null

    //    we compute the total amount if the number of item increase and if user select sides
    val totalCost: Double
        get() {
            val baseCost = menuItem.itemPrice * quantity
            val sidesCost = selectedSides.sumOf { it.price }
            return baseCost + sidesCost
        }
}
