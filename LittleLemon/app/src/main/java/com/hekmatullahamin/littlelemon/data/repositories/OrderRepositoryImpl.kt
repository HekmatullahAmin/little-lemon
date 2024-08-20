package com.hekmatullahamin.littlelemon.data.repositories

import com.hekmatullahamin.littlelemon.data.daos.OrderDao
import com.hekmatullahamin.littlelemon.data.models.Order
import com.hekmatullahamin.littlelemon.data.models.OrderItem
import com.hekmatullahamin.littlelemon.data.models.Side

class OrderRepositoryImpl(private val orderDao: OrderDao) : OrderRepository {
    override suspend fun insertOrder(order: Order): Long =
        orderDao.saveOrder(order)

    override suspend fun insertAllOrderItems(orderItems: List<OrderItem>): List<Long> =
        orderDao.insertAllOrderItems(orderItems)

    override suspend fun insertAllSides(sides: List<Side>) =
        orderDao.insertAllSides(sides)

}