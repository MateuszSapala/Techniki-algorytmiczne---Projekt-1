package pl.lodz.uni.project1.customvariable

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll

internal class CustomVariableDigitTest {
    private val testValue = CustomVariableDigit(5, CustomVariableDigit(8, CustomVariableDigit(2, null)))

    @Test
    fun testSize() {
        assert(testValue.size() == 2)
    }

    @Test
    fun testGet() {
        assertAll(
            { Assertions.assertEquals(testValue.get(0)!!.digit, (5).toByte()) },
            { Assertions.assertEquals(testValue.get(1)!!.digit, (8).toByte()) },
            { Assertions.assertEquals(testValue.get(2)!!.digit, (2).toByte()) },
        )
    }
}