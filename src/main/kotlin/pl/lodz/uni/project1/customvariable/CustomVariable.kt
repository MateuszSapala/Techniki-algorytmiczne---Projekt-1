package pl.lodz.uni.project1.customvariable

class CustomVariable private constructor(
    private val firstIntegerWithSign: CustomVariableDigit,
    private val firstFloatingPointNumber: CustomVariableDigit?,
) : Comparable<CustomVariable> {
    companion object {
        operator fun invoke(
            positive: Boolean,
            valInt: CustomVariableDigit?,
            valFloat: CustomVariableDigit?
        ): CustomVariable {
            var sign: Byte = if (positive) 1 else -1
            val int = valInt ?: CustomVariableDigit(0, null)
            if (valFloat == null && (valInt == null || (valInt.digit == (0).toByte() && valInt.nextDigit == null))) {
                sign = 1
            }
            var float = valFloat
            while (float?.digit == (0).toByte()) {
                float = float.nextDigit
            }
            return CustomVariable(CustomVariableDigit(sign, int), float)
        }

        operator fun invoke(
            positive: Boolean,
            valInt: List<Byte>,
            valFloat: List<Byte>
        ): CustomVariable {
            var int: CustomVariableDigit? = null
            var float: CustomVariableDigit? = null
            for (item in valInt) {
                int = CustomVariableDigit(item, int)
            }
            for (item in valFloat) {
                float = CustomVariableDigit(item, float)
            }
            return CustomVariable(positive, int, float)
        }

        fun parseCustomVariable(input: String): CustomVariable {
            if (input.count { it == '-' } > 1) {
                throw IllegalArgumentException("Provided more than one '-' in String input")
            }
            if (input.count { it == '-' } == 1 && input[0] != '-') {
                throw IllegalArgumentException("A negative number should start with a '-' not have that sign in the middle of the input")
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
                positive, numericStringToCustomVariableDigit(split[0]),
                if (split.size == 2) numericStringToCustomVariableDigit(split[1]) else null
            )
        }

        private fun numericStringToCustomVariableDigit(string: String): CustomVariableDigit? {
            var result: CustomVariableDigit? = null
            for (i in string.indices) {
                result = CustomVariableDigit(string[i].digitToInt().toByte(), result)
            }
            return result
        }

        private fun intCompareTo(a: CustomVariableDigit?, b: CustomVariableDigit?): Int {
            if (a == null || b == null) {
                if (a == null) {
                    return if (b == null) 0 else -1
                }
                return 1
            }
            val dA = a.size()
            val dB = b.size()
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
            val dA = a.size()
            val dB = b.size()
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
        val comma = if (firstFloatingPointNumber != null) "," else ""
        return "$sign${getInt() ?: ""}$comma${getFloat() ?: ""}"
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

    fun isZero(): Boolean = getInt()?.digit == (0).toByte() && getInt()?.nextDigit == null && getFloat() == null
}
