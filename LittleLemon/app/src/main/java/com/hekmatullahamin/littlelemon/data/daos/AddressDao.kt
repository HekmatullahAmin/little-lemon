package com.hekmatullahamin.littlelemon.data.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.hekmatullahamin.littlelemon.data.models.Address
import kotlinx.coroutines.flow.Flow

@Dao
interface AddressDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(address: Address)

    @Update
    suspend fun update(address: Address)

    @Delete
    suspend fun delete(address: Address)

    @Query("SELECT * FROM address WHERE addressId =:addressId")
    fun getAddress(addressId: Int): Flow<Address>

    @Query("SELECT * FROM address WHERE user_id =:userId")
    fun getAllAddresses(userId: Int): Flow<List<Address>>

    //    In SQLite, booleans are typically represented by integers (0 for false and 1 for true).
    @Query("UPDATE address SET is_default = 0 WHERE is_default = 1")
    suspend fun unsetDefaultAddress()

    @Query("UPDATE address SET is_default = 1 WHERE addressId = :addressId")
    suspend fun setDefaultAddress(addressId: Int)

    @Query("SELECT COUNT(*) FROM address WHERE user_id = :userId")
    suspend fun getAddressCount(userId: Int): Int

    @Query("SELECT * FROM address WHERE user_id = :userId AND is_default = 1")
    fun getDefaultAddress(userId: Int): Flow<Address>
}