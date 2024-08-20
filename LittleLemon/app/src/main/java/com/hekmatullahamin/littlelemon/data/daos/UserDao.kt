package com.hekmatullahamin.littlelemon.data.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.hekmatullahamin.littlelemon.data.models.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUser(user: User)

    @Update
    suspend fun updateUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)

    @Query("SELECT * FROM user WHERE email_address = :userEmail AND password = :userPassword")
    fun getUser(userEmail: String, userPassword: String): Flow<User>

    @Query("SELECT * FROM user WHERE id = :userId")
    fun getUserUsingId(userId: Int): Flow<User>
}