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

        var integer: CustomVariableDigit? = result.getFirstInteger()
        var expectedInteger: CustomVariableDigit? = expected.getFirstInteger()
        while (integer != null || expectedInteger != null) {
            assert(integer!!.digit == expectedInteger!!.digit)
            expectedInteger = expectedInteger.nextDigit
            integer = integer.nextDigit
        }

        var float: CustomVariableDigit? = result.firstFloatingPointNumber
        var expectedFloat: CustomVariableDigit? = expected.firstFloatingPointNumber
        while (float != null || expectedFloat != null) {
            assert(float!!.digit == expectedFloat!!.digit)
            expectedFloat = expectedFloat.nextDigit
            float = float.nextDigit
        }
    }

    private fun testParseCustomVariableData(): Stream<Arguments?>? {
        return Stream.of(
            Arguments.of(
                "947,35",
                CustomVariable(
                    CustomVariableDigit(
                        1, CustomVariableDigit(7, CustomVariableDigit(4, CustomVariableDigit(9, null)))
                    ),
                    CustomVariableDigit(3, CustomVariableDigit(5, null))
                )
            ),
            Arguments.of(
                "-544,514",
                CustomVariable(
                    CustomVariableDigit(
                        -1, CustomVariableDigit(
                            4,
                            CustomVariableDigit(4, CustomVariableDigit(5, null))
                        )
                    ),
                    CustomVariableDigit(5, CustomVariableDigit(1, CustomVariableDigit(4, null)))
                )
            ),
            Arguments.of(
                "000504,514",
                CustomVariable(
                    CustomVariableDigit(
                        1, CustomVariableDigit(
                            4,
                            CustomVariableDigit(0, CustomVariableDigit(5, null))
                        )
                    ),
                    CustomVariableDigit(5, CustomVariableDigit(1, CustomVariableDigit(4, null)))
                )
            ),
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
            Arguments.of(
                CustomVariable(
                    CustomVariableDigit(1, CustomVariableDigit(5, CustomVariableDigit(2, null))),
                    CustomVariableDigit(
                        2,
                        CustomVariableDigit(7, CustomVariableDigit(4, null))
                    )
                ), "25,274"
            ),
            Arguments.of(
                CustomVariable(
                    CustomVariableDigit(
                        -1,
                        CustomVariableDigit(
                            2,
                            CustomVariableDigit(4, CustomVariableDigit(4, null))
                        )
                    ),
                    CustomVariableDigit(
                        2,
                        CustomVariableDigit(2, CustomVariableDigit(5, null))
                    )
                ), "-442,225"
            ),
        )
    }
}