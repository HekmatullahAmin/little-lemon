package com.hekmatullahamin.littlelemon.ui.common_ui

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.hekmatullahamin.littlelemon.R
import com.hekmatullahamin.littlelemon.ui.theme.LittleLemonTheme
import com.hekmatullahamin.littlelemon.utils.Constants

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    @StringRes
    placeholder: Int,
    @DrawableRes
    endIcon: Int? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    keyboardActions: ImeAction = ImeAction.Next,
    textFieldType: String = "",
    onEndIconClicked: () -> Unit = {},
    errorMessage: String? = null,
//    testTag: String = "",
    modifier: Modifier = Modifier
) {
//    If you want to keep your ViewModel clean and focus only on business logic:
//    Handle password visibility in the CustomTextField.

//    Typically handled within the UI layer (Composable functions) because it directly affects the user interface.
//    Keeping it in the composable makes the UI code more modular and self-contained.
    var passwordVisible by remember { mutableStateOf(true) }
    Column(modifier = modifier) {
        TextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(text = stringResource(id = placeholder))
            },
            trailingIcon = {
                if (endIcon != null) {
                    Image(
                        painter = painterResource(id = endIcon),
                        contentDescription = null,
                        modifier = Modifier.clickable {
                            passwordVisible = !passwordVisible
                            onEndIconClicked()
                        }
                    )
                }
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = keyboardType,
                imeAction = keyboardActions
            ),
            singleLine = true,
            visualTransformation =
            if (textFieldType == Constants.PASSWORD_TEXT_FIELD) {
                if (passwordVisible) {
                    PasswordVisualTransformation()
                } else {
                    VisualTransformation.None
                }
            } else {
                VisualTransformation.None
            },
            isError = errorMessage != null,
            readOnly = textFieldType == Constants.DATE_OF_BIRTH_TEXT_FIELD,
            modifier = Modifier
                .fillMaxWidth()
//                .testTag(testTag)
        )
        errorMessage?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CustomTextFieldPreview() {
    LittleLemonTheme {
        CustomTextField(
            value = "",
            onValueChange = {},
            placeholder = R.string.enter_your_email_placeholder,
            keyboardType = KeyboardType.Email,
            errorMessage = "invalid email",
//            testTag = ""
        )
    }
}