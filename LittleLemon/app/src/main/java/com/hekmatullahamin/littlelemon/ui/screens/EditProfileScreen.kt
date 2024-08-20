package com.hekmatullahamin.littlelemon.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hekmatullahamin.littlelemon.AppViewModelProvider
import com.hekmatullahamin.littlelemon.R
import com.hekmatullahamin.littlelemon.ui.theme.LittleLemonTheme
import com.hekmatullahamin.littlelemon.ui.common_ui.ShowDatePickerDialog
import com.hekmatullahamin.littlelemon.utils.formatDate
import kotlinx.coroutines.launch

@Composable
fun EditProfileScreen(
    onNavigateUpClicked: () -> Unit,
    onAddressClicked: () -> Unit,
    editProfileViewModel: EditProfileViewModel = viewModel(factory = AppViewModelProvider.Factory),
    modifier: Modifier = Modifier
) {
//    Don't use flow when receiving from Db and updating values by passing the same user
    val user = editProfileViewModel.editProfileUiState.userDetails
    val openDialog = remember {
        mutableStateOf(false)
    }
    val scope = rememberCoroutineScope()

    Scaffold(topBar = {
        EditProfileScreenTopAppBar(
            onNavigateUpClicked = onNavigateUpClicked,
            onSaveClicked = {
                scope.launch {
                    editProfileViewModel.updateUser()
                    onNavigateUpClicked()
                }
            },
            isOnClickEnable = editProfileViewModel.editProfileUiState.isEntryValid
        )
    }) { contentPadding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.small_vertical_spacing)),
            modifier = modifier
                .verticalScroll(rememberScrollState())
                .padding(contentPadding)
        ) {
            LabeledTextField(
                fieldName = stringResource(id = R.string.first_name_label),
                value = user.userFirstName,
                onValueChange = {
                    editProfileViewModel.updateUiState(
                        user.copy(
                            userFirstName = it
                        )
                    )
                }
            )
            LabeledTextField(
                fieldName = stringResource(id = R.string.last_name_label),
                value = user.userLastName,
                onValueChange = {
                    editProfileViewModel.updateUiState(
                        user.copy(
                            userLastName = it
                        )
                    )
                }
            )
            LabeledText(
                fieldName = stringResource(id = R.string.date_of_birth_edit_profile_screen),
                value = user.userDateOfBirth.formatDate(),
                onTextClicked = { openDialog.value = true }
            )

            LabeledTextField(
                fieldName = stringResource(id = R.string.email_address_label),
                value = user.userEmailAddress,
                onValueChange = {
                    editProfileViewModel.updateUiState(
                        user.copy(
                            userEmailAddress = it
                        )
                    )
                }
            )
            LabeledTextField(
                fieldName = stringResource(id = R.string.password_label),
                value = user.userPassword,
                onValueChange = {
                    editProfileViewModel.updateUiState(
                        user.copy(
                            userPassword = it
                        )
                    )
                }
            )

            LabeledText(
                fieldName = stringResource(id = R.string.address_label),
                value = editProfileViewModel.editProfileUiState.defaultAddress,
                onTextClicked = onAddressClicked
            )

            if (openDialog.value) {
                ShowDatePickerDialog(
                    onDismissRequestClicked = { openDialog.value = false },
                    onConfirmButtonClicked = {
//              we update our date of birth in here because we want it to be
//              stored in long in our DB
                        editProfileViewModel.updateUiState(
                            user.copy(
                                userDateOfBirth = it
                            )
                        )
                        openDialog.value = false
                    },
                    onDismissButtonClicked = {
                        openDialog.value = false
                    }
                )
            }
        }
    }
}

//This is to make the same as our textField the same height and text positions
@Composable
fun LabeledText(
    fieldName: String,
    value: String,
    onTextClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = fieldName,
                modifier = Modifier
                    .weight(1F)
                    .padding(vertical = dimensionResource(id = R.dimen.labeled_text_vertical_padding))
            )
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.small_horizontal_spacing)))
            Text(
                text = value,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .weight(2F)
                    .padding(
                        start = dimensionResource(id = R.dimen.labeled_text_start_and_end_padding),
                        top = dimensionResource(id = R.dimen.labeled_text_top_and_bottom_padding),
                        bottom = dimensionResource(id = R.dimen.labeled_text_top_and_bottom_padding),
                        end = dimensionResource(id = R.dimen.labeled_text_start_and_end_padding)
                    )
                    .clickable { onTextClicked() }
            )

        }
        HorizontalDivider(thickness = dimensionResource(id = R.dimen.horizontal_divider_thickness))
    }
}

@Preview(showBackground = true)
@Composable
fun LabeledTextPreview() {
    LittleLemonTheme {
        LabeledText(
            fieldName = stringResource(id = R.string.address_label),
            value = "hi how are u",
            onTextClicked = {}
        )
    }
}

@Composable
fun LabeledTextField(
    fieldName: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = fieldName,
                modifier = Modifier.weight(1F)
            )
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.small_horizontal_spacing)))
            TextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                modifier = Modifier
                    .weight(2F)
            )
        }
        HorizontalDivider(thickness = dimensionResource(id = R.dimen.horizontal_divider_thickness))
    }
}

@Preview(showBackground = true)
@Composable
fun LabeledTextFieldPreview() {
    LittleLemonTheme {
        LabeledTextField(
            fieldName = stringResource(id = R.string.first_name_label),
            value = "Hekmatullah",
            onValueChange = {}
        )
    }
}

@Composable
fun EditProfileScreenTopAppBar(
    onNavigateUpClicked: () -> Unit,
    onSaveClicked: () -> Unit,
    isOnClickEnable: Boolean = false,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        IconButton(onClick = onNavigateUpClicked) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = stringResource(id = R.string.back_button_content_description)
            )
        }
        Text(
            text = stringResource(id = R.string.top_app_bar_edit_profile_title),
            modifier = Modifier.weight(1F),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge
        )
        TextButton(
            onClick = onSaveClicked,
            enabled = isOnClickEnable
        ) {
            Text(text = stringResource(id = R.string.save_text_label))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EditProfileScreenTopAppBarPreview() {
    LittleLemonTheme {
        EditProfileScreenTopAppBar(
            onNavigateUpClicked = {},
            onSaveClicked = {}
        )
    }
}