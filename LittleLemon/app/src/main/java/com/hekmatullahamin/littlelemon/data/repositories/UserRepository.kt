package com.hekmatullahamin.littlelemon.data.repositories

import com.hekmatullahamin.littlelemon.data.models.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun insertUser(user: User)
    suspend fun updateUser(user: User)
    suspend fun deleteUser(user: User)

    //    why we user ? in Flow<User?>
    //    getUserStream doesn't necessarily return the user data immediately. It might involve querying the database asynchronously, and the user data might be emitted later.
//    Or
//    The ? after User indicates that the Flow might emit null values. This is because the user with the provided credentials might not exist in the database.
    fun getUserStream(userEmail: String, userPassword: String): Flow<User?>
    fun getUserStreamUsingId(userId: Int): Flow<User?>
}