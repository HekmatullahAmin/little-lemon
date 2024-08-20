package com.hekmatullahamin.littlelemon.ui.screens

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.hekmatullahamin.littlelemon.AppViewModelProvider
import com.hekmatullahamin.littlelemon.R
import com.hekmatullahamin.littlelemon.data.models.MenuItemRoom
import com.hekmatullahamin.littlelemon.ui.state.HomeUiState
import com.hekmatullahamin.littlelemon.ui.theme.LittleLemonTheme
import com.hekmatullahamin.littlelemon.ui.theme.primaryDark
import com.hekmatullahamin.littlelemon.ui.theme.primaryLight
import com.hekmatullahamin.littlelemon.utils.formatAsCurrency

@Composable
fun HomeScreen(
    onProfilePictureClicked: () -> Unit,
    onMenuItemClicked: (Int) -> Unit,
    isItemToOrder: Boolean = false,
    onCartCardClicked: () -> Unit,
    homeScreenViewModel: HomeScreenViewModel = viewModel(factory = AppViewModelProvider.Factory),
    modifier: Modifier = Modifier
) {
    val uiState = homeScreenViewModel.homeUiState.collectAsState()
    val user = homeScreenViewModel.user.collectAsState()

    Scaffold(topBar = {
        HomeScreenTopAppBar(
            imageUri = user.value.userImageUri ?: "",
            onProfilePictureClicked = onProfilePictureClicked,
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.top_app_bar_padding))
                .fillMaxWidth()
        )
    }) { paddingValues ->
        Box(modifier = modifier
            .fillMaxHeight()
            .padding(paddingValues)) {

            /**
             * Note:
             * The previous implementation used a `Column` inside a `Box` to organize the UI elements,
             * with an attempt to make the screen vertically scrollable by applying a `Modifier.verticalScroll()`.
             * However, this caused a `java.lang.IllegalStateException` due to infinite height constraints
             * when combined with nested scrollable components like `LazyColumn`.
             *
             * To resolve this, the UI structure was refactored to use a single `LazyColumn`, which natively supports
             * vertical scrolling. The `item {}` blocks are used to include non-list components (e.g., `AboutLittleLemon()`,
             * `OutlinedTextField`, etc.) alongside list items. This approach avoids the infinite constraint issue
             * and simplifies the UI hierarchy.
             */
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.small_vertical_spacing))
            ) {
                item {
                    AboutLittleLemon()
                }
                item {
                    OutlinedTextField(
                        value = homeScreenViewModel.searchPhrase,
                        onValueChange = {
                            homeScreenViewModel.updateSearchPhrase(it)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Search, contentDescription = null)
                        },
                        placeholder = {
                            Text(text = stringResource(id = R.string.enter_item_name_placeholder))
                        }
                    )
                }
                item {
                    Text(
                        text = stringResource(id = R.string.category_label),
                        style = MaterialTheme.typography.headlineMedium
                    )
                }

                item {
                    MenuSectionsButtons(
                        categories = homeScreenViewModel.getCategories(),
                        onCategoryClicked = {
                            homeScreenViewModel.updateCategory(it)
                        }
                    )
                }

                item {
                    HorizontalDivider(thickness = dimensionResource(id = R.dimen.horizontal_divider_thickness))
                }

                when (val state = uiState.value) {
                    is HomeUiState.Success -> {
                        items(state.menuItems) { item ->
                            ItemRow(
                                menuItem = item,
                                onItemClicked = { onMenuItemClicked(item.itemId) }
                            )
                            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.medium_vertical_spacing)))
                        }
                    }

                    is HomeUiState.Loading -> {
                        item {
                            LoadingScreen(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .testTag("LoadingScreen")
                            )
                        }
                    }

                    is HomeUiState.Error -> {

                    }
                }
            }

            if (isItemToOrder) {
                CartCard(
                    onCardClicked = onCartCardClicked,
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    LittleLemonTheme {
        HomeScreen(
            onProfilePictureClicked = {},
            onMenuItemClicked = {},
            onCartCardClicked = {},
            modifier = Modifier.padding(dimensionResource(id = R.dimen.screens_padding))
        )
    }
}

@Composable
fun HomeScreenTopAppBar(
    imageUri: String,
    onProfilePictureClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
//    we used box instead of row because we want specific positions of images. for ex: the logo will be in
//    the center no matter where the profile picture will be
    Box(
        modifier = modifier
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = null,
            modifier = Modifier
                .size(
                    width = dimensionResource(id = R.dimen.appbar_logo_width),
                    height = dimensionResource(id = R.dimen.appbar_logo_height)
                )
                .align(Alignment.Center)
        )

        AsyncImage(
            model = ImageRequest.Builder(context = LocalContext.current)
                .data(imageUri)
                .crossfade(true)
                .build(),
            contentDescription = stringResource(id = R.string.profile_picture_content_description),
            contentScale = ContentScale.Crop,
            placeholder = painterResource(id = R.drawable.profile_picture),
            error = painterResource(id = R.drawable.profile_picture),
            modifier = Modifier
                .size(dimensionResource(id = R.dimen.app_bar_profile_image_size))
                .align(Alignment.CenterEnd)
                .clip(RoundedCornerShape(dimensionResource(id = R.dimen.profile_picture_rounded_corner)))
                .clickable { onProfilePictureClicked() }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TopAppBarPreview() {
    LittleLemonTheme {
        HomeScreenTopAppBar(
            imageUri = "content://media/picker/0/com.android.providers.media.photopicker/media/1000000020",
            onProfilePictureClicked = {},
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun AboutLittleLemon(modifier: Modifier = Modifier) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.small_horizontal_spacing)),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.weight(1F),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.small_vertical_spacing))
        ) {
            Text(
                text = stringResource(id = R.string.app_name),
                style = MaterialTheme.typography.displaySmall
            )
            Text(
                text = stringResource(id = R.string.about_the_app),
                style = MaterialTheme.typography.bodyLarge
            )
        }
        Image(
            painter = painterResource(id = R.drawable.little_lemon_background),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(
                width = dimensionResource(id = R.dimen.logo_width),
                height = dimensionResource(id = R.dimen.logo_height)
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AboutLittleLemonPreview() {
    LittleLemonTheme {
        AboutLittleLemon(modifier = Modifier.padding(10.dp))
    }
}

@Composable
fun MenuSectionsButtons(
    categories: List<String>,
    onCategoryClicked: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.small_horizontal_spacing)),
        modifier = modifier
    ) {
        items(categories) { menuCategory ->
            Button(onClick = { onCategoryClicked(menuCategory) }) {
                Text(text = menuCategory)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MenuSectionsButtonPreview() {
    val sampleCategories = listOf("Appetizers", "Main Courses", "Desserts", "Beverages")
    LittleLemonTheme {
        MenuSectionsButtons(
            categories = sampleCategories,
            onCategoryClicked = {})
    }
}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    val shimmerBrush = rememberShimmerBrush()
    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.medium_vertical_spacing)),
        modifier = modifier
    ) {
        repeat(5) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1F)) {
                    Box(modifier = Modifier
                        .height(dimensionResource(id = R.dimen.loading_item_name_and_price_height))
                        .width(dimensionResource(id = R.dimen.loading_item_name_width))
                        .drawBehind { drawRect(shimmerBrush) }) {
                    }
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.small_vertical_spacing)))
                    Box(
                        modifier = Modifier
                            .height(dimensionResource(id = R.dimen.loading_item_description_height))
                            .width(dimensionResource(id = R.dimen.loading_item_description_width))
                            .drawBehind { drawRect(shimmerBrush) }
                    )
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.small_vertical_spacing)))
                    Box(
                        modifier = Modifier
                            .height(dimensionResource(id = R.dimen.loading_item_name_and_price_height))
                            .width(dimensionResource(id = R.dimen.loading_item_price_width))
                            .drawBehind { drawRect(shimmerBrush) }
                    )
                }
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.small_horizontal_spacing)))
                Box(modifier = Modifier
                    .size(dimensionResource(id = R.dimen.item_image_size))
                    .clip(RoundedCornerShape(dimensionResource(id = R.dimen.loading_item_image_corner_radius)))
                    .drawBehind { drawRect(shimmerBrush) }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoadingScreenPreview() {
    LittleLemonTheme {
        LoadingScreen()
    }
}

@Composable
fun rememberShimmerBrush(): Brush {
    val shimmerColors = listOf(
        primaryDark,
        primaryLight,
        primaryDark
    )

    val transition = rememberInfiniteTransition(label = "infinite")
    val transitionAnim = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "transitionAnim"
    )
    return Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(transitionAnim.value - 1000f, 0f),
        end = Offset(transitionAnim.value, 0f)
    )
}


