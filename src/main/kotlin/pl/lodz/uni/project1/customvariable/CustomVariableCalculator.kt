package pl.lodz.uni.project1.customvariable

class CustomVariableCalculator {
    companion object {
        fun add(a: CustomVariable, b: CustomVariable): CustomVariable {
            // Subtract if variables have different sign e.g. '5'+"-3"->'5'-'3'
            if (a.isPositive() != b.isPositive()) {
                return subtract(a, CustomVariable(a.isPositive(), b.getInt(), b.getFloat()))
            }
            val floatResult = floatSum(a.getFloat(), b.getFloat())
            val int = intSum(a.getInt(), b.getInt(), floatResult.second)
            return CustomVariable(a.isPositive(), int, floatResult.first)
        }

        private fun floatSum(valA: CustomVariableDigit?, valB: CustomVariableDigit?): Pair<CustomVariableDigit?, Byte> {
            // Simplify calculations if one of the variables is null
            if (valA == null || valB == null) {
                return if (valA == null) Pair(valB, 0) else Pair(valA, 0)
            }
            val sizeA = valA.size()
            val sizeB = valB.size()
            val biggestSize = if (sizeA >= sizeB) sizeA else sizeB
            var result: CustomVariableDigit? = null
            var carried: Byte = 0
            for (i in biggestSize downTo 0) {
                //Getting digits in the same place from the end (if any exists)
                val a: Byte = if (i <= sizeA) valA.get(sizeA - i)!!.digit else 0
                val b: Byte = if (i <= sizeB) valB.get(sizeB - i)!!.digit else 0
                val sum = (a + b + carried).toByte()
                //Add a new number to the start of the list and calculate the carried value
                result = CustomVariableDigit(sum.mod(10).toByte(), result)
                carried = (sum / 10).toByte()
            }
            //Return value and carried value
            return Pair(result?.reverse(false), carried)
        }

        private fun intSum(
            valA: CustomVariableDigit?,
            valB: CustomVariableDigit?,
            valCarried: Byte
        ): CustomVariableDigit? {
            // Simplify calculations if one of the variables is null
            if (valA == null || valB == null) {
                return valA ?: valB
            }
            var carried = valCarried
            var varA = valA
            var varB = valB
            var result: CustomVariableDigit? = null
            while (varA != null || varB != null || carried > 0) {
                //Getting digits in the same place from the start (if any exists)
                val a = varA?.digit ?: 0
                val b = varB?.digit ?: 0
                val sum = (a + b + carried).toByte()
                //Add a new number to the start of the list and calculate the carried value
                result = CustomVariableDigit(sum.mod(10).toByte(), result)
                carried = (sum / 10).toByte()
                varA = varA?.nextDigit
                varB = varB?.nextDigit
            }
            return result?.reverse(true)
        }

        fun subtract(valA: CustomVariable, valB: CustomVariable): CustomVariable {
            val change = valA.compareToWithoutSign(valB) < 0
            // Add if variables have different sign e.g. '5'-"-3"->'5'+'3'
            if (valA.isPositive() != valB.isPositive()) {
                return add(
                    valA,
                    CustomVariable(valA.isPositive(), valB.getInt(), valB.getFloat())
                )
            }
            //To subtract smaller value from bigger
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
            val sizeA = valA?.size() ?: 0
            val sizeB = valB?.size() ?: 0
            val biggestSize = if (sizeA >= sizeB) sizeA else sizeB
            var result: CustomVariableDigit? = null
            var lent: Byte = 0
            for (i in biggestSize downTo 0) {
                val a: Byte = if (i <= sizeA && valA != null) valA.get(sizeA - i)!!.digit else 0
                val b: Byte = if (i <= sizeB && valB != null) valB.get(sizeB - i)!!.digit else 0
                var subtractionResult = (a - b - lent).toByte()
                lent = 0
                if (subtractionResult < 0) {
                    subtractionResult = (subtractionResult + 10).toByte()
                    lent = 1
                }
                result = CustomVariableDigit(subtractionResult, result)
            }
            //Return value and lent value that will be removed from integer
            return Pair(result?.reverse(false), lent)
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
            var result = multiplyFloat(b, a, a.isPositive() == b.isPositive())
            result = multiplyInt(b, a, result)
            return result
        }

        private fun multiplyFloat(
            b: CustomVariable,
            a: CustomVariable,
            sameSign: Boolean
        ): CustomVariable {
            var result = CustomVariable(sameSign, null, null)
            var float = b.getFloat()
            var floatSize = float?.size() ?: 0
            while (float != null) {
                //Multiply the first number by each digit in the floating point portion of the second digit
                val (floatMultiply, carried) = multiplyVariableByDigit(a.getFloat(), float.digit, false)
                var intMultiply = multiplyVariableByDigit(a.getInt(), float.digit, true, carried).first

                //Move the comma forward e.g 12,3 * 0,2-> 2,46
                for (i in 0..floatSize) {
                    floatMultiply?.setLast(intMultiply?.digit ?: 0)
                    intMultiply = intMultiply?.nextDigit
                }

                val multiplyByDigitResult = CustomVariable(a.isPositive() == b.isPositive(), intMultiply, floatMultiply)
                //Add the newly multiplied value to the previous values
                result = add(result, multiplyByDigitResult)
                float = float.nextDigit
                floatSize--
            }
            return result
        }

        private fun multiplyInt(
            b: CustomVariable,
            a: CustomVariable,
            valResult: CustomVariable
        ): CustomVariable {
            var result = valResult
            var int = b.getInt()
            var intSize = 0
            while (int != null) {
                //Multiply the first number by each digit in the integer part of the second digit
                var (floatMultiply, carried) = multiplyVariableByDigit(a.getFloat(), int.digit, false)
                var intMultiply = multiplyVariableByDigit(a.getInt(), int.digit, true, carried).first

                ////Move the comma backward e.g. 1,23 * 20-> 24,6
                for (i in 1..intSize) {
                    intMultiply = CustomVariableDigit(
                        floatMultiply?.get(floatMultiply.size())?.digit ?: 0,
                        intMultiply
                    )
                    floatMultiply = floatMultiply?.removeLast()
                }

                val multiplyByDigitResult = CustomVariable(a.isPositive() == b.isPositive(), intMultiply, floatMultiply)
                //Add the newly multiplied value to the previous values
                result = add(result, multiplyByDigitResult)
                int = int.nextDigit
                intSize++
            }
            return result
        }

        private fun multiplyVariableByDigit(
            valA: CustomVariableDigit?,
            digit: Byte,
            isInt: Boolean,
            valCarried: Byte = 0
        ): Pair<CustomVariableDigit?, Byte> {
            if (digit < 0 || digit >= 10) {
                throw IllegalArgumentException()
            }
            if (valA == null) {
                return Pair(null, 0)
            }
            if (digit == (0).toByte()) {
                return if (isInt) Pair(CustomVariableDigit(0, null), 0) else Pair(null, 0)
            }
            var a = valA
            var carried: Byte = valCarried
            var result: CustomVariableDigit? = null
            while (a != null || (isInt && carried != (0).toByte())) {
                val multiply = (a?.digit ?: 0) * digit + carried
                result = CustomVariableDigit(multiply.mod(10).toByte(), result)
                a = a?.nextDigit
                carried = (multiply / 10).toByte()
            }
            return Pair(result?.reverse(isInt), carried)
        }

        fun divide(valA: CustomVariable, valB: CustomVariable): Pair<CustomVariable, CustomVariable> {
            if (valB.isZero()) {
                throw IllegalArgumentException("Tried to be too smart, but it doesn't work here")
            }
            val sizeFloatB = valB.getFloat()?.size() ?: -1
            //Multiply the numbers so that b is an integer
            var (intA, floatA, intB) = multiplyNumbersToMakeDivisorAnInteger(valA, valB)
            if (intA == null) {
                return Pair(
                    CustomVariable(valA.isPositive() == valB.isPositive(), CustomVariableDigit(0, null), null),
                    CustomVariable(valA.isPositive() == valB.isPositive(), CustomVariableDigit(0, null), floatA)
                )
            }

            var (result, valueFromWhichWeSubtract) = divideInt(intA, intB)
            for (i in 0..sizeFloatB) {
                if (floatA != null) {
                    floatA.setLast(valueFromWhichWeSubtract.digit)
                } else {
                    floatA = CustomVariableDigit(valueFromWhichWeSubtract.digit, null)
                }
                valueFromWhichWeSubtract = valueFromWhichWeSubtract.nextDigit ?: CustomVariableDigit(0, null)
            }
            while (floatA != null && floatA.digit == (0).toByte()) {
                floatA = floatA.nextDigit
            }
            return Pair(
                CustomVariable(valA.isPositive() == valB.isPositive(), result, null),
                CustomVariable(valA.isPositive(), valueFromWhichWeSubtract, floatA)
            )
        }

        private fun divideInt(
            intA: CustomVariableDigit,
            intB: CustomVariableDigit?,
        ): Pair<CustomVariableDigit?, CustomVariableDigit> {
            var intAReversed = intA.reverse(false)
            var valueFromWhichWeSubtract = CustomVariableDigit(intAReversed?.digit ?: 0, null)
            intAReversed = intAReversed?.nextDigit
            var result: CustomVariableDigit? = null
            for (i in 0..intA.size()) {
                val (numberOfSubtractions, value) = subtractForDivision(valueFromWhichWeSubtract, intB)
                valueFromWhichWeSubtract = value
                result = if (result != null || numberOfSubtractions > 0) CustomVariableDigit(
                    numberOfSubtractions,
                    result
                ) else null

                if (intAReversed == null) {
                    break
                }
                valueFromWhichWeSubtract = CustomVariableDigit(
                    intAReversed.digit,
                    if (valueFromWhichWeSubtract.isZero()) null else valueFromWhichWeSubtract
                )
                intAReversed = intAReversed.nextDigit
            }
            return Pair(result, valueFromWhichWeSubtract)
        }

        private fun subtractForDivision(
            value: CustomVariableDigit,
            intB: CustomVariableDigit?
        ): Pair<Byte, CustomVariableDigit> {
            var valueFromWhichWeSubtract = value
            var numberOfSubtractions: Byte = 0
            var subResult = subtract(
                CustomVariable(true, valueFromWhichWeSubtract, null),
                CustomVariable(true, intB, null)
            )
            while (subResult.isPositive()) {
                numberOfSubtractions++
                valueFromWhichWeSubtract = subResult.getInt() ?: CustomVariableDigit(0, null)
                subResult = subtract(
                    CustomVariable(true, subResult.getInt(), null),
                    CustomVariable(true, intB, null)
                )
            }
            return Pair(numberOfSubtractions, valueFromWhichWeSubtract)
        }

        fun multiplyNumbersToMakeDivisorAnInteger(
            a: CustomVariable,
            b: CustomVariable
        ): Triple<CustomVariableDigit?, CustomVariableDigit?, CustomVariableDigit?> {
            var intB: CustomVariableDigit? = b.getInt()
            var floatB: CustomVariableDigit? = b.getFloat()
            var intA = a.getInt()
            var floatA = a.getFloat()
            while (floatB != null) {
                intB = CustomVariableDigit(
                    floatB.getLast()?.digit ?: 1,
                    if (intB?.isZero() != false) null else intB
                )
                floatB = floatB.removeLast()
                intA = CustomVariableDigit(floatA?.getLast()?.digit ?: 0, intA)
                floatA = floatA?.removeLast()
            }
            return Triple(intA, floatA, intB)
        }
    }
}