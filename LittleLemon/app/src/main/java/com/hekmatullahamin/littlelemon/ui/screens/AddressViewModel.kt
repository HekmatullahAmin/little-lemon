package com.hekmatullahamin.littlelemon.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hekmatullahamin.littlelemon.data.models.Address
import com.hekmatullahamin.littlelemon.data.repositories.AddressRepository
import com.hekmatullahamin.littlelemon.ui.navigation.AddressDestination
import com.hekmatullahamin.littlelemon.ui.state.AddressUiState
import com.hekmatullahamin.littlelemon.ui.state.BottomSheetUiState
import com.hekmatullahamin.littlelemon.utils.Constants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.IOException

class AddressViewModel(
    savedStateHandle: SavedStateHandle,
    private val addressRepository: AddressRepository,
) : ViewModel() {

    //    we use this userId to assign it to  userId column of Address data class showing this
//    address is added by this user
    private val userId: Int = checkNotNull(savedStateHandle[AddressDestination.userIdArg])
    private var _addressUiState: MutableStateFlow<AddressUiState> =
        MutableStateFlow(AddressUiState.Loading)
    val addressUiState: StateFlow<AddressUiState> = _addressUiState.asStateFlow()

    var bottomSheetUiState by mutableStateOf(BottomSheetUiState())
        private set

    init {
        fetchAllAddresses()
    }

    fun updateUiState(btmSheetUiState: BottomSheetUiState) {
        bottomSheetUiState = btmSheetUiState
        validateInput(btmSheetUiState.addressDetails)
    }

    private fun validateInput(addressDetails: Address): Boolean {
        val isDetailsValid =
            addressDetails.addressLineOne.isNotBlank() && addressDetails.addressLineTwo.isNotBlank() &&
                    addressDetails.city.isNotBlank() && addressDetails.postalCode.isNotBlank() && addressDetails.country.isNotBlank()

        bottomSheetUiState = bottomSheetUiState.copy(
            isEntryValid = isDetailsValid
        )
        return isDetailsValid
    }

    private fun fetchAllAddresses() {
        viewModelScope.launch {
            _addressUiState.value = AddressUiState.Loading
            try {
                addressRepository
//                    we will get all addresses which is added by this user
                    .getAllAddresses(userId)
                    .stateIn(
                        scope = viewModelScope,
                        started = SharingStarted.WhileSubscribed(Constants.TIMEOUT_MILLIS),
                        initialValue = listOf()
                    )
                    .collectLatest {
                        if (it.isNullOrEmpty()) {
                            _addressUiState.value = AddressUiState.Error
                        } else {
                            _addressUiState.value = AddressUiState.Success(it)
                        }
                    }
            } catch (e: IOException) {
                _addressUiState.value = AddressUiState.Error
            }
        }
    }

    suspend fun insertAddress(address: Address) {
        if (validateInput(address)) {
//            we get the total count of address table and if there is only 1 or no
//            address then we set them as our default address before inserting to DB
            val addressCount = addressRepository.getAddressCount(userId)
            val newAddress = if (addressCount == 0) {
                address.copy(isDefault = true)
            } else {
                address
            }
            addressRepository.insertAddress(newAddress.copy(userId = userId))
        }
    }

    suspend fun updateAddress(address: Address) {
        if (validateInput(address)) {
            addressRepository.updateAddress(address)
            bottomSheetUiState = BottomSheetUiState()
        }
    }

    suspend fun setDefaultAddress(address: Address) {
        addressRepository.setDefaultAddress(address.addressId)
    }

    suspend fun deleteAddress(address: Address) {
        addressRepository.deleteAddress(address)
    }

    //            after adding address we will reset bottom sheet (make it empty)
    fun resetBottomSheet() {
        bottomSheetUiState = BottomSheetUiState()
    }
}