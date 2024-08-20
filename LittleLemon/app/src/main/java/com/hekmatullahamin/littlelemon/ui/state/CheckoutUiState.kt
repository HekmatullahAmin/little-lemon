package com.hekmatullahamin.littlelemon.ui.state

import com.hekmatullahamin.littlelemon.ui.screens.PaymentMethod

data class CheckoutUiState(
    val defaultAddress: String = "",
    val paymentMethod: PaymentMethod = PaymentMethod.Card(),
    val isEntryValid: Boolean = false
)