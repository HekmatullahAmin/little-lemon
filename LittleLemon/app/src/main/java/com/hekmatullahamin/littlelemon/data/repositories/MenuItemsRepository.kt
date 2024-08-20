package com.hekmatullahamin.littlelemon.data.repositories

import com.hekmatullahamin.littlelemon.data.models.MenuItemNetwork
import com.hekmatullahamin.littlelemon.data.models.MenuItemRoom
import kotlinx.coroutines.flow.Flow

interface MenuItemsRepository {
    suspend fun fetchMenuItems(): List<MenuItemNetwork>
    suspend fun insertAll(menuItems: List<MenuItemRoom>)
    fun getAllMenuItems(): Flow<List<MenuItemRoom>?>
}

