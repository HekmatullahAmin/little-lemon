package com.hekmatullahamin.littlelemon.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hekmatullahamin.littlelemon.AppViewModelProvider
import com.hekmatullahamin.littlelemon.R
import com.hekmatullahamin.littlelemon.ui.theme.LittleLemonTheme
import com.hekmatullahamin.littlelemon.ui.common_ui.ActionButton
import com.hekmatullahamin.littlelemon.ui.common_ui.CustomTextField
import com.hekmatullahamin.littlelemon.utils.formatAsCurrency

@Composable
fun CheckoutScreen(
    onSelectAddressClicked: () -> Unit,
    subtotal: Double,
    onPayNowButtonClicked: () -> Unit,
    checkoutViewModel: CheckoutViewModel = viewModel(factory = AppViewModelProvider.Factory),
    modifier: Modifier = Modifier
) {
    val checkoutUiState by checkoutViewModel.checkoutUiState.collectAsState()
    Scaffold(
        bottomBar = {
            ActionButton(
                text = stringResource(id = R.string.pay_now_button_label),
                onButtonClick = onPayNowButtonClicked,
                enabled = checkoutUiState.isEntryValid,
                modifier = Modifier.padding(dimensionResource(id = R.dimen.action_button_padding))
            )
        }
    ) { contentPadding ->
        Column(
            modifier = modifier
                .padding(contentPadding)
                .verticalScroll(rememberScrollState())
        ) {
            PaymentMethodCard(
                paymentMethod = checkoutUiState.paymentMethod,
                onPaymentMethodChange = checkoutViewModel::updatePaymentMethod
            )
            BillingSummary(
                onSelectAddressClicked = onSelectAddressClicked,
                deliveryAddress = checkoutUiState.defaultAddress,
                subtotal = subtotal,
//                for now we want our delivery be the 10% of subtotal
                deliveryFee = subtotal * 0.10
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CheckoutScreenPreview() {
    LittleLemonTheme {
        CheckoutScreen(
            onSelectAddressClicked = {},
            subtotal = 0.0,
            onPayNowButtonClicked = {}
        )
    }
}

@Composable
fun PaymentMethodCard(
    paymentMethod: PaymentMethod,
    onPaymentMethodChange: (PaymentMethod) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RectangleShape,
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.medium_vertical_spacing)),
            modifier = Modifier.padding(dimensionResource(id = R.dimen.card_inside_padding))
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = paymentMethod is PaymentMethod.Card,
                    onClick = { onPaymentMethodChange(PaymentMethod.Card()) },
                    modifier = Modifier.testTag("Method Card")
                )
                Text(text = stringResource(id = R.string.radio_button_pay_by_card_label))
                RadioButton(
                    selected = paymentMethod is PaymentMethod.Cash,
                    onClick = { onPaymentMethodChange(PaymentMethod.Cash) },
                    modifier = Modifier.testTag("Method Cash")
                )
                Text(text = stringResource(id = R.string.radio_button_pay_by_cash_label))
            }

            if (paymentMethod is PaymentMethod.Card) {
                CustomTextField(
                    value = paymentMethod.nameOnCard,
                    onValueChange = {
                        onPaymentMethodChange(
                            paymentMethod.copy(
                                nameOnCard = it
                            )
                        )
                    },
                    placeholder = R.string.name_on_card_placeholder
                )

                CustomTextField(
                    value = paymentMethod.cardNumber,
                    onValueChange = {
                        onPaymentMethodChange(
                            paymentMethod.copy(
                                cardNumber = it
                            )
                        )
                    },
                    keyboardType = KeyboardType.Number,
                    placeholder = R.string.card_number_placeholder
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    CustomTextField(
                        value = paymentMethod.expiresDate,
                        onValueChange = {
                            onPaymentMethodChange(
                                paymentMethod.copy(
                                    expiresDate = it
                                )
                            )
                        },
                        placeholder = R.string.expires_end_placeholder,
                        modifier = Modifier.weight(1F)
                    )
                    Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.small_horizontal_spacing)))
                    CustomTextField(
                        value = paymentMethod.cvv,
                        onValueChange = {
                            onPaymentMethodChange(
                                paymentMethod.copy(
                                    cvv = it
                                )
                            )
                        },
                        keyboardType = KeyboardType.Number,
                        placeholder = R.string.cvv_code_placeholder,
                        modifier = Modifier.weight(1F)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PaymentMethodCardPreview() {
    LittleLemonTheme {
        PaymentMethodCard(
            paymentMethod = PaymentMethod.Card(),
            onPaymentMethodChange = {},
        )
    }
}

@Composable
fun BillingSummary(
    deliveryAddress: String,
    onSelectAddressClicked: () -> Unit,
    subtotal: Double,
    deliveryFee: Double,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.medium_vertical_spacing)),
        modifier = modifier
            .padding(dimensionResource(id = R.dimen.screens_padding))
            .fillMaxWidth()
    ) {
        Text(text = stringResource(id = R.string.delivery_address_label))
        Text(
            text = deliveryAddress,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.clickable { onSelectAddressClicked() }
        )
        HorizontalDivider(thickness = dimensionResource(id = R.dimen.horizontal_divider_thickness))

        Row {
            Text(text = stringResource(id = R.string.subtotal_label))
            Spacer(modifier = Modifier.weight(1F))
            Text(text = formatAsCurrency(subtotal))
        }

        Row {
            Text(text = stringResource(id = R.string.delivery_fee_label))
            Spacer(modifier = Modifier.weight(1F))
            Text(text = formatAsCurrency(deliveryFee))
        }

        Row {
            Text(text = stringResource(id = R.string.total_label))
            Spacer(modifier = Modifier.weight(1F))
            Text(text = formatAsCurrency(subtotal + deliveryFee))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BillingSummaryPreview() {
    LittleLemonTheme {
        BillingSummary(
            deliveryAddress = "65 Price Street, Derby, DE8 SRT",
            onSelectAddressClicked = {},
            subtotal = 899.21,
            deliveryFee = 25.0
        )
    }
}

