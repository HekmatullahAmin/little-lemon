package com.hekmatullahamin.littlelemon

import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.hekmatullahamin.littlelemon.ui.navigation.AddressDestination
import com.hekmatullahamin.littlelemon.ui.navigation.CartDestination
import com.hekmatullahamin.littlelemon.ui.navigation.CheckoutDestination
import com.hekmatullahamin.littlelemon.ui.navigation.EditProfileDestination
import com.hekmatullahamin.littlelemon.ui.navigation.HomeDestination
import com.hekmatullahamin.littlelemon.ui.navigation.LoginDestination
import com.hekmatullahamin.littlelemon.ui.navigation.MenuItemDestination
import com.hekmatullahamin.littlelemon.ui.navigation.NavigationDestination
import com.hekmatullahamin.littlelemon.ui.navigation.OrderCompleteDestination
import com.hekmatullahamin.littlelemon.ui.navigation.ProfileDestination
import com.hekmatullahamin.littlelemon.ui.navigation.RegisterDestination
import com.hekmatullahamin.littlelemon.ui.navigation.StartDestination
import com.hekmatullahamin.littlelemon.ui.navigation.ThemeDestination
import com.hekmatullahamin.littlelemon.ui.screens.AddressScreen
import com.hekmatullahamin.littlelemon.ui.screens.AddressViewModel
import com.hekmatullahamin.littlelemon.ui.screens.CartScreen
import com.hekmatullahamin.littlelemon.ui.screens.CheckoutScreen
import com.hekmatullahamin.littlelemon.ui.screens.EditProfileScreen
import com.hekmatullahamin.littlelemon.ui.screens.HomeScreen
import com.hekmatullahamin.littlelemon.ui.screens.LoginScreen
import com.hekmatullahamin.littlelemon.ui.screens.MenuItemScreen
import com.hekmatullahamin.littlelemon.ui.screens.OrderCompleteScreen
import com.hekmatullahamin.littlelemon.ui.screens.OrderViewModel
import com.hekmatullahamin.littlelemon.ui.screens.ProfileScreen
import com.hekmatullahamin.littlelemon.ui.screens.RegisterScreen
import com.hekmatullahamin.littlelemon.ui.screens.StartScreen
import com.hekmatullahamin.littlelemon.ui.screens.ThemeScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyCenterAlignedAppBar(
    currentScreen: NavigationDestination,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
//    if we already have top app bar in our other screens so we don't need for this CenterAlignedTopAppBar
    if (currentScreen.titleRes != null) {
        CenterAlignedTopAppBar(
            title = {
                Text(text = stringResource(id = currentScreen.titleRes!!))
            },
            navigationIcon = {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(
                            id = R.string.back_button_content_description
                        )
                    )
                }
            },
            modifier = modifier
        )
    }
}

