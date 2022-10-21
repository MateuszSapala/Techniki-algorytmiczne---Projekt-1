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
}
