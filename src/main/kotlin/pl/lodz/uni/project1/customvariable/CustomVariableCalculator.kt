package pl.lodz.uni.project1.customvariable

class CustomVariableCalculator {
    companion object {
        fun add(a: CustomVariable, b: CustomVariable): CustomVariable {
            val sameSign = a.isPositive() == b.isPositive()
            if (!sameSign) {
                return subtract(a, CustomVariable(a.isPositive(), b.getInt(), b.getFloat()))
            }
            val floatResult = floatSum(a.getFloat(), b.getFloat())
            val int = intSum(a.getInt(), b.getInt(), floatResult.second)
            return CustomVariable(a.isPositive(), int, floatResult.first)
        }

        private fun floatSum(valA: CustomVariableDigit?, valB: CustomVariableDigit?): Pair<CustomVariableDigit?, Byte> {
            if (valA == null || valB == null) {
                return if (valA == null) Pair(valB, 0) else Pair(valA, 0)
            }
            val dA = valA.depth()
            val dB = valB.depth()
            val maxDepth = if (dA >= dB) dA else dB
            var result: CustomVariableDigit? = null
            var carried: Byte = 0
            for (i in maxDepth downTo 0) {
                val a: Byte = if (i <= dA) valA.get(dA - i)!!.digit else 0
                val b: Byte = if (i <= dB) valB.get(dB - i)!!.digit else 0
                val sum = (a + b + carried).toByte()
                result = CustomVariableDigit(sum.mod(10).toByte(), result)
                carried = (sum / 10).toByte()
            }
            return Pair(result?.reverse(skipZeroAtEnd = true), carried)
        }

        private fun intSum(
            valA: CustomVariableDigit?,
            valB: CustomVariableDigit?,
            valCarried: Byte
        ): CustomVariableDigit? {
            if (valA == null || valB == null) {
                return valA ?: valB
            }
            var carried = valCarried
            var varA = valA
            var varB = valB
            var result: CustomVariableDigit? = null
            while (varA != null || varB != null || carried > 0) {
                val a = varA?.digit ?: 0
                val b = varB?.digit ?: 0
                val sum = (a + b + carried).toByte()
                result = CustomVariableDigit(sum.mod(10).toByte(), result)
                carried = (sum / 10).toByte()
                varA = varA?.nextDigit
                varB = varB?.nextDigit
            }
            return result?.reverse(true)
        }

        fun subtract(valA: CustomVariable, valB: CustomVariable): CustomVariable {
            val change = valA.compareToWithoutSign(valB) < 0
            val sameSign = valA.isPositive() == valB.isPositive()
            if (!sameSign) {
                return add(
                    valA,
                    CustomVariable(valA.isPositive(), valB.getInt(), valB.getFloat())
                )
            }
            val a = if (change) valB else valA
            val b = if (change) valA else valB

            val floatResult = floatSubtraction(a.getFloat(), b.getFloat())
            val int = intSubtraction(a.getInt(), b.getInt(), floatResult.second)
            val positive = if (change) !a.isPositive() else a.isPositive()
            return CustomVariable(positive, int, floatResult.first)
        }

        private fun floatSubtraction(
            valA: CustomVariableDigit?,
            valB: CustomVariableDigit?
        ): Pair<CustomVariableDigit?, Byte> {
            if (valA == null || valB == null) {
                return if (valA == null) Pair(valB, 0) else Pair(valA, 0)
            }
            val dA = valA.depth()
            val dB = valB.depth()
            val maxDepth = if (dA >= dB) dA else dB
            var result: CustomVariableDigit? = null
            var lent: Byte = 0
            for (i in maxDepth downTo 0) {
                val a: Byte = if (i <= dA) valA.get(dA - i)!!.digit else 0
                val b: Byte = if (i <= dB) valB.get(dB - i)!!.digit else 0
                var sum = (a - b - lent).toByte()
                lent = 0
                if (sum < 0) {
                    sum = (sum + 10).toByte()
                    lent = 1
                }
                result = CustomVariableDigit(sum, result)
            }
            return Pair(result?.reverse(skipZeroAtEnd = true), lent)
        }

        private fun intSubtraction(
            valA: CustomVariableDigit?,
            valB: CustomVariableDigit?,
            valLent: Byte
        ): CustomVariableDigit? {
            if (valA == null || valB == null) {
                return valA ?: valB
            }
            var lent = valLent
            var varA = valA
            var varB = valB
            var result: CustomVariableDigit? = null
            while (varA != null || varB != null) {
                val a = varA?.digit ?: 0
                val b = varB?.digit ?: 0
                var sum = (a - b - lent).toByte()
                lent = 0
                if (sum < 0) {
                    sum = (sum + 10).toByte()
                    lent = 1
                }
                result = CustomVariableDigit(sum, result)
                varA = varA?.nextDigit
                varB = varB?.nextDigit
            }
            return result?.reverse(true)
        }

        fun multiply(a: CustomVariable, b: CustomVariable): CustomVariable {
            TODO("Not implemented yet")
        }

        fun divide(a: CustomVariable, b: CustomVariable): Pair<CustomVariable, CustomVariable> {
            TODO("Not implemented yet")
        }
    }
}