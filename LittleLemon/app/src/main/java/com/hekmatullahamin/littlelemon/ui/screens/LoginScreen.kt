package com.hekmatullahamin.littlelemon.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.hekmatullahamin.littlelemon.data.models.User
import com.hekmatullahamin.littlelemon.ui.common_ui.ActionButton
import com.hekmatullahamin.littlelemon.ui.common_ui.CustomTextField
import com.hekmatullahamin.littlelemon.ui.theme.LittleLemonTheme
import com.hekmatullahamin.littlelemon.utils.Constants
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    onLoginButtonClicked: (User?) -> Unit,
    onJoinTextClicked: () -> Unit,
    loginViewModel: LoginViewModel = viewModel(factory = AppViewModelProvider.Factory),
    modifier: Modifier = Modifier
) {

    val loginUiState = loginViewModel.uiState
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = modifier.verticalScroll(rememberScrollState())) {
        Text(
            text = stringResource(id = R.string.login_title_label),
            style = MaterialTheme.typography.displayMedium
        )

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.large_vertical_spacing)))

        CustomTextField(
            value = loginUiState.userDetails.userEmailAddress,
            onValueChange = {
                loginViewModel.updatePasswordUiState(
                    loginUiState.userDetails.copy(
                        userEmailAddress = it
                    )
                )
            },
            placeholder = R.string.email_placeholder,
            keyboardType = KeyboardType.Email
        )

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.medium_vertical_spacing)))

        CustomTextField(
            value = loginUiState.userDetails.userPassword,
            onValueChange = {
                loginViewModel.updatePasswordUiState(
                    loginUiState.userDetails.copy(
                        userPassword = it
                    )
                )
            },
            placeholder = R.string.password_placeholder,
            endIcon = R.drawable.eye_24,
            keyboardType = KeyboardType.Password,
            keyboardActions = ImeAction.Done,
            textFieldType = Constants.PASSWORD_TEXT_FIELD,
            onEndIconClicked = {}
        )

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.medium_vertical_spacing)))

        ActionButton(
            text = stringResource(id = R.string.button_login_label),
            onButtonClick = {
                coroutineScope.launch {
                    val currentUser = loginViewModel.getUser(loginUiState.userDetails)
                    onLoginButtonClicked(currentUser)
                }
            },
            enabled = loginUiState.isEntryValid
        )

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.medium_vertical_spacing)))

        Row(
            modifier = modifier
                .align(Alignment.CenterHorizontally)
        ) {
            Text(text = stringResource(id = R.string.dont_have_account))
            Text(
                text = stringResource(id = R.string.join_label),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { onJoinTextClicked() }
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginScreenPreview() {
    LittleLemonTheme {
        LoginScreen(
            onLoginButtonClicked = {},
            onJoinTextClicked = {},
            modifier = Modifier.padding(dimensionResource(id = R.dimen.screens_padding))
        )
    }
}