package com.hekmatullahamin.littlelemon.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hekmatullahamin.littlelemon.AppViewModelProvider
import com.hekmatullahamin.littlelemon.R
import com.hekmatullahamin.littlelemon.data.models.Address
import com.hekmatullahamin.littlelemon.ui.theme.LittleLemonTheme
import com.hekmatullahamin.littlelemon.ui.common_ui.ActionButton
import com.hekmatullahamin.littlelemon.ui.common_ui.CustomTextField
import com.hekmatullahamin.littlelemon.ui.state.AddressUiState
import com.hekmatullahamin.littlelemon.ui.state.BottomSheetUiState
import kotlinx.coroutines.launch

/*Future improvement:
if there will be one address remaining set it to default programmatically*/

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressScreen(
    addressViewModel: AddressViewModel = viewModel(factory = AppViewModelProvider.Factory),
    modifier: Modifier = Modifier
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember {
        mutableStateOf(false)
    }

    val addresses = addressViewModel.addressUiState.collectAsState()
    val bottomSheetUiState = addressViewModel.bottomSheetUiState

    Scaffold(
        bottomBar = {
//            will open bottom sheet for adding address
            ActionButton(
                text = stringResource(id = R.string.add_address_button_label),
                onButtonClick = {
                    showBottomSheet = true
                    addressViewModel.updateUiState(
                        bottomSheetUiState.copy(
                            bottomSheetTitle = R.string.add_new_address_title_label,
                            bottomSheetButtonLabel = R.string.save_address_button_label
                        )
                    )
                },
                modifier = Modifier.padding(dimensionResource(id = R.dimen.action_button_padding))
            )
        }
    ) { contentPadding ->
        when (val state = addresses.value) {
            is AddressUiState.Loading -> {

            }

            is AddressUiState.Success -> {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.small_vertical_spacing)),
                    modifier = modifier.padding(contentPadding)
                ) {
                    items(items = state.addresses, key = { address -> address.addressId }) { address ->
                        AddressCard(
                            address = address,
//                          will open bottom sheet for editing the existing address
                            onEditClicked = {
                                showBottomSheet = true
                                addressViewModel.updateUiState(
                                    bottomSheetUiState.copy(
                                        bottomSheetTitle = R.string.edit_my_address_title_label,
                                        bottomSheetButtonLabel = R.string.edit_address_button_label,
//                                      to fill the textfields of bottom sheet with this clicked address details
                                        addressDetails = address
                                    )
                                )
                            },
                            onRemoveClicked = {
                                scope.launch {
                                    addressViewModel.deleteAddress(address)
                                }
                            },
                            onSetAsDefaultClicked = {
//                              click to set our default address
                                scope.launch {
                                    addressViewModel.setDefaultAddress(address)
                                }
                            }
                        )
                    }
                }
            }

            is AddressUiState.Error -> {

            }
        }
    }

    if (showBottomSheet) {
        AddOrEditAddressBottomSheet(
            uiState = bottomSheetUiState,
            sheetState = sheetState,
            onDismissRequest = {
                showBottomSheet = false
            },
            onActionButtonClicked = {
                scope.launch {
//                      we check if the title of bottom sheet is edit then update
//                        otherwise insert the new address
                    if (bottomSheetUiState.bottomSheetTitle == R.string.edit_my_address_title_label) {
                        addressViewModel.updateAddress(bottomSheetUiState.addressDetails)
                    } else {
                        addressViewModel.insertAddress(bottomSheetUiState.addressDetails)
                        addressViewModel.resetBottomSheet()
                    }
                    sheetState.hide()
                }.invokeOnCompletion {
                    if (!sheetState.isVisible) {
                        showBottomSheet = false
                    }
                }
            },
            onValueChange = {
                addressViewModel.updateUiState(
                    bottomSheetUiState.copy(
                        addressDetails = it
                    )
                )
            },
            modifier = Modifier.padding(dimensionResource(id = R.dimen.screens_padding))
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AddressScreenPreview() {
    LittleLemonTheme {
        AddressScreen(
            modifier = Modifier.padding(dimensionResource(id = R.dimen.screens_padding))
        )
    }
}

@Composable
fun AddressCard(
    address: Address,
    onEditClicked: () -> Unit,
    onRemoveClicked: () -> Unit,
    onSetAsDefaultClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        border = BorderStroke(
            width = dimensionResource(id = R.dimen.card_border_stroke_width),
            color = MaterialTheme.colorScheme.primary
        )
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.small_vertical_spacing)),
            modifier = Modifier.padding(dimensionResource(id = R.dimen.card_inside_padding))
        ) {
            if (address.isDefault) {
                Text(text = stringResource(id = R.string.default_address_label))
                HorizontalDivider(thickness = dimensionResource(id = R.dimen.horizontal_divider_thickness))
            }
            Text(
                text = "${address.addressLineOne}\n" +
                        "${address.addressLineTwo}\n" +
                        "${address.city}, ${address.postalCode}\n" +
                        address.country
            )
            Row(horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.small_horizontal_spacing))) {
                OutlinedButton(onClick = onEditClicked) {
                    Text(text = stringResource(id = R.string.edit_label))
                }
                OutlinedButton(onClick = onRemoveClicked) {
                    Text(text = stringResource(id = R.string.remove_text_button_label))
                }
                if (!address.isDefault)
                    OutlinedButton(onClick = onSetAsDefaultClicked) {
                        Text(text = stringResource(id = R.string.set_as_default_text_button_label))
                    }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddressCardPreview() {
    LittleLemonTheme {
        AddressCard(
            address = Address(
                addressId = 1,
                addressLineOne = "Mercure Brigstow Hotel 5 7",
                addressLineTwo = "Welsh Back",
                city = "Bristol",
                country = "United Kingdom",
                postalCode = "BS1 4SP"
            ),
            onEditClicked = {},
            onRemoveClicked = {},
            onSetAsDefaultClicked = {}
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddOrEditAddressBottomSheet(
    uiState: BottomSheetUiState,
    onValueChange: (Address) -> Unit,
    sheetState: SheetState,
    onDismissRequest: () -> Unit,
    onActionButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState
    ) {
        Column(modifier = modifier.verticalScroll(rememberScrollState())) {
            Text(
                text = stringResource(id = uiState.bottomSheetTitle),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.medium_vertical_spacing)))

            Text(text = stringResource(id = R.string.address_line_1))
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.small_vertical_spacing)))
            CustomTextField(
                value = uiState.addressDetails.addressLineOne,
                onValueChange = {
                    onValueChange(
                        uiState.addressDetails.copy(
                            addressLineOne = it
                        )
                    )
                },
                placeholder = R.string.enter_address_line_one_placeholder
            )

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.medium_vertical_spacing)))

            Text(text = stringResource(id = R.string.address_line_2))
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.small_vertical_spacing)))
            CustomTextField(
                value = uiState.addressDetails.addressLineTwo,
                onValueChange = {
                    onValueChange(
                        uiState.addressDetails.copy(
                            addressLineTwo = it
                        )
                    )
                },
                placeholder = R.string.enter_address_line_two_placeholder
            )

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.medium_vertical_spacing)))

            Text(text = stringResource(id = R.string.city_or_town))
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.small_vertical_spacing)))
            CustomTextField(
                value = uiState.addressDetails.city,
                onValueChange = {
                    onValueChange(
                        uiState.addressDetails.copy(
                            city = it
                        )
                    )
                },
                placeholder = R.string.enter_city_or_town_placeholder
            )

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.medium_vertical_spacing)))

            Text(text = stringResource(id = R.string.postal_code))
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.small_vertical_spacing)))
            CustomTextField(
                value = uiState.addressDetails.postalCode,
                onValueChange = {
                    onValueChange(
                        uiState.addressDetails.copy(
                            postalCode = it
                        )
                    )
                },
                placeholder = R.string.enter_postal_code_placeholder
            )

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.medium_vertical_spacing)))

            Text(text = stringResource(id = R.string.country))
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.small_vertical_spacing)))
            CustomTextField(
                value = uiState.addressDetails.country,
                onValueChange = {
                    onValueChange(
                        uiState.addressDetails.copy(
                            country = it
                        )
                    )
                },
                placeholder = R.string.enter_country_placeholder
            )

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.medium_vertical_spacing)))

            ActionButton(
                text = stringResource(id = uiState.bottomSheetButtonLabel),
                onButtonClick = onActionButtonClicked,
                enabled = uiState.isEntryValid,
                modifier = Modifier.padding(dimensionResource(id = R.dimen.action_button_padding))
            )
        }
    }
}