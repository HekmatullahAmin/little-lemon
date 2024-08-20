package com.hekmatullahamin.littlelemon

import androidx.navigation.NavController
import org.junit.Assert.assertEquals

//this is a helper method
//writing helper methods in your tests saves you from writing duplicate code.
fun NavController.assertCurrentRouteName(expectedRouteName: String) {
//    In this function, assert that the expectedRouteName is equal to the destination route of the nav controller's current back stack entry.
    assertEquals(expectedRouteName, currentBackStackEntry?.destination?.route)
}