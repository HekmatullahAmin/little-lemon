package com.hekmatullahamin.littlelemon.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MenuNetworkData(
    @SerialName(value = "menu")
    val menuItems: List<MenuItemNetwork>
)
