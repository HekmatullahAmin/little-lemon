package com.hekmatullahamin.littlelemon.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

//if an OrderItem is deleted, all Side entries that reference this OrderItem
// will also be deleted automatically.
@Entity(
    tableName = "side",
    foreignKeys = [
        ForeignKey(
            entity = OrderItem::class,
            parentColumns = ["id"],
            childColumns = ["order_item_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Side(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val sideId: Int = 0,
    @ColumnInfo(name = "order_item_id")
    val orderItemId: Int = 0,
    @ColumnInfo("side_name")
    val sideName: String = "",
    @ColumnInfo(name = "price")
    val sidePrice: Double = 0.0
)
