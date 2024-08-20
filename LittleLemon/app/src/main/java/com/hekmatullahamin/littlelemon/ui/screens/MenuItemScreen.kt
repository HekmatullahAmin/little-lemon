package com.hekmatullahamin.littlelemon.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hekmatullahamin.littlelemon.AppViewModelProvider
import com.hekmatullahamin.littlelemon.R
import com.hekmatullahamin.littlelemon.data.models.MenuItemRoom
import com.hekmatullahamin.littlelemon.ui.common_ui.ActionButton
import com.hekmatullahamin.littlelemon.ui.common_ui.ItemQuantitySelector
import com.hekmatullahamin.littlelemon.ui.state.MenuItemUiState
import com.hekmatullahamin.littlelemon.ui.theme.LittleLemonTheme
import com.hekmatullahamin.littlelemon.utils.formatAsCurrency

enum class SpiceLevel {
    MILD,
    MEDIUM,
    HOT;

    companion object {
        fun fromDisplayName(displayName: String): SpiceLevel {
            return when (displayName) {
                "Mild" -> MILD
                "Medium" -> MEDIUM
                "Hot" -> HOT
                else -> throw IllegalArgumentException("Unknown display name: $displayName")
            }
        }
    }
}

enum class Side(val price: Double) {
    CULT_CHAPATI(price = 5.49),
    THIN_WHEAT_BREAD(price = 4.98),
    LAYERED_FLAT_BREAD(price = 6.59);

    companion object {
        fun fromDisplayName(displayName: String): Side {
            return when (displayName) {
                "Cult Chapati (+ $5.49)" -> CULT_CHAPATI
                "Thin Wheat Bread (+ $4.95)" -> THIN_WHEAT_BREAD
                "Layered Flat Bread (+ $6.59)" -> LAYERED_FLAT_BREAD
                else -> throw IllegalArgumentException("Unknown display name: $displayName")
            }
        }
    }
}

@Composable
fun MenuItemScreen(
    navigateUp: () -> Unit,
    onAddToBagButtonClicked: (MenuItemUiState) -> Unit,
    menuItemViewModel: MenuItemViewModel = viewModel(factory = AppViewModelProvider.Factory),
    modifier: Modifier = Modifier
) {
    val menuItemUiState = menuItemViewModel.menuItemUiState.collectAsState().value

    Scaffold(
        bottomBar = {
            ActionButton(
                if (menuItemUiState.isSpiceLevelSelected) {
                    stringResource(
                        id = R.string.add_to_cart,
                        formatAsCurrency(menuItemUiState.totalCost)
                    )
                } else {
                    stringResource(id = R.string.make_required_choice_button_label)
                },
                onButtonClick = {
                    onAddToBagButtonClicked(menuItemUiState)
                },
                enabled = menuItemUiState.isSpiceLevelSelected,
                modifier = Modifier.padding(dimensionResource(id = R.dimen.action_button_padding))
            )
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .padding(contentPadding)
                .verticalScroll(rememberScrollState())
        ) {
            ItemInfoDisplay(
                navigateUp = navigateUp,
                menuItem = menuItemUiState.menuItem
            )
            Text(
                text = stringResource(id = R.string.quantity_label),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(start = dimensionResource(id = R.dimen.screens_padding))
            )
            ItemQuantitySelector(
                count = menuItemUiState.quantity,
                onIncrement = {
                    menuItemViewModel.updateQuantity(
                        menuItemUiState.quantity + 1
                    )
                },
                onDecrement = {
//                    only decrease if the quantity is bigger than 1
                    if (menuItemUiState.quantity > 1) {
                        menuItemViewModel.updateQuantity(menuItemUiState.quantity - 1)
                    }
                },
                modifier = modifier
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.small_vertical_spacing)))
            HorizontalDivider(thickness = dimensionResource(id = R.dimen.horizontal_divider_thickness))
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.small_vertical_spacing)))

            RequiredSpiceLevel(
                selectedSpiceLevel = menuItemUiState.spiceLevel,
                onSelectedSpiceLevel = {
                    menuItemViewModel.updateSpiceLevel(SpiceLevel.fromDisplayName(it))
                },
                modifier = Modifier.padding(
                    start = dimensionResource(id = R.dimen.screens_padding),
                    end = dimensionResource(id = R.dimen.screens_padding)
                )
            )

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.small_vertical_spacing)))
            OptionalSides(
                selectedSides = menuItemUiState.selectedSides,
                onSelectedSide = { sideName, isSelected ->
                    menuItemViewModel.updateSideSelection(
                        Side.fromDisplayName(sideName),
                        isSelected
                    )
                },
                modifier = Modifier.padding(
                    start = dimensionResource(id = R.dimen.screens_padding),
                    end = dimensionResource(id = R.dimen.screens_padding)
                )
            )
        }
    }
}

