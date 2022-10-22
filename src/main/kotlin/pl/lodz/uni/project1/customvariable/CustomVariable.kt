package pl.lodz.uni.project1.customvariable

class CustomVariable private constructor(
    private val firstIntegerWithSign: CustomVariableDigit,
    private val firstFloatingPointNumber: CustomVariableDigit?,
) : Comparable<CustomVariable> {
    companion object {
        operator fun invoke(
            positive: Boolean,
            valInt: CustomVariableDigit?,
            float: CustomVariableDigit?
        ): CustomVariable {
            val sign: Byte = if (positive) 1 else -1
            val int = valInt ?: CustomVariableDigit(0, null)
            return CustomVariable(CustomVariableDigit(sign, int), float)
        }

        operator fun invoke(
            positive: Boolean,
            valInt: List<Byte>,
            valFloat: List<Byte>
        ): CustomVariable {
            val sign: Byte = if (positive) 1 else -1
            var int: CustomVariableDigit? = null
            var float: CustomVariableDigit? = null
            for (item in valInt) {
                int = CustomVariableDigit(item, int)
            }
            for (item in valFloat) {
                float = CustomVariableDigit(item, float)
            }
            return CustomVariable(CustomVariableDigit(sign, int), float)
        }

        fun parseCustomVariable(input: String): CustomVariable {
            if (input.count { it == '-' } > 1) {
                throw IllegalArgumentException("Provided more than one '-' in String input")
            }
            val positive = input[0] != '-'

            var beforeSplit = input.replaceFirst("-", "")
            if (beforeSplit.startsWith("0")) {
                beforeSplit = beforeSplit.replaceFirst(Regex("0+"), "")
            }
            val split = beforeSplit.split(",")
            if (split.size > 2) {
                throw IllegalArgumentException("Provided more than one ',' in String input")
            }

            return CustomVariable(
                positive, numericStringToCustomVariableDigitChain(split[0]),
                if (split.size == 2) numericStringToCustomVariableDigitChain(split[1]) else null
            )
        }

        private fun numericStringToCustomVariableDigitChain(string: String): CustomVariableDigit? {
            var result: CustomVariableDigit? = null
            for (i in string.indices) {
                result = CustomVariableDigit(string[i].digitToInt().toByte(), result)
            }
            return result
        }

        private fun stringFromCustomVariable(variable: CustomVariableDigit?): String {
            var string = ""
            var iterator = variable
            while (iterator != null) {
                string = iterator.digit.toString() + string
                iterator = iterator.nextDigit
            }
            return string
        }

        private fun intCompareTo(a: CustomVariableDigit?, b: CustomVariableDigit?): Int {
            if (a == null || b == null) {
                if (a == null) {
                    return if (b == null) 0 else -1
                }
                return 1
            }
            val dA = a.depth()
            val dB = b.depth()
            if (dA != dB) {
                return if (dA > dB) 1 else -1
            }
            for (i in dA downTo 0) {
                val compare = a.get(i)!!.digit.compareTo(b.get(i)!!.digit)
                if (compare != 0) {
                    return compare
                }
            }
            return 0
        }

        private fun floatCompareTo(a: CustomVariableDigit?, b: CustomVariableDigit?): Int {
            if (a == null || b == null) {
                if (a == null) {
                    return if (b == null) 0 else -1
                }
                return 1
            }
            val dA = a.depth()
            val dB = b.depth()
            val d = if (dA > dB) dA else dB
            for (i in 0..d) {
                val compare = (a.get(dA - i)?.digit ?: 0).compareTo(b.get(dB - i)?.digit ?: 0)
                if (compare != 0) {
                    return compare
                }
            }
            return 0
        }
    }

    fun getInt(): CustomVariableDigit? = firstIntegerWithSign.nextDigit
    fun getFloat(): CustomVariableDigit? = firstFloatingPointNumber

    fun isPositive(): Boolean = firstIntegerWithSign.digit == 1.toByte()
    fun sign(): Byte = firstIntegerWithSign.digit

    override fun toString(): String {
        val sign = if (isPositive()) "" else "-";
        val int = stringFromCustomVariable(getInt())
        val comma = if (firstFloatingPointNumber != null) "," else ""
        val float = stringFromCustomVariable(firstFloatingPointNumber)
        return "$sign$int$comma$float"
    }

    override fun compareTo(other: CustomVariable): Int {
        if (isPositive() != other.isPositive()) {
            return if (isPositive()) 1 else -1
        }
        return compareToWithoutSign(other)
    }

    fun compareToWithoutSign(other: CustomVariable): Int {
        val intCompare = intCompareTo(this.getInt(), other.getInt())
        if (intCompare == 0) {
            return floatCompareTo(this.getFloat(), other.getFloat())
        }
        return intCompare
    }

    override fun equals(other: Any?): Boolean {
        if (other is String) {
            return this.toString() == other
        }
        if (other !is CustomVariable) {
            return false
        }
        return this.toString() == other.toString()
    }
}
