package com.hekmatullahamin.littlelemon.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.hekmatullahamin.littlelemon.R
import com.hekmatullahamin.littlelemon.ui.theme.LittleLemonTheme
import com.hekmatullahamin.littlelemon.ui.common_ui.ActionButton
import com.hekmatullahamin.littlelemon.ui.common_ui.ItemQuantitySelector
import com.hekmatullahamin.littlelemon.ui.state.MenuItemUiState
import com.hekmatullahamin.littlelemon.ui.state.OrderUiState
import com.hekmatullahamin.littlelemon.utils.formatAsCurrency

@Composable
fun CartScreen(
    orderUiState: OrderUiState,
    onAddPromoCodeCardClicked: () -> Unit,
    onCheckoutButtonClicked: () -> Unit,
    onIncrement: (Int) -> Unit,
    onDecrement: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        bottomBar = {
            ActionButton(
                text = stringResource(id = R.string.checkout_button_label),
                onButtonClick = onCheckoutButtonClicked,
                modifier = Modifier.padding(dimensionResource(id = R.dimen.action_button_padding))
            )
        }
    ) { contentPadding ->
        LazyColumn(modifier = modifier.padding(contentPadding)) {
            items(
                items = orderUiState.cartItems,
                key = { item -> item.menuItem.itemId }) { itemUiState ->
                CartItem(
                    itemUiState = itemUiState,
                    onIncrement = { onIncrement(itemUiState.menuItem.itemId) },
                    onDecrement = { onDecrement(itemUiState.menuItem.itemId) })
            }
            item {
                CartSummarySection(
                    savingAmount = 3.95,
                    promoCode = "HA",
                    subtotal = orderUiState.subtotal,
                    onAddPromoCodeCardClicked = onAddPromoCodeCardClicked
                )
            }
        }

    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CartScreenPreview() {
    LittleLemonTheme {
        CartScreen(
            orderUiState = OrderUiState(),
            onAddPromoCodeCardClicked = {},
            onCheckoutButtonClicked = {},
            onIncrement = {},
            onDecrement = {},
            modifier = Modifier.padding(dimensionResource(id = R.dimen.screens_padding))
        )
    }
}

@Composable
fun CartSummarySection(
    savingAmount: Double,
    promoCode: String,
    subtotal: Double,
    onAddPromoCodeCardClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.small_vertical_spacing)),
        modifier = modifier
    ) {
        PromoCodeCard(onAddPromoCodeCardClicked = onAddPromoCodeCardClicked)
        PromoSavingSummaryCard(savingAmount = savingAmount, promoCode = promoCode)
        Row {
            Text(
                text = stringResource(id = R.string.order_subtotal_label),
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.weight(1F))
            Text(
                text = formatAsCurrency(subtotal),
                style = MaterialTheme.typography.headlineSmall
            )
        }
    }
}

@Composable
fun CartItem(
    itemUiState: MenuItemUiState,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = modifier
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen.card_inside_padding))
    ) {
        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.small_horizontal_spacing)))
        Column(
            modifier = Modifier.weight(1F),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.small_vertical_spacing))
        ) {
            Text(
                text = itemUiState.menuItem.itemName,
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = formatAsCurrency(itemUiState.totalCost)
            )
        }
        ItemQuantitySelector(
            count = itemUiState.quantity,
            onIncrement = onIncrement,
            onDecrement = onDecrement,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CartItemPreview() {
    LittleLemonTheme {
        CartItem(
            itemUiState = MenuItemUiState(),
            onIncrement = { },
            onDecrement = {}
        )
    }
}

@Composable
fun PromoCodeCard(
    onAddPromoCodeCardClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier
        .fillMaxWidth()
        .clickable { onAddPromoCodeCardClicked() }) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.promo_code_card_inner_padding))
                .fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = R.drawable.promocode),
                contentDescription = null,
                modifier = Modifier.size(dimensionResource(id = R.dimen.promo_code_image_size))
            )
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.small_horizontal_spacing)))
            Text(
                text = stringResource(id = R.string.add_promo_code_label),
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.weight(1F))
            Image(
                painter = painterResource(id = R.drawable.baseline_arrow_forward_24),
                contentDescription = stringResource(id = R.string.add_promo_code_arrow_content_description)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PromoCodeCardPreview() {
    LittleLemonTheme {
        PromoCodeCard(onAddPromoCodeCardClicked = {})
    }
}

@Composable
fun PromoSavingSummaryCard(
    savingAmount: Double,
    promoCode: String,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer),
        modifier = modifier
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen.promo_saving_summary_card_outside_padding))
    ) {
        Text(
            text = stringResource(
                id = R.string.promo_code_saving_amount,
                formatAsCurrency(savingAmount),
                promoCode
            ),
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.promo_code_card_inner_padding))
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PromoSavingSummaryCardPreview() {
    LittleLemonTheme {
        PromoSavingSummaryCard(3.95, "HA")
    }
}





