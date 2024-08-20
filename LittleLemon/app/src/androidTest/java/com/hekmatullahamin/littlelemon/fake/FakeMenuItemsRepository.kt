package com.hekmatullahamin.littlelemon.fake

import com.hekmatullahamin.littlelemon.data.models.MenuItemNetwork
import com.hekmatullahamin.littlelemon.data.models.MenuItemRoom
import com.hekmatullahamin.littlelemon.data.repositories.LocalMenuItemsRepository
import com.hekmatullahamin.littlelemon.data.repositories.MenuItemsRepository
import com.hekmatullahamin.littlelemon.network.NetworkMenuItemsRepository
import kotlinx.coroutines.flow.Flow

class FakeMenuItemsRepository(
    private val fakeNetworkMenuItemsRepository: NetworkMenuItemsRepository,
    private val fakeLocalMenuItemsRepository: LocalMenuItemsRepository
) : MenuItemsRepository {
    override suspend fun fetchMenuItems(): List<MenuItemNetwork> {
        return fakeNetworkMenuItemsRepository.getMenuItems()
    }

    override suspend fun insertAll(menuItems: List<MenuItemRoom>) {
        fakeLocalMenuItemsRepository.insertAll(menuItems)
    }

    override fun getAllMenuItems(): Flow<List<MenuItemRoom>?> {
        return fakeLocalMenuItemsRepository.getAllMenuItems()
    }
}