@Composable
fun MyLittleLemonApp(
    navController: NavHostController = rememberNavController(),
    orderViewModel: OrderViewModel = viewModel(factory = AppViewModelProvider.Factory),
    modifier: Modifier = Modifier
) {

//    val navController: NavController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = when (backStackEntry?.destination?.route) {
//        route = start
        StartDestination.route -> StartDestination
        RegisterDestination.route -> RegisterDestination
        LoginDestination.route -> LoginDestination
//        route = home/{userId}
        HomeDestination.route -> HomeDestination
//        route = menuItem/{menuItemId}
        MenuItemDestination.routeWithArgs -> MenuItemDestination
//        route = cart
        CartDestination.route -> CartDestination
//        route = checkout/{menuItemId}
//        so instead of route we should use routeWithArgs
        CheckoutDestination.routeWithArgs -> CheckoutDestination
        OrderCompleteDestination.route -> OrderCompleteDestination
        ProfileDestination.routeWithArgs -> ProfileDestination
        EditProfileDestination.route -> EditProfileDestination
        AddressDestination.routeWithArgs -> AddressDestination
        ThemeDestination.route -> ThemeDestination
        else -> StartDestination
    }
    val scope = rememberCoroutineScope()
    val snackBarHostState = remember {
        SnackbarHostState()
    }

    val orderUiState by orderViewModel.orderUiState.collectAsState()

    Scaffold(
        topBar = {
            MyCenterAlignedAppBar(
                currentScreen = currentScreen,
                navigateUp = { navController.navigateUp() }
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        }
    ) { contentPadding ->
        NavHost(
            navController = navController,
            startDestination = StartDestination.route,
            modifier = modifier
                .padding(contentPadding)
                .padding(dimensionResource(id = R.dimen.screens_padding))
        ) {
            composable(route = StartDestination.route) {
                StartScreen(
                    onLoginButtonClicked = { navController.navigate(LoginDestination.route) },
                    onCreateAccountClicked = { navController.navigate(RegisterDestination.route) }
                )
            }

            composable(route = RegisterDestination.route) {
                RegisterScreen(
                    onCreateMyAccountButtonClicked = { userId ->
//                        popUpTo(HomeDestination.route) specifies that the navigation back stack should be popped up to (and including) the Home screen.
//                        inclusive = true ensures that the Home screen itself is removed from the back stack.
//                        /${userId} or /$userId
                        orderViewModel.setUserId(userId)
                        navController.navigate("${HomeDestination.route}/${userId}") {
                            popUpTo(route = HomeDestination.route) {
                                inclusive = true
                            }
                        }
                    },
                    onLoginTextClicked = { navController.navigate(LoginDestination.route) }
                )
            }

            composable(route = LoginDestination.route) {
                // Fetch the string resource in a composable context
                val snackBarMessage = stringResource(id = R.string.snackbar_message)
                LoginScreen(
                    onLoginButtonClicked = { user ->
//                        if credential are same then login and go to home screen else show snackbar
                        if (user != null) {
                            orderViewModel.setUserId(user.userId)
                            navController.navigate("${HomeDestination.route}/${user.userId}") {
                                popUpTo(route = HomeDestination.route) {
                                    inclusive = true
                                }
                            }
                        } else {
                            scope.launch {
                                snackBarHostState.showSnackbar(
                                    message = snackBarMessage,
                                    duration = SnackbarDuration.Short
                                )
                            }
                        }
                    },
                    onJoinTextClicked = { navController.navigate(RegisterDestination.route) }
                )
            }

            composable(
                route = HomeDestination.routeWithArgs,
                arguments = listOf(navArgument(HomeDestination.userIdArg) {
                    type = NavType.IntType
                    defaultValue = -1
                })
            ) {
                val snackBarMessage = stringResource(id = R.string.already_in_cart)
                HomeScreen(
                    onMenuItemClicked = { itemId ->

                        // check at least one element in the cartItems list has the the menuItem with the currently clicked menuItem
                        val isItemInCart = orderUiState.cartItems.any { menuItemUiState ->
                            menuItemUiState.menuItem.itemId == itemId
                        }

                        if (isItemInCart) {
                            // Item is already in the cart
                            scope.launch {
                                snackBarHostState.showSnackbar(
                                    message = snackBarMessage,
                                    duration = SnackbarDuration.Short
                                )
                            }

                        } else {
                            // Item is not in the cart, proceed with navigation
                            navController.navigate("${MenuItemDestination.route}/$itemId")
                        }
                    },
                    onProfilePictureClicked = {
                        navController.navigate("${ProfileDestination.route}/${orderViewModel.currentUserId}")
                    },
//                    show the cart card in the below if any item is added to order
                    isItemToOrder = orderUiState.cartItems.isNotEmpty(),
                    onCartCardClicked = { navController.navigate(CartDestination.route) }
                )
            }

            composable(route = MenuItemDestination.routeWithArgs,
                arguments = listOf(
                    navArgument(
                        name = MenuItemDestination.menuItemIdArg
                    ) {
                        type = NavType.IntType
                    }
                )
            ) {
                MenuItemScreen(
                    navigateUp = { navController.navigateUp() },
                    onAddToBagButtonClicked = {
//                        we will add the menuItem, quantity, spice level and sides
//                        which together is MenuItemUiState to OrderViewModels cartItems
                        orderViewModel.addItemToCart(it)
                        navController.navigateUp()
                    }
                )
            }

            composable(route = CartDestination.route) {
                CartScreen(
                    orderUiState = orderUiState,
                    onAddPromoCodeCardClicked = {
//                   TODO: implement add promo code
                    },
                    onIncrement = { orderViewModel.incrementItemQuantity(it) },
                    onDecrement = { orderViewModel.decrementItemQuantity(it) },
                    onCheckoutButtonClicked = { navController.navigate("${CheckoutDestination.route}/${orderViewModel.currentUserId}") }
                )
            }

            composable(route = CheckoutDestination.routeWithArgs,
                arguments = listOf(
                    navArgument(
                        name = CheckoutDestination.userIdArg
                    ) {
                        type = NavType.IntType
                    }
                )
            ) {
                CheckoutScreen(
                    onSelectAddressClicked = {
                        navController.navigate("${AddressDestination.route}/${orderViewModel.currentUserId}")
                    },
                    subtotal = orderUiState.subtotal,
                    onPayNowButtonClicked = {
                        orderViewModel.saveOrder()
                        navController.navigate(
                            OrderCompleteDestination.route
                        )
                    })
            }

            composable(route = OrderCompleteDestination.route) {
                OrderCompleteScreen(
//                    just want to to pass the 10% of total which will be the delivery fee
                    totalAmount = orderUiState.subtotal + orderUiState.subtotal * 0.1,
                    onOrderAgainButtonClicked = {
//                        after ordering we want to reset the viewmodel
                        orderViewModel.resetUiState()
                        navController.navigate("${HomeDestination.route}/${orderViewModel.currentUserId}")
                    })
            }

            composable(
                route = ProfileDestination.routeWithArgs,
                arguments = listOf(navArgument(name = ProfileDestination.userIdArg) {
                    type = NavType.IntType
                })
            ) {
                ProfileScreen(
                    onLogoutButtonClicked = {
                        navController.navigate(StartDestination.route) {
                            popUpTo(route = StartDestination.route) {
                                inclusive = true
                            }
                        }
                    },
                    onAccountCardClicked = {
                        navController.navigate("${EditProfileDestination.route}/${orderViewModel.currentUserId}")
                    },
                    onThemeCardClicked = { navController.navigate(ThemeDestination.route) }
                )
            }

            composable(route = EditProfileDestination.routeWithArgs,
                listOf(
                    navArgument(name = EditProfileDestination.userIdArg) {
                        type = NavType.IntType
                    }
                )
            ) {
                EditProfileScreen(
                    onNavigateUpClicked = { navController.navigateUp() },
                    onAddressClicked = { navController.navigate("${AddressDestination.route}/${orderViewModel.currentUserId}") }
                )
            }

            composable(
                route = AddressDestination.routeWithArgs,
                listOf(
                    navArgument(name = AddressDestination.userIdArg) {
                        type = NavType.IntType
                    }
                )
            ) {
                AddressScreen()
            }

            composable(route = ThemeDestination.route) {
                ThemeScreen()
            }
        }
    }
}

