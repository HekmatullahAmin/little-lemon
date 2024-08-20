package com.hekmatullahamin.littlelemon.data.repositories

import com.hekmatullahamin.littlelemon.data.models.MenuItemNetwork
import com.hekmatullahamin.littlelemon.data.models.MenuItemRoom
import com.hekmatullahamin.littlelemon.network.NetworkMenuItemsRepository
import kotlinx.coroutines.flow.Flow

class MenuItemsRepositoryImpl(
    private val networkMenuItemsRepository: NetworkMenuItemsRepository,
    private val localMenuItemsRepository: LocalMenuItemsRepository
) : MenuItemsRepository {
    override suspend fun fetchMenuItems(): List<MenuItemNetwork> =
        networkMenuItemsRepository.getMenuItems()

    override suspend fun insertAll(menuItems: List<MenuItemRoom>) =
        localMenuItemsRepository.insertAll(menuItems)

    override fun getAllMenuItems(): Flow<List<MenuItemRoom>?> =
        localMenuItemsRepository.getAllMenuItems()
}