@Composable
fun ItemRow(
    menuItem: MenuItemRoom,
    onItemClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.clickable { onItemClicked() }
    ) {
        Column(modifier = Modifier.weight(1F)) {
            Text(text = menuItem.itemName)
            Text(
                text = menuItem.itemDescription,
                style = MaterialTheme.typography.labelSmall
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.small_vertical_spacing)))
            Text(text = formatAsCurrency(menuItem.itemPrice))
        }
        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.small_horizontal_spacing)))
        Card {
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(menuItem.itemImage)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.loading_img),
                error = painterResource(id = R.drawable.ic_broken_image),
                modifier = Modifier.size(dimensionResource(id = R.dimen.item_image_size))
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ItemRowPreview() {
    LittleLemonTheme {
        ItemRow(
            menuItem = MenuItemRoom(
                itemId = 1,
                itemName = "Lemon",
                itemDescription = "It has a very good taste",
                itemPrice = 20.2,
                itemImage = "https://github.com/Meta-Mobile-Developer-PC/Working-With-Data-API/blob/main/images/greekSalad.jpg?raw=true",
            ),
            onItemClicked = {}
        )
    }
}

@Composable
fun CartCard(
    onCardClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier.clickable { onCardClicked() }) {
        Row(modifier = Modifier.padding(dimensionResource(id = R.dimen.card_inside_padding))) {
            Icon(
                imageVector = Icons.Filled.ShoppingCart,
                contentDescription = stringResource(id = R.string.view_cart_label)
            )
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.small_horizontal_spacing)))
            Text(text = stringResource(id = R.string.view_cart_label))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CardCardPreview() {
    LittleLemonTheme {
        CartCard(onCardClicked = {})
    }
}