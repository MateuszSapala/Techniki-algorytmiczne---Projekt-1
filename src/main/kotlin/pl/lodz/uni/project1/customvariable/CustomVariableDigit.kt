package pl.lodz.uni.project1.customvariable

data class CustomVariableDigit(val digit: Byte, val nextDigit: CustomVariableDigit?) {
    fun depth(): Int {
        var iterator = nextDigit
        var depth = 0
        while (iterator != null) {
            depth++
            iterator = iterator.nextDigit
        }
        return depth
    }

    fun get(index: Int): CustomVariableDigit? {
        var result: CustomVariableDigit? = this
        for (i in 1..index) {
            if (result == null) {
                throw IndexOutOfBoundsException()
            }
            result = result.nextDigit
        }
        return result
    }

    fun reverse(skipZeroAtBeginning: Boolean = false): CustomVariableDigit {
        var x: CustomVariableDigit? = this
        var reversed: CustomVariableDigit? = null
        while (x != null) {
            if (skipZeroAtBeginning && reversed == null && x.digit.toInt() == 0) {
                x = x.nextDigit
                continue
            }
            reversed = CustomVariableDigit(x.digit, reversed)
            x = x.nextDigit
        }
        return reversed!!
    }
}
