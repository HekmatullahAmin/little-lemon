package com.hekmatullahamin.littlelemon.data.repositories

import com.hekmatullahamin.littlelemon.data.models.MenuItemRoom
import kotlinx.coroutines.flow.Flow

interface LocalMenuItemsRepository {
    suspend fun insertAll(menuItems: List<MenuItemRoom>)
    fun getAllMenuItems(): Flow<List<MenuItemRoom>?>
    fun getMenuItem(menuItemId: Int): Flow<MenuItemRoom?>
}