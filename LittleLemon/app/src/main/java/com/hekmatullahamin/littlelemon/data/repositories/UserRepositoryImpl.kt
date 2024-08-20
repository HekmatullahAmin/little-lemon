package com.hekmatullahamin.littlelemon.data.repositories

import com.hekmatullahamin.littlelemon.data.daos.UserDao
import com.hekmatullahamin.littlelemon.data.models.User
import kotlinx.coroutines.flow.Flow

class UserRepositoryImpl(private val userDao: UserDao) : UserRepository {
    override suspend fun insertUser(user: User) = userDao.insertUser(user)

    override suspend fun updateUser(user: User) = userDao.updateUser(user)

    override suspend fun deleteUser(user: User) = userDao.deleteUser(user)

    override fun getUserStream(userEmail: String, userPassword: String): Flow<User?> =
        userDao.getUser(userEmail, userPassword)

    override fun getUserStreamUsingId(userId: Int): Flow<User?> =
        userDao.getUserUsingId(userId)
}