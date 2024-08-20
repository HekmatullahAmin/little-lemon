package com.hekmatullahamin.littlelemon.data.repositories

import com.hekmatullahamin.littlelemon.data.models.Order
import com.hekmatullahamin.littlelemon.data.models.OrderItem
import com.hekmatullahamin.littlelemon.data.models.Side

interface OrderRepository {
    suspend fun insertOrder(order: Order): Long
    suspend fun insertAllOrderItems(orderItems: List<OrderItem>): List<Long>
    suspend fun insertAllSides(sides: List<Side>)
}