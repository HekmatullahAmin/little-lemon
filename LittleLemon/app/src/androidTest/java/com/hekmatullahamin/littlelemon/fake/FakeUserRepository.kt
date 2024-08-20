package com.hekmatullahamin.littlelemon.fake

import com.hekmatullahamin.littlelemon.data.models.User
import com.hekmatullahamin.littlelemon.data.repositories.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class FakeUserRepository : UserRepository {

//    In-Memory Data Storage: users is a mutable list that mimics your database table for users.
//    MutableStateFlow: userFlow is a Flow that emits the current list of users whenever it changes.
//    Update Flow: After modifying the users list, the userFlow is updated to reflect these changes.
//    Flow Mapping: getUserStream and getUserStreamUsingId use the map function to filter users based on the provided criteria.
    private val users = mutableListOf<User>()
    private val userFlow = MutableStateFlow<List<User>>(emptyList())

    override suspend fun insertUser(user: User) {
        users.add(user)
        userFlow.update { users.toList() }
    }

    override suspend fun updateUser(user: User) {
        users.replaceAll { if (it.userId == user.userId) user else it }
        userFlow.update { users.toList() }
    }

    override suspend fun deleteUser(user: User) {
        users.removeIf { it.userId == user.userId }
        userFlow.update { users.toList() }
    }

    override fun getUserStream(userEmail: String, userPassword: String): Flow<User?> {
        return userFlow.map {
            users.find { it.userEmailAddress == userEmail && it.userPassword == userPassword }
        }
    }

    override fun getUserStreamUsingId(userId: Int): Flow<User?> {
        return userFlow.map { users.find { it.userId == userId } }
    }
}