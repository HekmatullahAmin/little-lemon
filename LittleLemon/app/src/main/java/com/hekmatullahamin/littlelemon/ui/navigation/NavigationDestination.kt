package com.hekmatullahamin.littlelemon.ui.navigation

import com.hekmatullahamin.littlelemon.R

/**
 * Interface to describe the navigation destinations for the app
 */
interface NavigationDestination {
    /**
     * Unique name to define the path for a composable
     */
    val route: String

    /**
     * String resource id to that contains title to be displayed for the screen.
     */
    val titleRes: Int?
}

object StartDestination : NavigationDestination {
    override val route = "start"
    override val titleRes: Int? = null
}

object RegisterDestination : NavigationDestination {
    override val route = "register"
    override val titleRes: Int? = null
}

object LoginDestination : NavigationDestination {
    override val route = "login"
    override val titleRes: Int? = null
}

object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleRes: Int? = null
    const val userIdArg = "userId"
    val routeWithArgs = "$route/{$userIdArg}"
}

object MenuItemDestination : NavigationDestination {
    override val route = "menu_item"
    override val titleRes: Int? = null
    const val menuItemIdArg = "menuItemId"
    val routeWithArgs = "$route/{$menuItemIdArg}"
}

object CartDestination : NavigationDestination {
    override val route = "cart"
    override val titleRes = R.string.top_app_bar_my_cart_title
}

object CheckoutDestination : NavigationDestination {
    override val route = "checkout"
    override val titleRes = R.string.top_app_bar_checkout_title
    const val userIdArg = "menuItemId"
    val routeWithArgs = "$route/{$userIdArg}"
}

object OrderCompleteDestination : NavigationDestination {
    override val route = "order_complete"
    override val titleRes: Int? = null
}

object ProfileDestination : NavigationDestination {
    override val route = "profile"
    override val titleRes = R.string.top_app_bar_profile_title
    const val userIdArg = "userId"
    val routeWithArgs = "$route/{$userIdArg}"

}

object EditProfileDestination : NavigationDestination {
    override val route = "edit_profile"
    override val titleRes: Int? = null
    const val userIdArg = "userId"
    val routeWithArgs = "$route/{$userIdArg}"
}

object AddressDestination : NavigationDestination {
    override val route = "address"
    override val titleRes = R.string.top_app_bar_your_addressed_title
    const val userIdArg = "userId"
    val routeWithArgs = "$route/{$userIdArg}"
}

object ThemeDestination : NavigationDestination {
    override val route = "theme"
    override val titleRes = R.string.theme_label
}
