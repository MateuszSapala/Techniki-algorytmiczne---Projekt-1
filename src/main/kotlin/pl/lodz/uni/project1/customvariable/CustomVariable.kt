package pl.lodz.uni.project1.customvariable

data class CustomVariable(
    private val firstIntegerWithSign: CustomVariableDigit,
    val firstFloatingPointNumber: CustomVariableDigit?,
) {
    companion object {
        fun parseCustomVariable(input: String): CustomVariable {
            if (input.count { it == '-' } > 1) {
                throw IllegalArgumentException("Provided more than one '-' in String input")
            }
            val sign: Byte = if (input[0] == '-') -1 else 1

            var beforeSplit = input.replaceFirst("-", "")
            if(beforeSplit.startsWith("0")){
                beforeSplit = beforeSplit.replaceFirst(Regex("0+"), "")
            }
            val split = beforeSplit.split(",")
            if (split.size > 2) {
                throw IllegalArgumentException("Provided more than one ',' in String input")
            }

            return CustomVariable(
                CustomVariableDigit(sign, numericStringToCustomVariableDigitChain(split[0])),
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
    }

    fun getFirstInteger(): CustomVariableDigit? = firstIntegerWithSign.nextDigit

    fun isPositive(): Boolean = firstIntegerWithSign.digit == 1.toByte()
    fun sign(): Byte = firstIntegerWithSign.digit

    override fun toString(): String {
        val sign = if (isPositive()) "" else "-";
        val int = stringFromCustomVariable(getFirstInteger())
        val comma = if (firstFloatingPointNumber != null) "," else ""
        val float = stringFromCustomVariable(firstFloatingPointNumber)
        return "$sign$int$comma$float"
    }

    override fun equals(other: Any?): Boolean {
        if (other !is CustomVariable) {
            return false
        }
        return this.toString() == other.toString()
    }
}
