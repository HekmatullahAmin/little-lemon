package com.hekmatullahamin.littlelemon.ui.state

import com.hekmatullahamin.littlelemon.R
import com.hekmatullahamin.littlelemon.data.models.Address

data class BottomSheetUiState(
    val bottomSheetTitle: Int = R.string.add_new_address_title_label,
    val bottomSheetButtonLabel: Int = R.string.save_address_button_label,
    val addressDetails: Address = Address(),
    val isEntryValid: Boolean = false
)