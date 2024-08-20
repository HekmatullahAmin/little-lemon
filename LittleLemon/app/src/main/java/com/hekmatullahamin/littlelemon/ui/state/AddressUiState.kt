package com.hekmatullahamin.littlelemon.ui.state

import com.hekmatullahamin.littlelemon.data.models.Address

sealed interface AddressUiState {
    data class Success(val addresses: List<Address>) : AddressUiState
    data object Loading : AddressUiState
    data object Error : AddressUiState
}