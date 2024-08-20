package com.hekmatullahamin.littlelemon.data.repositories

import com.hekmatullahamin.littlelemon.data.models.Address
import kotlinx.coroutines.flow.Flow

interface AddressRepository {
    suspend fun insertAddress(address: Address)
    suspend fun updateAddress(address: Address)
    suspend fun deleteAddress(address: Address)
    fun getAddressStream(addressId: Int): Flow<Address?>
    fun getAllAddresses(userId: Int): Flow<List<Address>?>
    suspend fun setDefaultAddress(addressId: Int)
    suspend fun getAddressCount(userId: Int): Int
    fun getDefaultAddress(userId: Int): Flow<Address?>
}