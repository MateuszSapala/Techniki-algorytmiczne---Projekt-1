package pl.lodz.uni.project1.customvariable

data class CustomVariable(
    private val firstIntegerWithSign: CustomVariableDigit,
    val firstFloatingPointNumber: CustomVariableDigit?
) {
    companion object {
        fun parseCustomVariable(input: String): CustomVariable {
            if (input.count { it == '-' } > 1) {
                throw IllegalArgumentException("Provided more than one '-' in String input")
            }
            val sign: Byte = if (input[0] == '-') -1 else 1

            val split = input
                .replaceFirst("-", "")
                .replaceFirst(Regex("0+"), "")
                .split(",")
            if (split.size > 2) {
                throw IllegalArgumentException("Provided more than one ',' in String input")
            }

            return CustomVariable(
                CustomVariableDigit(
                    sign, numericStringToCustomVariableDigitChain(split[0], getIntIterator(split[0]))
                ),
                if (split.size == 2) numericStringToCustomVariableDigitChain(
                    split[1],
                    getFloatIterator(split[1])
                ) else null
            )
        }

        private fun numericStringToCustomVariableDigitChain(
            string: String,
            progression: IntProgression
        ): CustomVariableDigit? {
            var result: CustomVariableDigit? = null
            for (i in progression) {
                result = CustomVariableDigit(string[i].digitToInt().toByte(), result)
            }
            return result
        }

        private fun getIntIterator(string: String): IntProgression = string.indices
        private fun getFloatIterator(string: String): IntProgression = string.length - 1 downTo 0
    }

    fun getFirstInteger(): CustomVariableDigit? = firstIntegerWithSign.nextDigit

    fun isPositive(): Boolean = firstIntegerWithSign.digit == 1.toByte()

    override fun toString(): String {
        var string = ""
        var iterator = getFirstInteger()
        while (iterator != null) {
            string = iterator.digit.toString() + string
            iterator = iterator.nextDigit
        }
        if (!isPositive()) {
            string = "-$string"
        }
        if (firstFloatingPointNumber == null) {
            return string
        }
        string += ","
        iterator = firstFloatingPointNumber
        while (iterator != null) {
            string += iterator.digit
            iterator = iterator.nextDigit
        }
        return string
    }

    override fun equals(other: Any?): Boolean {
        if (other !is CustomVariable) {
            return false
        }
        return this.toString() == other.toString()
    }
}
