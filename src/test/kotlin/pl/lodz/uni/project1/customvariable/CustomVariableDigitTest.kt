package pl.lodz.uni.project1.customvariable

import org.junit.jupiter.api.Test

internal class CustomVariableDigitTest {
    private val testValue = CustomVariableDigit(5, CustomVariableDigit(8, CustomVariableDigit(2, null)))

    @Test
    fun depth() {
        assert(testValue.depth() == 2)
    }

    @Test
    fun get() {
        assert(testValue.get(0)!!.digit.toInt() == 5)
        assert(testValue.get(1)!!.digit.toInt() == 8)
        assert(testValue.get(2)!!.digit.toInt() == 2)
    }
}