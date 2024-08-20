package com.hekmatullahamin.littlelemon.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "order_item",
    foreignKeys = [
        ForeignKey(
            entity = Order::class,
            parentColumns = ["id"],
            childColumns = ["order_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = MenuItemRoom::class,
            parentColumns = ["id"],
            childColumns = ["menu_item_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class OrderItem(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val orderItemId: Int = 0,
    @ColumnInfo(name = "order_id")
    val orderId: Int = 0,
    @ColumnInfo(name = "menu_item_id")
    val menuItemId: Int = 0,
    val quantity: Int = 1,
    @ColumnInfo("spice_level")
    val spiceLevel: String = ""
)
