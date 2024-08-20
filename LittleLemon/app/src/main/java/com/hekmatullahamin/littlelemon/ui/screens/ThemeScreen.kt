package com.hekmatullahamin.littlelemon.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hekmatullahamin.littlelemon.AppViewModelProvider
import com.hekmatullahamin.littlelemon.R
import com.hekmatullahamin.littlelemon.ui.theme.LittleLemonTheme

enum class ThemeMode {
    LIGHT, DARK, DYNAMIC
}

@Composable
fun ThemeScreen(
    themeViewModel: ThemeViewModel = viewModel(factory = AppViewModelProvider.Factory),
    modifier: Modifier = Modifier
) {
    val themeMode = themeViewModel.themeMode.collectAsState()

    Column(modifier = modifier.verticalScroll(rememberScrollState())) {
        ThemeSelectionRow(
            themeName = stringResource(id = R.string.light_theme_label),
            isSelected = themeMode.value == ThemeMode.LIGHT,
            onNewThemeSelected = {
                themeViewModel.setThemeMode(ThemeMode.LIGHT)
            }
        )
        ThemeSelectionRow(
            themeName = stringResource(id = R.string.dark_theme_label),
            isSelected = themeMode.value == ThemeMode.DARK,
            onNewThemeSelected = { themeViewModel.setThemeMode(ThemeMode.DARK) }
        )
        ThemeSelectionRow(
            themeName = stringResource(id = R.string.dynamic_theme_label),
            isSelected = themeMode.value == ThemeMode.DYNAMIC,
            onNewThemeSelected = { themeViewModel.setThemeMode(ThemeMode.DYNAMIC) }
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ThemeScreenPreview() {
    LittleLemonTheme {
        ThemeScreen(
            modifier = Modifier.padding(dimensionResource(id = R.dimen.screens_padding))
        )
    }
}

@Composable
fun ThemeSelectionRow(
    themeName: String,
    onNewThemeSelected: () -> Unit,
    isSelected: Boolean = false,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier
        .padding(dimensionResource(id = R.dimen.text_view_inner_padding))
        .clickable { onNewThemeSelected() }) {
        Text(
            text = themeName,
            modifier = Modifier.weight(1F)
        )
        if (isSelected) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = stringResource(id = R.string.selected_theme_content_description)
            )
        }
    }
    HorizontalDivider(thickness = dimensionResource(id = R.dimen.horizontal_divider_thickness))
}

@Preview(showBackground = true)
@Composable
fun ThemeSelectionRowPreview() {
    LittleLemonTheme {
        ThemeSelectionRow(themeName = "Light", onNewThemeSelected = {})
    }
}