@Composable
fun ItemInfoDisplay(
    navigateUp: () -> Unit,
    menuItem: MenuItemRoom,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(dimensionResource(id = R.dimen.screens_padding))
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(
                            id = R.string.back_button_content_description
                        )
                    )
                }
                Text(
                    text = menuItem.itemName,
                    style = MaterialTheme.typography.headlineLarge
                )
                Spacer(modifier = Modifier.weight(1F))
                Text(
                    text = formatAsCurrency(menuItem.itemPrice),
                    style = MaterialTheme.typography.headlineMedium
                )
            }
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.small_vertical_spacing)))
            Text(
                text = menuItem.itemDescription,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ItemInfoDisplayPreview() {
    LittleLemonTheme {
        ItemInfoDisplay(
            navigateUp = {},
            menuItem = MenuItemRoom(
                1,
                "Burger",
                22.341,
                "It is really delicious. Try it with salads and coke"
            ),
            modifier = Modifier.padding(dimensionResource(id = R.dimen.screens_padding))
        )
    }
}

@Composable
fun RequiredSpiceLevel(
    selectedSpiceLevel: SpiceLevel? = null,
    onSelectedSpiceLevel: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val spiceLevels = stringArrayResource(id = R.array.spice_levels)
    var isSpiceLevelVisible by remember {
        mutableStateOf(true)
    }
    Column(verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.small_vertical_spacing))) {
        Text(
            text = stringResource(id = R.string.required_label),
            style = MaterialTheme.typography.headlineMedium,
            modifier = modifier
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
        ) {
            Text(
                text = stringResource(id = R.string.choose_spice_level_label),
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.weight(1F))
            IconButton(onClick = {
                isSpiceLevelVisible = !isSpiceLevelVisible
            }) {
                Icon(
                    imageVector = if (isSpiceLevelVisible) Icons.Default.KeyboardArrowUp
                    else Icons.Default.KeyboardArrowDown,
                    contentDescription = stringResource(id = R.string.arrow_down_content_description)
                )
            }
        }

        AnimatedVisibility(
            visible = isSpiceLevelVisible,
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            Column {
                spiceLevels.forEach { spiceName ->
                    val spiceLevelEnum = SpiceLevel.fromDisplayName(spiceName)
                    SpiceRadioButton(
                        spiceName = spiceName,
                        isSelected = selectedSpiceLevel == spiceLevelEnum,
                        onSelect = {
                            onSelectedSpiceLevel(spiceName)
                        },
                        modifier = modifier
//                        modifier = modifier.testTag("spiceLevelsTag")
                    )
                    HorizontalDivider(thickness = dimensionResource(id = R.dimen.horizontal_divider_thickness))
                }
            }
        }
//        if we hide spice level then there should be a divider in between Required spice level and optional Add Side
        if (!isSpiceLevelVisible) {
            HorizontalDivider(thickness = dimensionResource(id = R.dimen.horizontal_divider_thickness))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RequiredSpiceLevelPreview() {
    LittleLemonTheme {
        RequiredSpiceLevel(
            selectedSpiceLevel = null,
            onSelectedSpiceLevel = {},
            modifier = Modifier.padding(
                start = dimensionResource(id = R.dimen.screens_padding),
                end = dimensionResource(id = R.dimen.screens_padding)
            )
        )
    }
}

@Composable
fun SpiceRadioButton(
    spiceName: String,
    isSelected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
    ) {
        Text(text = spiceName)
        RadioButton(
            selected = isSelected,
            onClick = onSelect,
            modifier = Modifier.testTag("radioTag")
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SpiceRadioButtonPreview() {
    LittleLemonTheme {
        SpiceRadioButton(
            spiceName = "Mild",
            isSelected = false,
            onSelect = {}
        )
    }
}

@Composable
fun OptionalSides(
    selectedSides: List<Side>,
    onSelectedSide: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val spiceLevels = stringArrayResource(id = R.array.sides)
    var isSidesVisible by remember {
        mutableStateOf(false)
    }
    Column(verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.small_vertical_spacing))) {
        Text(
            text = stringResource(id = R.string.optional_choices_label),
            style = MaterialTheme.typography.headlineMedium,
            modifier = modifier
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
        ) {
            Text(
                text = stringResource(id = R.string.add_side_label),
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.weight(1F))
            IconButton(onClick = {
                isSidesVisible = !isSidesVisible
            }) {
                Icon(
                    imageVector = if (isSidesVisible) Icons.Default.KeyboardArrowUp
                    else Icons.Default.KeyboardArrowDown,
                    contentDescription = stringResource(id = R.string.arrow_down_content_description)
                )
            }
        }
        AnimatedVisibility(
            visible = isSidesVisible,
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            Column {
                spiceLevels.forEach { sideDisplayName ->
                    val sideEnum = Side.fromDisplayName(sideDisplayName)
                    SideCheckBox(
                        sideName = sideDisplayName,
                        isChecked = selectedSides.contains(sideEnum),
                        onChecked = { isChecked ->
                            onSelectedSide(sideDisplayName, isChecked)
                        },
                        modifier = modifier
                    )
                    HorizontalDivider(thickness = dimensionResource(id = R.dimen.horizontal_divider_thickness))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OptionalSidesPreview() {
    LittleLemonTheme {
        OptionalSides(
            selectedSides = emptyList(),
            onSelectedSide = { _, _ -> },
            modifier = Modifier.padding(
                start = dimensionResource(id = R.dimen.screens_padding),
                end = dimensionResource(id = R.dimen.screens_padding)
            )
        )
    }
}

@Composable
fun SideCheckBox(
    sideName: String,
    isChecked: Boolean,
    onChecked: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
    ) {
        Text(text = sideName)
        Checkbox(
            checked = isChecked,
            onCheckedChange = {
                onChecked(it)
            },
            modifier = Modifier.testTag("checkboxTag")
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SideCheckBoxPreview() {
    LittleLemonTheme {
        SideCheckBox(
            sideName = "Cult Chapati (+5.49)",
            isChecked = false,
            onChecked = {}
        )
    }
}


