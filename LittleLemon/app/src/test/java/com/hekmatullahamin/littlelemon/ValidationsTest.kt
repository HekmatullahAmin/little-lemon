package com.hekmatullahamin.littlelemon

import com.hekmatullahamin.littlelemon.utils.Validations
import org.junit.Assert
import org.junit.Test

class ValidationsTest {
    //    for validations

    @Test
    fun isValidEmail_returnsTrueForValidGmailAddress() {
        val myEmail = "hekmat@gmail.com"
        Assert.assertTrue(Validations.isValidEmail(myEmail))
    }

    @Test
    fun isValidEmail_returnsTrueForValidYahooAddress() {
        val myEmail = "hekmat@yahoo.com"
        Assert.assertTrue(Validations.isValidEmail(myEmail))
    }

    @Test
    fun isValidEmail_returnsFalseForInvalidDomain() {
        val myEmail = "hekmat@outlook.com"
        Assert.assertFalse(Validations.isValidEmail(myEmail))
    }

    @Test
    fun isValidEmail_returnsFalseForEmptyString() {
        val myEmptyEmail = ""
        Assert.assertFalse(Validations.isValidEmail(myEmptyEmail))
    }

    @Test
    fun isValidEmail_returnsFalseForInvalidEmailStructure() {
        val invalidEmails = listOf(
            "plainaddress",
            "missingatsign.com",
            "missingdomain@.com",
            "missinguser@domain",
            "@missingusername.com",
            "missingdomain@.com",
            "invalid@domain@domain.com"
        )

        invalidEmails.forEach { email ->
            Assert.assertFalse(Validations.isValidEmail(email))
        }
    }

    @Test
    fun isValidPassword_returnsTrueForStrongPassword() {
        val myPassword = "hekmat@1"
        Assert.assertTrue(Validations.isValidPassword(myPassword))
    }

    @Test
    fun isValidPassword_returnsFalseForMissingDigits() {
        val myPassword = "hekmat@h"
        Assert.assertFalse(Validations.isValidPassword(myPassword))
    }

    @Test
    fun isValidPassword_returnsFalseForMissingSpecialCharacters() {
        val myPassword = "hekmat1"
        Assert.assertFalse(Validations.isValidPassword(myPassword))
    }

    @Test
    fun isValidPassword_returnsFalseForShortPassword() {
        val myPassword = "hek@1"
        Assert.assertFalse(Validations.isValidPassword(myPassword))
    }

    @Test
    fun isValidPassword_returnsFalseForEmptyString() {
        val myEmptyPassword = ""
        Assert.assertFalse(Validations.isValidPassword(myEmptyPassword))
    }
}