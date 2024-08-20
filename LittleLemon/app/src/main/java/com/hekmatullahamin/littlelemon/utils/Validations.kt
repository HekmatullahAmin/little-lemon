package com.hekmatullahamin.littlelemon.utils

object Validations {
//    Email and Password Validation: These should be handled within the ViewModel or a similar layer that deals with business logic.
//    Validation is a part of ensuring the data correctness before any business operation (e.g., saving to a database, sending to a server).

    //    The email regex ensures the email is from a specific domain (gmail.com or yahoo.com) and follows the common email structure.

    fun isValidEmail(email: String): Boolean {
        val emailPattern = "^[\\w\\-.]+@(gmail|yahoo)\\.com$".toRegex()
        return email.matches(emailPattern)
    }

    //    The password regex ensures the password is strong enough by requiring digits, special characters, and a minimum length.
    fun isValidPassword(password: String): Boolean {
        val passwordPattern = "^(?=.*[0-9])(?=.*[!@#\$%^&*]).{6,}$".toRegex()
        return password.matches(passwordPattern)
    }
}