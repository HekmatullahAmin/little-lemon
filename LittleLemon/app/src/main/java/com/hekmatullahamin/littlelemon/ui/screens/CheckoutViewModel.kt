package com.hekmatullahamin.littlelemon.ui.screens

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hekmatullahamin.littlelemon.data.repositories.AddressRepository
import com.hekmatullahamin.littlelemon.ui.navigation.CheckoutDestination
import com.hekmatullahamin.littlelemon.ui.state.CheckoutUiState
import com.hekmatullahamin.littlelemon.utils.Constants
import com.hekmatullahamin.littlelemon.utils.concatenateAddress
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface PaymentMethod {
    data class Card(
        val nameOnCard: String = "",
        val cardNumber: String = "",
        val expiresDate: String = "",
        val cvv: String = ""
    ) : PaymentMethod

    data object Cash : PaymentMethod
}

class CheckoutViewModel(
    savedStateHandle: SavedStateHandle,
    private val addressRepository: AddressRepository
) : ViewModel() {
    val userId: Int = checkNotNull(savedStateHandle[CheckoutDestination.userIdArg])
    private val _checkoutUiState = MutableStateFlow(CheckoutUiState())
    val checkoutUiState: StateFlow<CheckoutUiState> = _checkoutUiState.asStateFlow()

    init {
        viewModelScope.launch {
            addressRepository.getDefaultAddress(userId)
                .collectLatest {
                    val concatenatedAddress = it?.let {
                        concatenateAddress(it)
                    } ?: Constants.SELECT_YOUR_ADDRESS

                    _checkoutUiState.value = CheckoutUiState(
                        defaultAddress = concatenatedAddress
                    )
                }
        }
    }

    fun updatePaymentMethod(paymentMethod: PaymentMethod) {
        _checkoutUiState.update {
            it.copy(
                paymentMethod = paymentMethod, isEntryValid = validateEntries(paymentMethod)
            )
        }
    }

    private fun validateEntries(paymentMethod: PaymentMethod): Boolean {
//        if no user address is added then can't make the payment they should add it
        val isUserAddressAvailable = _checkoutUiState.value.defaultAddress.isNotBlank() &&
                _checkoutUiState.value.defaultAddress != Constants.SELECT_YOUR_ADDRESS
        return when (paymentMethod) {
            is PaymentMethod.Card -> {
                paymentMethod.nameOnCard.isNotBlank() &&
                        paymentMethod.cardNumber.isNotBlank() &&
                        paymentMethod.expiresDate.isNotBlank() &&
                        paymentMethod.cvv.isNotBlank() && isUserAddressAvailable
            }

            PaymentMethod.Cash -> isUserAddressAvailable
        }
    }

}

