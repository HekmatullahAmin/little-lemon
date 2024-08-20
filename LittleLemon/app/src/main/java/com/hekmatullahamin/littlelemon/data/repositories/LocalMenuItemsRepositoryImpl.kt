package com.hekmatullahamin.littlelemon.data.repositories

import com.hekmatullahamin.littlelemon.data.daos.MenuItemDao
import com.hekmatullahamin.littlelemon.data.models.MenuItemRoom
import kotlinx.coroutines.flow.Flow

class LocalMenuItemsRepositoryImpl(private val menuItemDao: MenuItemDao) :
    LocalMenuItemsRepository {
    override suspend fun insertAll(menuItems: List<MenuItemRoom>) {
        menuItemDao.insertAll(menuItems)
    }

    override fun getAllMenuItems(): Flow<List<MenuItemRoom>?> = menuItemDao.getAllMenuItems()
    override fun getMenuItem(menuItemId: Int): Flow<MenuItemRoom?> = menuItemDao.getMenuItem(menuItemId)
}