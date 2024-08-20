package com.hekmatullahamin.littlelemon.network

import com.hekmatullahamin.littlelemon.data.models.MenuItemNetwork
import com.hekmatullahamin.littlelemon.data.models.MenuNetworkData
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

interface NetworkMenuItemsRepository {
    suspend fun getMenuItems(): List<MenuItemNetwork>
}

class NetworkMenuItemsRepositoryImpl(private val httpClient: HttpClient) :
    NetworkMenuItemsRepository {
    override suspend fun getMenuItems(): List<MenuItemNetwork> {
        val baseUrl =
            "https://raw.githubusercontent.com/Meta-Mobile-Developer-PC/Working-With-Data-API/main/menu.json"

        val response: MenuNetworkData =
            httpClient.get(baseUrl)
                .body()
        return response.menuItems
    }
}