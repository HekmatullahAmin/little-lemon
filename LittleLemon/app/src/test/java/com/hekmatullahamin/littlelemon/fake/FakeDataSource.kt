package com.hekmatullahamin.littlelemon.fake

import com.hekmatullahamin.littlelemon.data.models.MenuItemNetwork
import com.hekmatullahamin.littlelemon.data.models.MenuItemRoom

object FakeDataSource {
    val networkMenuItemsList = listOf(
        MenuItemNetwork(
            itemId = 0,
            itemName = "Rice",
            itemPrice = "20.33",
            itemDescription = "It is really tasty",
            itemCategory = "desserts",
            itemImage = "url1"
        ),
        MenuItemNetwork(
            itemId = 1,
            itemName = "Pepsi",
            itemPrice = "10.33",
            itemDescription = "The pepsi is zero sugar",
            itemCategory = "drinks",
            itemImage = "url2"
        ),
    )

    val localMenuItemsList = listOf(
        MenuItemRoom(
            itemId = 0,
            itemName = "Rice",
            itemPrice = 20.33,
            itemDescription = "It is really tasty",
            itemCategory = "desserts",
            itemImage = "url1"
        ),
        MenuItemRoom(
            itemId = 1,
            itemName = "Pepsi",
            itemPrice = 10.33,
            itemDescription = "The pepsi is zero sugar",
            itemCategory = "drinks",
            itemImage = "url2"
        ),
    )
}