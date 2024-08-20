package com.hekmatullahamin.littlelemon.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.hekmatullahamin.littlelemon.AppViewModelProvider
import com.hekmatullahamin.littlelemon.R
import com.hekmatullahamin.littlelemon.data.models.User
import com.hekmatullahamin.littlelemon.ui.theme.LittleLemonTheme

@Composable
fun ProfileScreen(
    onLogoutButtonClicked: () -> Unit,
    onAccountCardClicked: () -> Unit,
    onThemeCardClicked: () -> Unit,
    profileScreenViewModel: ProfileScreenViewModel = viewModel(factory = AppViewModelProvider.Factory),
    modifier: Modifier = Modifier
) {
    val user = profileScreenViewModel.user.collectAsState().value

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = {
            it?.let { profileScreenViewModel.updateUserImageUri(user, it) }
        }
    )

    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.medium_vertical_spacing))
    ) {
        ProfileHeader(
            user = user,
            onLogoutButtonClicked = onLogoutButtonClicked,
            onChangeProfilePictureIconClicked = {
                imagePicker.launch(
                    PickVisualMediaRequest(
                        ActivityResultContracts.PickVisualMedia.ImageOnly
                    )
                )
            },
            modifier = Modifier.fillMaxWidth()
        )

        PersonalDetailsSection(onAccountCardClicked = onAccountCardClicked)
        SettingsSection(onThemeCardClicked = onThemeCardClicked)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfileScreenPreview() {
    LittleLemonTheme {
        ProfileScreen(
            onLogoutButtonClicked = {},
            onAccountCardClicked = {},
            onThemeCardClicked = {},
            modifier = Modifier.padding(dimensionResource(id = R.dimen.screens_padding))
        )
    }
}

@Composable
fun ProfileHeader(
    user: User,
    onChangeProfilePictureIconClicked: () -> Unit,
    onLogoutButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.medium_horizontal_spacing)),
        modifier = modifier
    ) {
        Box {
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(user.userImageUri)
                    .crossfade(true)
                    .build(),
                contentDescription = stringResource(id = R.string.profile_picture_content_description),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.profile_picture),
                error = painterResource(id = R.drawable.profile_picture),
                modifier = Modifier
                    .size(dimensionResource(id = R.dimen.profile_screen_profile_image_size))
                    .clip(RoundedCornerShape(dimensionResource(id = R.dimen.profile_picture_rounded_corner)))
            )

            IconButton(
                onClick = onChangeProfilePictureIconClicked,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(
                        x = dimensionResource(id = R.dimen.edit_profile_image_icon_button_offset),
                        y = dimensionResource(id = R.dimen.edit_profile_image_icon_button_offset)
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = stringResource(id = R.string.change_profile_picture_content_description),
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.tertiaryContainer,
                            shape = RoundedCornerShape(dimensionResource(id = R.dimen.profile_picture_rounded_corner))
                        )
                        .padding(dimensionResource(id = R.dimen.edit_profile_image_icon_button_inner_padding))
                )
            }
        }
        Column(verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.small_vertical_spacing))) {
            Text(
                text = stringResource(id = R.string.welcome_back_label),
                style = MaterialTheme.typography.headlineMedium
            )
            Text(text = "${user.userFirstName} ${user.userLastName}")
            Button(onClick = onLogoutButtonClicked) {
                Text(
                    text = stringResource(id = R.string.logout_button_label),
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileHeaderPreview() {
    LittleLemonTheme {
        ProfileHeader(
            user = User(
                userId = 0,
                userFirstName = "Hekmatullah",
                userLastName = "Amin",
                userEmailAddress = "hekmatullah_asmatullah@yahoo.com",
                userPassword = "Hekmat123"
            ),
            onChangeProfilePictureIconClicked = {},
            onLogoutButtonClicked = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.screens_padding))
        )
    }
}

@Composable
fun PersonalDetailsSection(
    onAccountCardClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.small_vertical_spacing))
    ) {
        Text(
            text = stringResource(id = R.string.personal_details_label),
            style = MaterialTheme.typography.headlineMedium
        )
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onAccountCardClicked() },
            border = BorderStroke(
                dimensionResource(id = R.dimen.card_border_stroke_width),
                MaterialTheme.colorScheme.primary
            ),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)
        ) {
            Row(
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.card_inside_padding)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1F)) {
                    Text(
                        text = stringResource(id = R.string.account_label),
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Text(text = stringResource(id = R.string.manage_account_label))
                }
                Icon(
                    painter = painterResource(id = R.drawable.baseline_arrow_forward_24),
                    contentDescription = stringResource(id = R.string.manage_account_arrow_content_description)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PersonalDetailsSectionPreview() {
    LittleLemonTheme {
        PersonalDetailsSection(
            onAccountCardClicked = {},
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.screens_padding))
        )
    }
}

@Composable
fun SettingsSection(
    onThemeCardClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.small_vertical_spacing))
    ) {
        Text(
            text = stringResource(id = R.string.settings_label),
            style = MaterialTheme.typography.headlineMedium
        )
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onThemeCardClicked() },
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer),
            border = BorderStroke(
                dimensionResource(id = R.dimen.card_border_stroke_width),
                MaterialTheme.colorScheme.primary
            )
        ) {
            Row(
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.card_inside_padding)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1F)) {
                    Text(
                        text = stringResource(id = R.string.theme_label),
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Text(text = stringResource(id = R.string.choose_a_theme))
                }
                Icon(
                    painter = painterResource(id = R.drawable.baseline_arrow_forward_24),
                    contentDescription = stringResource(id = R.string.choose_a_theme_arrow_content_description)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsSectionPreview() {
    LittleLemonTheme {
        SettingsSection(
            onThemeCardClicked = {},
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.screens_padding))
        )
    }
}

