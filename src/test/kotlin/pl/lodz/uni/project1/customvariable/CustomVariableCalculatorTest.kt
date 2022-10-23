package pl.lodz.uni.project1.customvariable

import org.junit.Ignore
import org.junit.jupiter.api.TestInstance
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
        println("($a)+($b)=($result)")
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
        )
    }


    @ParameterizedTest
    @MethodSource("testSubtractData")
    fun testSubtract(inputA: String, inputB: String, expected: String) {
        val a = CustomVariable.parseCustomVariable(inputA)
        val b = CustomVariable.parseCustomVariable(inputB)
        val result = CustomVariableCalculator.subtract(a, b)
        println("($a)-($b)=($result)")
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
        )
    }

    @Ignore
    @ParameterizedTest
    @MethodSource("testMultiplyData")
    fun testMultiply(inputA: String, inputB: String, expected: String) {
        val a = CustomVariable.parseCustomVariable(inputA)
        val b = CustomVariable.parseCustomVariable(inputB)
        val result = CustomVariableCalculator.multiply(a, b)
        println("($a)*($b)=($result)")
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
        )
    }

    @Ignore
    @ParameterizedTest
    @MethodSource("testDivideData")
    fun testDivide(inputA: String, inputB: String, expected: String, rest: String) {
        val a = CustomVariable.parseCustomVariable(inputA)
        val b = CustomVariable.parseCustomVariable(inputB)
        val result = CustomVariableCalculator.divide(a, b)
        println("($a)/($b)=(${result.first}) and (${result.second})")
        assert(result.first.equals(expected))
        assert(result.second.equals(rest))
    }

    private fun testDivideData(): Stream<Arguments?>? {
        return Stream.of(
            Arguments.of("411,598", "22,597", "18", "4,852"),
            Arguments.of("257,844", "-26,346", "-9", "20,73"),
            Arguments.of("-199,647", "37,27", "-5", "-13,297"),
            Arguments.of("-252,123", "-15,457", "16", "-4,811"),
        )
    }
}