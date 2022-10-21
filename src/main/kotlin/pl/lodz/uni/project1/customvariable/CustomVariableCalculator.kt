package pl.lodz.uni.project1.customvariable

class CustomVariableCalculator {
    companion object {
        fun add(a: CustomVariable, b: CustomVariable): CustomVariable {
            val sameSign = a.isPositive() == b.isPositive()
            if (!sameSign) {
                subtract(
                    a,
                    CustomVariable(CustomVariableDigit(a.sign(), b.getFirstInteger()), b.firstFloatingPointNumber)
                )
            }
            val floatResult = digitSum(a.firstFloatingPointNumber, b.firstFloatingPointNumber, 0, true)
            val intResult = digitSum(a.getFirstInteger(), b.getFirstInteger(), floatResult.second, false)
            return CustomVariable(
                CustomVariableDigit(if (a.isPositive()) 1 else -1, intResult.first),
                floatResult.first
            )
        }

        private fun digitSum(
            valA: CustomVariableDigit?,
            valB: CustomVariableDigit?,
            valCarried: Byte,
            float: Boolean
        ): Pair<CustomVariableDigit?, Byte> {
            var carried = valCarried;
            var varA = valA;
            var varB = valB
            var depthA: Int = varA?.depth() ?: 0
            var depthB: Int = varB?.depth() ?: 0
            val depth = if (depthA > depthB) depthA else depthB
            val array = ByteArray(depth + 1)

            for (i in depth downTo 0) {
                val (a, depthA2, varA2) = sumNext(depthA, depthB, varA, float); depthA = depthA2; varA = varA2
                val (b, depthB2, varB2) = sumNext(depthB, depthA, varB, float); depthB = depthB2; varB = varB2
                val sum: Byte = (a + b + carried).toByte()
                carried = (sum / 10).toByte()
                array[i] = sum.mod(10).toByte()
            }

            var newFloatDigit: CustomVariableDigit? = null
            if (!float && carried != (0).toByte()) {
                newFloatDigit = CustomVariableDigit(carried, newFloatDigit)
                carried = 0
            }
            for (j in array) {
                newFloatDigit = CustomVariableDigit(j, newFloatDigit)
            }
            return Pair(newFloatDigit, carried)
        }

        private fun sumNext(
            depth1: Int,
            depth2: Int,
            x: CustomVariableDigit?,
            float: Boolean
        ): Triple<Byte, Int, CustomVariableDigit?> {
            if (!float || (depth1 >= depth2 && x != null)) {
                return Triple(x?.digit ?: (0).toByte(), depth1 - 1, x?.nextDigit)
            }
            return Triple(0, depth1, x)
        }

        fun subtract(a: CustomVariable, b: CustomVariable): CustomVariable {
            TODO("Not implemented yet")
        }

        fun multiply(a: CustomVariable, b: CustomVariable): CustomVariable {
            TODO("Not implemented yet")
        }

        fun divide(a: CustomVariable, b: CustomVariable): Pair<CustomVariable, CustomVariable> {
            TODO("Not implemented yet")
        }
    }
}