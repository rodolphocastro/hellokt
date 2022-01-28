/*
    helloKt - A simple sandbox for playing around with Kotlin features
    Copyright (C) 2022  Rodolpho Alves
 */
@file:Suppress("ClassName")

import org.junit.Test
import kotlin.math.exp
import kotlin.test.assertEquals

/**
 * Tests describing the flow of control in Kotlin.
 */
class `Kotlin's Control Flow` {

    /**
     * Kotlin provides us with the "when" statement and expression. It is a more flexible version of the
     * classic switch-case we see on C++, C# and Java.
     * "Cases" can be equality checks or type comparisons.
     */
    @Test
    fun `When is a more powerful version of the classic switch-case statement`() {
        // Arrange
        val subject = "cupcake"
        val expected = subject.uppercase()
        var statementGot: String?

        // Act
        // When can be used as a statement...
        when (subject) {
            is Nothing -> throw Exception("Never should have come here")
            "not a cupcake" -> throw Exception("Well, this is weird!")
            is String -> statementGot = expected
            else -> statementGot = "something unexpected"
        }

        // or as an expression...
        val expressionGot = when (subject) {
            is Nothing -> throw Exception("Never should have come here")
            "not a cupcake" -> throw Exception("Well, this is weird!")
            is String -> expected
            else -> "something unexpected"
        }

        // Assert
        assertEquals(expected, statementGot)
        assertEquals(expected, expressionGot)
    }

    /**
     * Kotlin provides us with the usual loop states foreach, while and do...while.
     */
    @Test
    fun `For, While and Do-While are the loops supported by Kotlin`() {
        // Arrange
        val subject = listOf("Abacaxi", "Banana", "Carambola")  // Creating an immutable list of Strings
        val expected = subject.size
        var (countFor, countWhile, countDoWhile) = listOf(0, 0, 0) // Creating and assigning multiple variables at once
        val actWithFor = {
            for (thing in subject) {
                countFor++
            }
        }
        val actWithWhile = {
            while (countWhile < subject.size) {
                countWhile++
            }
        }
        val actWithDoWhile = {
            do {
                countDoWhile++
            } while (countDoWhile < subject.size)
        }

        // Act
        actWithFor()
        actWithDoWhile()
        actWithWhile()

        // Assert
        assertEquals(expected, countFor)
        assertEquals(expected, countWhile)
        assertEquals(expected, countDoWhile)
    }

    /**
     * Range operators allow us to easily create and traverse through ranges.
     */
    @Suppress("LocalVariableName")
    @Test
    fun `Ranges allow us to handle control flow and assert upon objects`() {
        // Arrange
        val `from one, up to ten` = (1..10).toList()            // Creating a list generated from a full range
        val `odd numbers, up to ten` =
            (1..10 step 2).toList()  // Creating a list generated from a range that grows by 2 each step
        val `from ten, down to one` = (10 downTo 1).toList()    // Creating a list from a reverse range
        val `from A to Z` =
            ('A' until 'Z').toList()            // Creating a list of all characters in the alphabet, expect Z

        // Act

        // Assert
        assert(`from A to Z`.contains('B'))    // Checking if B is within the a to Z range
        assert(`odd numbers, up to ten`.contains(9))
        assertEquals(10, `from ten, down to one`.first())
        assertEquals(10, `from one, up to ten`.last())
    }

    /**
     * Kotlin doesn't have ternary operators and has two types of equality: Referential and Structural.
     * https://play.kotlinlang.org/byExample/02_control_flow/04_Equality%20Checks
     */
    @Test
    fun `Equality and Ternaries`() {
        // Arrange
        data class Food(val name: String)
        val foodName = "Bolinho de Arroz"
        val expected = Food(foodName)

        // Act
        val got = expected
        val otherGot = Food(foodName)

        // Assert
        assert(expected === got)    // Equality by reference (is the same object)
        assert(expected == got)     // Equality by structure (has the same structural values)
        assert(expected !== otherGot)   // They are not (referentially) equal because they are different variables
        assert(expected == otherGot)    // They are equal because they contain the same string
        @Suppress("RedundantIf")
        assert(if (got == null) false else true)    // Kotlin ternary equivalent
    }

}