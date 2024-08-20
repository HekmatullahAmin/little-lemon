package com.hekmatullahamin.littlelemon.ui.state

import com.hekmatullahamin.littlelemon.data.models.User

data class UserUiState(
    val userDetails: User = User(),
    val isEntryValid: Boolean = false,
    val emailError: String? = null,
    val passwordError: String? = null
)
