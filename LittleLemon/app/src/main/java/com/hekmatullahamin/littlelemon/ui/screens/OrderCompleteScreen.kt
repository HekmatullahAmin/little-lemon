package com.hekmatullahamin.littlelemon.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.hekmatullahamin.littlelemon.R
import com.hekmatullahamin.littlelemon.ui.theme.LittleLemonTheme
import com.hekmatullahamin.littlelemon.ui.common_ui.ActionButton
import com.hekmatullahamin.littlelemon.utils.formatAsCurrency

@Composable
fun OrderCompleteScreen(
    totalAmount: Double,
    onOrderAgainButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.confetti))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        LottieAnimation(
            composition = composition,
            progress = { progress },
            modifier = Modifier.size(dimensionResource(id = R.dimen.confetti_size))
        )

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.medium_vertical_spacing)))
        Text(
            text = stringResource(id = R.string.order_complete_label),
            style = MaterialTheme.typography.displaySmall
        )
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.small_vertical_spacing)))
        Text(text = stringResource(id = R.string.paid_amount_label, formatAsCurrency(totalAmount)))
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.large_vertical_spacing)))
        ActionButton(
            text = stringResource(id = R.string.order_again_button_label),
            onButtonClick = onOrderAgainButtonClicked,
            modifier = Modifier.fillMaxWidth(0.8F)
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun OrderCompleteScreenPreview() {
    LittleLemonTheme {
        OrderCompleteScreen(
            totalAmount = 56.0,
            onOrderAgainButtonClicked = {},
            modifier = Modifier.padding(dimensionResource(id = R.dimen.screens_padding))
        )
    }
}