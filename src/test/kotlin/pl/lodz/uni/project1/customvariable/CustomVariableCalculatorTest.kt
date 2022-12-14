package pl.lodz.uni.project1.customvariable

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class CustomVariableCalculatorTest {
    @ParameterizedTest
    @MethodSource("testAddData")
    fun testAdd(inputA: String, inputB: String, expected: String) {
        val a = CustomVariable.parseCustomVariable(inputA)
        val b = CustomVariable.parseCustomVariable(inputB)
        val result = CustomVariableCalculator.add(a, b)
        println("testAdd: '$a'+'$b'='$result'")
        assert(result.toString() == expected)
    }

    private fun testAddData(): Stream<Arguments?>? {
        return Stream.of(
            Arguments.of("438152,388001", "13883,382633", "452035,770634"),
            Arguments.of("577585,765821", "-85443,753154", "492142,012667"),
            Arguments.of("-972087,847792", "873379,208133", "-98708,639659"),
            Arguments.of("-371344,83341", "-893550,145901", "-1264894,979311"),
            Arguments.of("99,99", "99,99", "199,98"),
            Arguments.of("-99,99", "99,99", "0"),
            Arguments.of("15", "125", "140"),
            Arguments.of("0,67", "0,33", "1"),
            Arguments.of(
                "11111111111111111111111111111111111111111111111111111111111111111111111,11111111111111111111111111111111111111111111111111111111111111111111111",
                "11111111111111111111111111111111111111111111111111111111111111111111111,11111111111111111111111111111111111111111111111111111111111111111111111",
                "22222222222222222222222222222222222222222222222222222222222222222222222,22222222222222222222222222222222222222222222222222222222222222222222222"
            ),
            Arguments.of(
                "99999999999999999999999999999999999999999999999999999999999999999999999,99999999999999999999999999999999999999999999999999999999999999999999999",
                "99999999999999999999999999999999999999999999999999999999999999999999999,99999999999999999999999999999999999999999999999999999999999999999999999",
                "199999999999999999999999999999999999999999999999999999999999999999999999,99999999999999999999999999999999999999999999999999999999999999999999998"
            ),
            Arguments.of(
                "10000000000000000000000000000000000000000000000000000000000000000000000,00000000000000000000000000000000000000000000000000000000000000000000001",
                "90000000000000000000000000000000000000000000000000000000000000000000000,00000000000000000000000000000000000000000000000000000000000000000000009",
                "100000000000000000000000000000000000000000000000000000000000000000000000,0000000000000000000000000000000000000000000000000000000000000000000001",
            ),
        )
    }


    @ParameterizedTest
    @MethodSource("testSubtractData")
    fun testSubtract(inputA: String, inputB: String, expected: String) {
        val a = CustomVariable.parseCustomVariable(inputA)
        val b = CustomVariable.parseCustomVariable(inputB)
        val result = CustomVariableCalculator.subtract(a, b)
        println("testSubtract: '$a'-'$b'='$result'")
        assert(result.equals(expected))
    }

    private fun testSubtractData(): Stream<Arguments?>? {
        return Stream.of(
            Arguments.of("160681,519408", "978997,978997", "-818316,459589"),
            Arguments.of("486581,549341", "-614187,216522", "1100768,765863"),
            Arguments.of("-274052,358333", "78620,14971", "-352672,508043"),
            Arguments.of("-25648,447155", "-963069,738277", "937421,291122"),
            Arguments.of("23,56", "23,56", "0"),
            Arguments.of("23,56", "23,46", "0,1"),
            Arguments.of("25", "68", "-43"),
            Arguments.of("-99,99", "99,99", "-199,98"),
            Arguments.of(
                "22222222222222222222222222222222222222222222222222222222222222222222222,22222222222222222222222222222222222222222222222222222222222222222222222",
                "11111111111111111111111111111111111111111111111111111111111111111111111,11111111111111111111111111111111111111111111111111111111111111111111111",
                "11111111111111111111111111111111111111111111111111111111111111111111111,11111111111111111111111111111111111111111111111111111111111111111111111"
            ),
            Arguments.of(
                "99999999999999999999999999999999999999999999999999999999999999999999999,99999999999999999999999999999999999999999999999999999999999999999999999",
                "100000000000000000000000000000000000000000000000000000000000000000000000,00000000000000000000000000000000000000000000000000000000000000000000000",
                "-0,00000000000000000000000000000000000000000000000000000000000000000000001"
            ),
            Arguments.of("0,99", "0,9", "0,09"),
        )
    }

    @ParameterizedTest
    @MethodSource("testMultiplyData")
    fun testMultiply(inputA: String, inputB: String, expected: String) {
        val a = CustomVariable.parseCustomVariable(inputA)
        val b = CustomVariable.parseCustomVariable(inputB)
        val result = CustomVariableCalculator.multiply(a, b)
        println("testMultiply: '$a'*'$b'='$result'")
        assert(result.equals(expected))
    }

    private fun testMultiplyData(): Stream<Arguments?>? {
        return Stream.of(
            Arguments.of("0,12", "0,18", "0,0216"),
            Arguments.of("12", "18", "216"),
            Arguments.of("12", "180", "2160"),
            Arguments.of("1,2", "3,4", "4,08"),
            Arguments.of("24,542", "0", "0"),
            Arguments.of("999,999", "999,999", "999998,000001"),
            Arguments.of("36,134", "684,937", "24749,513558"),
            Arguments.of("653,307", "-744,528", "-486405,354096"),
            Arguments.of("-407,320", "879,215", "-358121,8538"),
            Arguments.of("-692,560", "-576,940", "399565,5664"),
            Arguments.of(
                "11111111111111111111111111111111111111111111111111111111111111111111111,11111111111111111111111111111111111111111111111111111111111111111111111",
                "2",
                "22222222222222222222222222222222222222222222222222222222222222222222222,22222222222222222222222222222222222222222222222222222222222222222222222"
            ),
            Arguments.of(
                "11111111111111111111111111111111111111111111111111111111111111111111111,11111111111111111111111111111111111111111111111111111111111111111111111",
                "22222222222222222222222222222222222222222222222222222222222222222222222,22222222222222222222222222222222222222222222222222222222222222222222222",
                "246913580246913580246913580246913580246913580246913580246913580246913580246913580246913580246913580246913580246913580246913580246913580246913,5308641975308641975308641975308641975308641975308641975308641975308641975308641975308641975308641975308641975308641975308641975308641975308642"
            ),
        )
    }

    @ParameterizedTest
    @MethodSource("testPrepareForDivideData")
    fun testPrepareForDivide(
        aString: String,
        bString: String,
        expectedIntA: String,
        expectedFloatA: String,
        expectedIntB: String
    ) {
        val a = CustomVariable.parseCustomVariable(aString)
        val b = CustomVariable.parseCustomVariable(bString)
        val (intA, floatA, intB) = CustomVariableCalculator.multiplyNumbersToMakeDivisorAnInteger(a, b)
        println("testPrepareForDivide: '$a'/'$b'='$intA${if (floatA != null) ",$floatA" else ""}'/'$intB'")
        assertAll(
            { Assertions.assertEquals(expectedIntA, intA.toString()) },
            { Assertions.assertEquals(expectedFloatA, floatA.toString()) },
            { Assertions.assertEquals(expectedIntB, intB.toString()) },
        )
    }

    private fun testPrepareForDivideData(): Stream<Arguments> {
        return Stream.of(
            Arguments.of("411,8", "22,7", "4118", "null", "227"),
            Arguments.of("5782,4826", "46,28", "578248", "26", "4628"),
            Arguments.of("47,28", "26,34", "4728", "null", "2634"),
            Arguments.of("148,2", "39,36", "14820", "null", "3936"),
            Arguments.of("34", "39,36", "3400", "null", "3936"),
            Arguments.of("100", "10", "100", "null", "10"),
            Arguments.of("500", "0,5", "5000", "null", "5"),
        )
    }

    @ParameterizedTest
    @MethodSource("testDivideData")
    fun testDivide(inputA: String, inputB: String, expected: String, rest: String) {
        val a = CustomVariable.parseCustomVariable(inputA)
        val b = CustomVariable.parseCustomVariable(inputB)
        val result = CustomVariableCalculator.divide(a, b)
        println("testDivide: '$a'/'$b'='${result.first}'; rest='${result.second}'")
        assertAll(
            { Assertions.assertEquals(expected, result.first.toString()) },
            { Assertions.assertEquals(rest, result.second.toString()) },
        )
    }

    private fun testDivideData(): Stream<Arguments?>? {
        return Stream.of(
            Arguments.of("411,598", "22,597", "18", "4,852"),
            Arguments.of("257,844", "-26,346", "-9", "20,73"),
            Arguments.of("-199,647", "37,27", "-5", "-13,297"),
            Arguments.of("-252,123", "-15,457", "16", "-4,811"),
            Arguments.of("-252,123", "1", "-252", "-0,123"),
            Arguments.of("125", "5", "25", "0"),
            Arguments.of("500", "0,5", "1000", "0"),
            Arguments.of(
                "44444444444444444444444444444444444444444444444444444444444444444444444,44444444444444444444444444444444444444444444444444444444444444444444444",
                "22222222222222222222222222222222222222222222222222222222222222222222222,22222222222222222222222222222222222222222222222222222222222222222222222",
                "2",
                "0",
            ),
            Arguments.of(
                "11111111111111111111111111111111111111111111111111111111111111111111111,11111111111111111111111111111111111111111111111111111111111111111111111",
                "99",
                "112233445566778900112233445566778900112233445566778900112233445566778",
                "89,11111111111111111111111111111111111111111111111111111111111111111111111"
            ),
        )
    }
}