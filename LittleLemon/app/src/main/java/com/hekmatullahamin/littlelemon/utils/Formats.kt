package com.hekmatullahamin.littlelemon.utils

import com.hekmatullahamin.littlelemon.data.models.Address
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

//This makes the method public, but indicates to others that it's only public for testing purposes.
fun concatenateAddress(address: Address): String {
    return with(address) {
        "$addressLineOne, $addressLineTwo, $city, $postalCode, $country"
    }
}

fun formatAsCurrency(price: Double): String {
    return NumberFormat.getCurrencyInstance(Locale.US).format(price)
}

fun Long.formatDate(): String {
//    by checking this it will not display 01/01/1970 but it will show the placeholder text
    if (this == 0L) return ""
    val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
    val date = Date(this)
    return formatter.format(date)
}