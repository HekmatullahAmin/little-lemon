package com.hekmatullahamin.littlelemon.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hekmatullahamin.littlelemon.AppViewModelProvider
import com.hekmatullahamin.littlelemon.R
import com.hekmatullahamin.littlelemon.ui.common_ui.ActionButton
import com.hekmatullahamin.littlelemon.ui.common_ui.CustomTextField
import com.hekmatullahamin.littlelemon.ui.common_ui.ShowDatePickerDialog
import com.hekmatullahamin.littlelemon.ui.theme.LittleLemonTheme
import com.hekmatullahamin.littlelemon.utils.Constants
import com.hekmatullahamin.littlelemon.utils.formatDate
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(
    onCreateMyAccountButtonClicked: (Int) -> Unit,
    onLoginTextClicked: () -> Unit,
    registerViewModel: RegisterViewModel = viewModel(factory = AppViewModelProvider.Factory),
    modifier: Modifier = Modifier
) {
    val userDetails = registerViewModel.userUiState.userDetails
    val coroutineScope = rememberCoroutineScope()
    val openDialog = remember {
        mutableStateOf(false)
    }

    Column(modifier = modifier.verticalScroll(rememberScrollState())) {
        Text(text = stringResource(id = R.string.first_name_label))
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.small_vertical_spacing)))
        CustomTextField(
            value = userDetails.userFirstName,
            onValueChange = {
                registerViewModel.updateUiState(userDetails.copy(userFirstName = it))
            },
            placeholder = R.string.enter_first_name_placeholder
        )

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.medium_vertical_spacing)))

        Text(text = stringResource(id = R.string.last_name_label))
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.small_vertical_spacing)))
        CustomTextField(
            value = userDetails.userLastName,
            onValueChange = {
                registerViewModel.updateUiState(userDetails.copy(userLastName = it))
            },
            placeholder = R.string.enter_last_name_placeholder,
        )

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.medium_vertical_spacing)))

        Text(text = stringResource(id = R.string.date_of_birth_label))
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.small_vertical_spacing)))
        CustomTextField(
            value = userDetails.userDateOfBirth.formatDate(),
            onValueChange = {},
            placeholder = R.string.select_date_of_birth_placeholder,
            keyboardType = KeyboardType.Number,
            textFieldType = Constants.DATE_OF_BIRTH_TEXT_FIELD,
            endIcon = R.drawable.arrow_down_24,
            onEndIconClicked = {
                openDialog.value = true
            },
        )

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.medium_vertical_spacing)))

        Text(text = stringResource(id = R.string.email_address_label))
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.small_vertical_spacing)))
        CustomTextField(
            value = userDetails.userEmailAddress,
            onValueChange = {
                registerViewModel.updateUiState(userDetails.copy(userEmailAddress = it))
            },
            placeholder = R.string.enter_your_email_placeholder,
            keyboardType = KeyboardType.Email,
            errorMessage = registerViewModel.userUiState.emailError,
        )

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.medium_vertical_spacing)))

        Text(text = stringResource(id = R.string.create_password_label))
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.small_vertical_spacing)))


//        when we click in end icon of our password textfield we change the visibility state of the text
//        from visible to invisible and vice versa

        CustomTextField(
            value = userDetails.userPassword,
            onValueChange = {
                registerViewModel.updateUiState(userDetails.copy(userPassword = it))
            },
            placeholder = R.string.enter_your_password_placeholder,
            endIcon = R.drawable.eye_24,
            keyboardType = KeyboardType.Password,
            keyboardActions = ImeAction.Done,
            textFieldType = Constants.PASSWORD_TEXT_FIELD,
//            when end icon is pressed we will update the visibility of our password
            onEndIconClicked = {
                // Additional action if needed
            },
            errorMessage = registerViewModel.userUiState.passwordError,
        )
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.large_vertical_spacing)))

        if (openDialog.value) {
            ShowDatePickerDialog(
                onDismissRequestClicked = { openDialog.value = false },
                onConfirmButtonClicked = {
//              we update our date of birth in here because we want it to be
//            stored in long in our DB
                    registerViewModel.updateUiState(
                        userDetails.copy(
                            userDateOfBirth = it
                        )
                    )
                    openDialog.value = false
                },
                onDismissButtonClicked = { openDialog.value = false }
            )
        }

        ActionButton(
            text = stringResource(id = R.string.button_create_my_account_label),
            onButtonClick = {
                coroutineScope.launch {
                    val user = registerViewModel.saveUser(userDetails)
                    user?.let {
                        onCreateMyAccountButtonClicked(it.userId)
                    }
                }
            },
//            we want to user to register if the entry is valid and then then
//            action button will be enabled
            enabled = registerViewModel.userUiState.isEntryValid
        )

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.medium_vertical_spacing)))

        Row(
            modifier = modifier
                .align(Alignment.CenterHorizontally)
        ) {
            Text(text = stringResource(id = R.string.already_member_label))
            Text(
                text = stringResource(id = R.string.login_label),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { onLoginTextClicked() }
            )
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun RegisterScreenPreview() {
    LittleLemonTheme {
        RegisterScreen(
            onCreateMyAccountButtonClicked = {},
            onLoginTextClicked = {},
            modifier = Modifier.padding(dimensionResource(id = R.dimen.screens_padding))
        )
    }
}
