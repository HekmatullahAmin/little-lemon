package com.hekmatullahamin.littlelemon.ui.state

import com.hekmatullahamin.littlelemon.data.models.User

data class PasswordUserUiState(
    val userDetails: User = User(),
    val isEntryValid: Boolean = false
)