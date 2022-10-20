package pl.lodz.uni.project1.customvariable

data class CustomVariable(val firstInteger: CustomVariableDigit, val firstFloatingPointNumber: CustomVariableDigit?) {
    companion object {
        fun parseCustomVariable(input: String): CustomVariable {
            val split = input.replaceFirst(Regex("0+"), "").split(",")
            if (split.size > 2) {
                throw IllegalArgumentException("Provided more than one ',' in String input")
            }
            val sign: Byte = if (split[0].contains("-")) -1 else 1
            return CustomVariable(
                CustomVariableDigit(sign, numericStringToCustomVariableDigitChain(split[0].replaceFirst("-", ""))),
                numericStringToCustomVariableDigitChain(split[1])
            )
        }

        private fun numericStringToCustomVariableDigitChain(string: String): CustomVariableDigit? {
            var result: CustomVariableDigit? = null
            for (i in string.length - 1 downTo 0) {
                result = CustomVariableDigit(string[i].digitToInt().toByte(), result)
            }
            return result
        }
    }

    override fun toString(): String {
        var string = ""
        var iterator: CustomVariableDigit? = firstInteger
        if (firstInteger.digit == (-1).toByte()) {
            string += "-"
        }
        iterator = iterator!!.nextDigit
        while (iterator != null) {
            string += iterator.digit
            iterator = iterator.nextDigit
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
