package com.hekmatullahamin.littlelemon.fake

import com.hekmatullahamin.littlelemon.data.models.MenuItemNetwork
import com.hekmatullahamin.littlelemon.network.NetworkMenuItemsRepository

class FakeNetworkMenuItemsRepository : NetworkMenuItemsRepository {
    override suspend fun getMenuItems(): List<MenuItemNetwork> {
        return FakeDataSource.networkMenuItemsList
    }
}