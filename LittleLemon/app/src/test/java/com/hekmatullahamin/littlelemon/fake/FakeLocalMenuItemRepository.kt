package com.hekmatullahamin.littlelemon.fake

import com.hekmatullahamin.littlelemon.data.models.MenuItemRoom
import com.hekmatullahamin.littlelemon.data.models.User
import com.hekmatullahamin.littlelemon.data.repositories.LocalMenuItemsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class FakeLocalMenuItemRepository : LocalMenuItemsRepository {

    private val menuItemsList = mutableListOf<MenuItemRoom>()
    private val menuItemsFlow = MutableStateFlow<List<MenuItemRoom>>(emptyList())

    override suspend fun insertAll(menuItems: List<MenuItemRoom>) {
        menuItemsList.addAll(menuItems)
        menuItemsFlow.update { menuItemsList.toList() }
    }

    override fun getAllMenuItems(): Flow<List<MenuItemRoom>?> {
        return menuItemsFlow.map { it.toList() }
    }

    override fun getMenuItem(menuItemId: Int): Flow<MenuItemRoom?> {
        return menuItemsFlow.map {
            it.find { menuItem ->
                menuItem.itemId == menuItemId
            }
        }
    }
}