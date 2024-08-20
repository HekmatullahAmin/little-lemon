package com.hekmatullahamin.littlelemon.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "menu_item")
data class MenuItemRoom(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val itemId: Int = 0,
    @ColumnInfo(name = "title")
    val itemName: String = "",
    @ColumnInfo(name = "price")
    val itemPrice: Double = 0.0,
    @ColumnInfo(name = "description")
    val itemDescription: String = "",
    @ColumnInfo(name = "image")
    val itemImage: String = "",
    @ColumnInfo(name = "category")
    val itemCategory: String = ""
)
