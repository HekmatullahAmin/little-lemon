package com.hekmatullahamin.littlelemon.ui.common_ui

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import com.hekmatullahamin.littlelemon.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowDatePickerDialog(
    onDismissRequestClicked: () -> Unit,
    onConfirmButtonClicked: (Long) -> Unit,
    onDismissButtonClicked: () -> Unit
) {
    val datePickerState = rememberDatePickerState()
    val confirmedEnable = remember {
        derivedStateOf {
            datePickerState.selectedDateMillis != null
        }
    }
    DatePickerDialog(
        onDismissRequest = onDismissRequestClicked,
        confirmButton = {
            TextButton(
                onClick = { onConfirmButtonClicked(datePickerState.selectedDateMillis!!) },
                enabled = confirmedEnable.value
            ) {
                Text(text = stringResource(id = R.string.date_picker_confirm_button_text_label))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissButtonClicked) {
                Text(text = stringResource(id = R.string.date_picker_dismiss_button_text_label))
            }
        },
        modifier = Modifier.testTag("DatePickerDialog")
    ) {
        DatePicker(state = datePickerState)
    }
}