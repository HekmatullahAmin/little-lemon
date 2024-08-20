package com.hekmatullahamin.littlelemon.ui.common_ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.hekmatullahamin.littlelemon.R
import com.hekmatullahamin.littlelemon.ui.theme.LittleLemonTheme

@Composable
fun ActionButton(
    text: String,
    onButtonClick: () -> Unit,
    enabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onButtonClick,
        enabled = enabled,
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(dimensionResource(id = R.dimen.action_button_text_padding)),
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ActionButtonPreview() {
    LittleLemonTheme {
        ActionButton(
            text = "Log In",
            onButtonClick = {},
            enabled = true
        )
    }
}