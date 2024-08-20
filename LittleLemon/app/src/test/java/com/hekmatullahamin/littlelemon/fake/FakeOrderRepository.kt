package com.hekmatullahamin.littlelemon.fake

import com.hekmatullahamin.littlelemon.data.models.Order
import com.hekmatullahamin.littlelemon.data.models.OrderItem
import com.hekmatullahamin.littlelemon.data.models.Side
import com.hekmatullahamin.littlelemon.data.repositories.OrderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class FakeOrderRepository : OrderRepository {
    private val orders = mutableListOf<Order>()
    private val orderItems = mutableListOf<OrderItem>()
    private val sides = mutableListOf<Side>()

    private val ordersFlow = MutableStateFlow<List<Order>>(emptyList())
    private val orderItemsFlow = MutableStateFlow<List<OrderItem>>(emptyList())
    private val sidesFlow = MutableStateFlow<List<Side>>(emptyList())

    override suspend fun insertOrder(order: Order): Long {
        orders.add(order)
        ordersFlow.update { orders.toList() }
        return order.orderId.toLong()
    }

    override suspend fun insertAllOrderItems(orderItems: List<OrderItem>): List<Long> {
        this.orderItems.addAll(orderItems)
        orderItemsFlow.update { this.orderItems.toList() }
        return orderItems.map { it.orderItemId.toLong() }
    }

    override suspend fun insertAllSides(sides: List<Side>) {
        this.sides.addAll(sides)
        sidesFlow.update { this.sides.toList() }
    }

    // Add other functions that you might need for testing or fetching data

    fun getOrderStream(orderId: Long): Flow<Order?> {
        return ordersFlow.map { orderList ->
            orderList.find { it.orderId.toLong() == orderId }
        }
    }

    fun getOrderItemsStream(orderId: Long): Flow<List<OrderItem>> {
        return orderItemsFlow.map { itemList ->
            itemList.filter { it.orderId.toLong() == orderId }
        }
    }

    fun getSidesStream(orderItemId: Long): Flow<List<Side>> {
        return sidesFlow.map { sideList ->
            sideList.filter { it.orderItemId.toLong() == orderItemId }
        }
    }
}