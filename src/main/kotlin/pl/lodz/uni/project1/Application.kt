package pl.lodz.uni.project1

import pl.lodz.uni.project1.customvariable.CustomVariable
import pl.lodz.uni.project1.customvariable.CustomVariableCalculator

class Application

fun main(args: Array<String>) {
    println("====================================")
    println("Calculator for numbers of any length")
    println("====================================")
    println("Examples of correct inputs handled by calculator: '123', '-25', '34,45', '-5623,56', '0,343', '-0,323', '0'")
    try {
        println("Insert first value:")
        val inputA = readln()
        val a = CustomVariable.parseCustomVariable(inputA)
        println("Insert second value:")
        val inputB = readln()
        val b = CustomVariable.parseCustomVariable(inputB)
        println()

        var selected = 0
        while (selected <= 0 || selected > 4) {
            println("Options:")
            println("1. Addition")
            println("2. Subtraction")
            println("3. Multiplication")
            println("4. Division")
            try {
                selected = readln().toInt()
            } catch (_: Exception) {
            }
        }
        println()

        when (selected) {
            1 -> {
                val result = CustomVariableCalculator.add(a, b)
                println("'$a'+'$b'='$result'")
            }

            2 -> {
                val result = CustomVariableCalculator.subtract(a, b)
                println("'$a'-'$b'='$result'")
            }

            3 -> {
                val result = CustomVariableCalculator.multiply(a, b)
                println("'$a'*'$b'='$result'")
            }

            4 -> {
                val result = CustomVariableCalculator.divide(a, b)
                println("'$a'/'$b'='${result.first}'; rest='${result.second}'")
            }
        }
    } catch (ex: Exception) {
        println("Something went wrong. ${ex.message}")
    }
}
