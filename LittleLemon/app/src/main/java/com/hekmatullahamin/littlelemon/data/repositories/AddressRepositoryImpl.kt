package com.hekmatullahamin.littlelemon.data.repositories

import com.hekmatullahamin.littlelemon.data.daos.AddressDao
import com.hekmatullahamin.littlelemon.data.models.Address
import kotlinx.coroutines.flow.Flow

class AddressRepositoryImpl(private val addressDao: AddressDao) : AddressRepository {
    override suspend fun insertAddress(address: Address) =
        addressDao.insert(address)

    override suspend fun updateAddress(address: Address) =
        addressDao.update(address)

    override suspend fun deleteAddress(address: Address) =
        addressDao.delete(address)

    override fun getAddressStream(addressId: Int): Flow<Address?> =
        addressDao.getAddress(addressId)

    override fun getAllAddresses(userId: Int): Flow<List<Address>?> =
        addressDao.getAllAddresses(userId)

    override suspend fun setDefaultAddress(addressId: Int) {
        addressDao.unsetDefaultAddress()
        addressDao.setDefaultAddress(addressId)
    }

    override suspend fun getAddressCount(userId: Int): Int =
        addressDao.getAddressCount(userId = userId)

    override fun getDefaultAddress(userId: Int): Flow<Address?> =
        addressDao.getDefaultAddress(userId)
}