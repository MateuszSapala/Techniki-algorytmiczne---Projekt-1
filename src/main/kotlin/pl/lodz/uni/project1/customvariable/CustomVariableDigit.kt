package pl.lodz.uni.project1.customvariable

data class CustomVariableDigit(val digit: Byte, var nextDigit: CustomVariableDigit?) {
    fun size(): Int {
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

    fun getLast(): CustomVariableDigit? = get(size())

    fun reverse(isInt: Boolean): CustomVariableDigit? {
        var x: CustomVariableDigit? = this
        var reversed: CustomVariableDigit? = null
        while (x != null) {
            if (isInt && reversed == null && x.digit.toInt() == 0) {
                x = x.nextDigit
                continue
            }
            reversed = CustomVariableDigit(x.digit, reversed)
            x = x.nextDigit
        }
        if (!isInt) {
            while (reversed != null && reversed.digit == (0).toByte()) {
                reversed = reversed.nextDigit
            }
        }
        return reversed
    }

    fun setLast(value: Byte) {
        get(size())?.nextDigit = CustomVariableDigit(value, null)
    }

    fun removeLast(): CustomVariableDigit? {
        if (nextDigit == null) {
            return null;
        }
        get(size() - 1)?.nextDigit = null
        return this
    }

    override fun toString(): String {
        var string = ""
        var iterator: CustomVariableDigit? = this
        while (iterator != null) {
            string = iterator.digit.toString() + string
            iterator = iterator.nextDigit
        }
        return string
    }

    fun isZero(): Boolean = nextDigit == null && digit == (0).toByte()
}
