package com.hekmatullahamin.littlelemon.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MenuItemNetwork(
    @SerialName(value = "id")
    val itemId: Int,
    @SerialName(value = "title")
    val itemName: String,
    @SerialName(value = "price")
    val itemPrice: String,
    @SerialName(value = "description")
    val itemDescription: String,
    @SerialName(value = "image")
    val itemImage: String = "",
    @SerialName(value = "category")
    val itemCategory: String = ""
) {
    fun toMenuItemRoom() = MenuItemRoom(
        itemId,
        itemName,
        itemPrice.toDouble(),
        itemDescription,
        itemImage,
        itemCategory
    )
}
