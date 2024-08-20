package com.hekmatullahamin.littlelemon.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "order")
data class Order(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val orderId: Int = 0,
    @ColumnInfo(name = "user_id")
    val userId: Int = 0,
    @ColumnInfo(name = "total_amount")
    val subTotal: Double = 0.0,
    @ColumnInfo(name = "delivery_fee")
    val deliveryFee: Double = 0.0,
    @ColumnInfo(name = "order_date")
    val orderDate: Long = 0L
)
