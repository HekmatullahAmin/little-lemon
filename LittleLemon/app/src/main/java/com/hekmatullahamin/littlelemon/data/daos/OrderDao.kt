package com.hekmatullahamin.littlelemon.data.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.hekmatullahamin.littlelemon.data.models.Order
import com.hekmatullahamin.littlelemon.data.models.OrderItem
import com.hekmatullahamin.littlelemon.data.models.Side

@Dao
interface OrderDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun saveOrder(order: Order): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAllOrderItems(orderItem: List<OrderItem>): List<Long>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAllSides(sides: List<Side>)
}