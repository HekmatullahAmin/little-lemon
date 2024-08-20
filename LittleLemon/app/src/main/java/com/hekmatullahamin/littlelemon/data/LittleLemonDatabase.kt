package com.hekmatullahamin.littlelemon.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.hekmatullahamin.littlelemon.data.daos.AddressDao
import com.hekmatullahamin.littlelemon.data.daos.MenuItemDao
import com.hekmatullahamin.littlelemon.data.daos.OrderDao
import com.hekmatullahamin.littlelemon.data.daos.UserDao
import com.hekmatullahamin.littlelemon.data.models.Address
import com.hekmatullahamin.littlelemon.data.models.MenuItemRoom
import com.hekmatullahamin.littlelemon.data.models.Order
import com.hekmatullahamin.littlelemon.data.models.OrderItem
import com.hekmatullahamin.littlelemon.data.models.Side
import com.hekmatullahamin.littlelemon.data.models.User

@Database(
    entities = [User::class, MenuItemRoom::class, Address::class,
        Order::class, OrderItem::class, Side::class],
    version = 1,
    exportSchema = false
)
abstract class LittleLemonDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun menuItemDao(): MenuItemDao
    abstract fun addressDao(): AddressDao
    abstract fun orderDao(): OrderDao

    companion object {
        private var Instance: LittleLemonDatabase? = null
        fun getDatabase(context: Context): LittleLemonDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    LittleLemonDatabase::class.java,
                    "little_lemon_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}