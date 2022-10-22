package pl.lodz.uni.project1.customvariable

import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class CustomVariableTest {

    @ParameterizedTest
    @MethodSource("testParseCustomVariableData")
    fun testParseCustomVariable(input: String, expected: CustomVariable) {
        val result = CustomVariable.parseCustomVariable(input)

        assert(result.isPositive() == expected.isPositive())

        var integer: CustomVariableDigit? = result.getInt()
        var expectedInteger: CustomVariableDigit? = expected.getInt()
        while (integer != null || expectedInteger != null) {
            assert(integer!!.digit == expectedInteger!!.digit)
            expectedInteger = expectedInteger.nextDigit
            integer = integer.nextDigit
        }

        var float: CustomVariableDigit? = result.getFloat()
        var expectedFloat: CustomVariableDigit? = expected.getFloat()
        while (float != null || expectedFloat != null) {
            assert(float!!.digit == expectedFloat!!.digit)
            expectedFloat = expectedFloat.nextDigit
            float = float.nextDigit
        }
    }

    private fun testParseCustomVariableData(): Stream<Arguments?>? {
        return Stream.of(
            Arguments.of("947,35", CustomVariable(true, listOf(9, 4, 7), listOf(3, 5))),
            Arguments.of("-544,514", CustomVariable(false, listOf(5, 4, 4), listOf(5, 1, 4))),
            Arguments.of("000504,514", CustomVariable(true, listOf(5, 0, 4), listOf(5, 1, 4))),
            Arguments.of("504,004", CustomVariable(true, listOf(5, 0, 4), listOf(0, 0, 4))),
            Arguments.of("125", CustomVariable(true, listOf(1, 2, 5), listOf())),
            Arguments.of(",125", CustomVariable(true, listOf(0), listOf(1, 2, 5))),
        )
    }

    @ParameterizedTest
    @MethodSource("testToStringData")
    fun testToString(input: CustomVariable, expected: String) {
        val result = input.toString()
        assert(result == expected)
    }

    private fun testToStringData(): Stream<Arguments?>? {
        return Stream.of(
            Arguments.of(CustomVariable(true, listOf(2, 5), listOf(2, 7, 4)), "25,274"),
            Arguments.of(CustomVariable(false, listOf(4, 4, 2), listOf(2, 2, 5)), "-442,225"),
        )
    }
}