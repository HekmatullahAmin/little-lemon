package com.hekmatullahamin.littlelemon.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.hekmatullahamin.littlelemon.R
import com.hekmatullahamin.littlelemon.ui.theme.LittleLemonTheme
import com.hekmatullahamin.littlelemon.ui.common_ui.ActionButton

@Composable
fun StartScreen(
    onLoginButtonClicked: () -> Unit,
    onCreateAccountClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box {
        Image(
            painter = painterResource(id = R.drawable.little_lemon_background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom,
            modifier = modifier.fillMaxSize()
        ) {
            Text(
                text = stringResource(id = R.string.app_name).uppercase(),
                style = MaterialTheme.typography.displayMedium
            )

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.large_vertical_spacing)))

            ActionButton(
                text = stringResource(id = R.string.button_login_label),
                onButtonClick = onLoginButtonClicked
            )

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.small_vertical_spacing)))

            ActionButton(
                text = stringResource(id = R.string.button_create_account_label),
                onButtonClick = onCreateAccountClicked
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.large_vertical_spacing)))
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun StartScreenPreview() {
    LittleLemonTheme {
        StartScreen(
            onLoginButtonClicked = {},
            onCreateAccountClicked = {},
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.screens_padding))
        )
    }
}