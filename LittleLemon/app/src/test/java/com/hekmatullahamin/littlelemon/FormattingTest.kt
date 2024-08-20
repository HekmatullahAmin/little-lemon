package com.hekmatullahamin.littlelemon

import com.hekmatullahamin.littlelemon.data.models.Address
import com.hekmatullahamin.littlelemon.utils.concatenateAddress
import com.hekmatullahamin.littlelemon.utils.formatAsCurrency
import com.hekmatullahamin.littlelemon.utils.formatDate
import org.junit.Assert
import org.junit.Test

class FormattingTest {
    //    for formats
    @Test
    fun concatenateAddress_shouldReturnCorrectFormatAddress() {
        val address = Address(
            addressLineOne = "House No.1",
            addressLineTwo = "Karte Ariana",
            city = "Kabul",
            postalCode = "0000",
            country = "Afghanistan"
        )

        val expectedAddress = with(address) {
            "$addressLineOne, $addressLineTwo, $city, $postalCode, $country"
        }

        val actualAddress = concatenateAddress(address)

        Assert.assertEquals(expectedAddress, actualAddress)
    }

    @Test
    fun formatAsCurrency_shouldFormatPriceCorrectly_USLocale() {
        val expectedPrice = "$10.00"
        val actualPrice = formatAsCurrency(10.0)
        Assert.assertEquals(expectedPrice, actualPrice)
    }

    @Test
    fun formatDate_returnsEmptyStringForZeroValue() {
        val expectedDate = ""
        val value = 0L
        val actualDate = value.formatDate()
        Assert.assertEquals(expectedDate, actualDate)
    }

    @Test
    fun formatDate_shouldFormatLongToDateString() {
        val expectedDate = "04/28/2000"
        val myDateOfBirthInMilliSec = 956876400000L
        val actualDate = myDateOfBirthInMilliSec.formatDate()
        Assert.assertEquals(expectedDate, actualDate)
    }
}