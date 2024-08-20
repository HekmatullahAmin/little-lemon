package com.hekmatullahamin.littlelemon.ui.common_ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.hekmatullahamin.littlelemon.R
import com.hekmatullahamin.littlelemon.ui.theme.LittleLemonTheme

@Composable
fun ItemQuantitySelector(
    count: Int,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth(0.5F)
            .clip(RoundedCornerShape(dimensionResource(id = R.dimen.quantity_card_corner_radius))),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.item_quantity_selector_inside_padding))
        ) {
            IconButton(
                onClick = onDecrement,
//                modifier = Modifier.testTag("Dec")
            ) {
                Image(
                    painter = painterResource(id = R.drawable.minus),
                    contentDescription = stringResource(id = R.string.decrement_content_description),
                )
            }
            Text(
                text = count.toString(),
                style = MaterialTheme.typography.headlineMedium
            )
            IconButton(
                onClick = onIncrement,
//                modifier = Modifier.testTag("Inc")
            ) {
                Image(
                    painter = painterResource(id = R.drawable.plus),
                    contentDescription = stringResource(id = R.string.increment_content_description),
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ItemQuantitySelectorPreview() {
    LittleLemonTheme {
        ItemQuantitySelector(2, {}, {})
    }
}