package com.hekmatullahamin.littlelemon.fake

import com.hekmatullahamin.littlelemon.data.models.Address
import com.hekmatullahamin.littlelemon.data.repositories.AddressRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class FakeAddressRepository : AddressRepository {

    private val addresses = mutableListOf<Address>()
    private val addressesFlow = MutableStateFlow<List<Address>>(emptyList())

    override suspend fun insertAddress(address: Address) {
        addresses.add(address)
        addressesFlow.update { addresses.toList() }
    }

    override suspend fun updateAddress(address: Address) {
        addresses.replaceAll { if (it.addressId == address.addressId) address else it }
        addressesFlow.update { addresses.toList() }
    }

    override suspend fun deleteAddress(address: Address) {
        addresses.removeIf { it.addressId == address.addressId }
        addressesFlow.update { addresses.toList() }
    }

    override fun getAddressStream(addressId: Int): Flow<Address?> {
        return addressesFlow.map { addressesList ->
            addressesList.find { it.addressId == addressId }
        }
    }

    override fun getAllAddresses(userId: Int): Flow<List<Address>?> {
        return addressesFlow.map { addressList ->
            addressList.filter {
                it.userId == userId
            }
//            addresses.filter { it.userId == userId }
        }
    }

    override suspend fun setDefaultAddress(addressId: Int) {
//        we will set isDefault to true if the id's are the same otherwise false for addresses which are not default
        addresses.replaceAll {
            if (it.addressId == addressId) {
                it.copy(
                    isDefault = true
                )
            } else {
                it.copy(isDefault = false)
            }
        }
        addressesFlow.update { addresses.toList() }
    }

    override suspend fun getAddressCount(userId: Int): Int {
        return addresses.count {
            it.userId == userId
        }
    }

    //    if the isDefault = true and it is from the current user then return it
    override fun getDefaultAddress(userId: Int): Flow<Address?> {
        return addressesFlow.map { addressList ->
            addressList.find {
                it.userId == userId && it.isDefault
            }
        }
    }
}