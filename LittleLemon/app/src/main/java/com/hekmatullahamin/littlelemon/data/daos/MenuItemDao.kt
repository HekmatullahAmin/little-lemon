package com.hekmatullahamin.littlelemon.data.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hekmatullahamin.littlelemon.data.models.MenuItemRoom
import kotlinx.coroutines.flow.Flow

@Dao
interface MenuItemDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(menuItems: List<MenuItemRoom>)

    @Query("SELECT * FROM menu_item")
    fun getAllMenuItems(): Flow<List<MenuItemRoom>>

    @Query("SELECT * FROM menu_item WHERE id = :menuItemId")
    fun getMenuItem(menuItemId: Int): Flow<MenuItemRoom>
}