/*
    helloKt - A simple sandbox for playing around with Kotlin features
    Copyright (C) 2022  Rodolpho Alves
 */
@file:Suppress("ClassName")

import org.junit.Test
import kotlin.test.assertEquals

/**
 * Test describing functional programming features in Kotlin.
 */
class `Functional Features in Kotlin` {

    /**
     * A local that returns a number!
     */
    private fun returnsANumber(baseResult: Int) = baseResult

    /**
     * A local function that returns the triple of a doubled number.
     */
    private fun triplesANumber(baseNumber: Int, returnsNumber: (Int) -> Int): Int {
        return returnsNumber(baseNumber) * 3
    }

    /**
     * A local function that receives a function and returns another function
     */
    private fun takesAFunctionReturnsAFunction(lhs: Int, rhs: Int, operation: (Int, Int) -> Int): () -> Int {
        val result = {
            operation(lhs, rhs)
        }
        return result
    }

    @Test
    fun `Functions can be used as parameters and returns for other functions`() {
        // Arrange
        val expectedBase = 2

        /**
         * Creating a lambda to pass as a parameter later
         */
        val act = { returnsANumber(expectedBase) }

        /**
         * A lambda with parameters!
         */
        val multiply = { a: Int, b: Int ->
            a * b
        }

        /**
         * A local function that receives a function as a parameter
         */
        fun doubleANumber(returnsAnInt: () -> Int): Int {
            return returnsAnInt() * 2
        }

        // Act
        /**
         * Passing in a (lambda) function to a function to get an actual result.
         */
        val got = doubleANumber(act)
        // :: is the equivalent of the "nameOf" in C#.
        // the :: operator allows us to reference a class, function or method by its name
        val gotTriple = triplesANumber(expectedBase, ::returnsANumber)
        // anotherGot is a function, it's not a final result yet!
        val anotherGot = takesAFunctionReturnsAFunction(5, 4, multiply)

        // Assert
        assertEquals(expectedBase * 2, got)
        assertEquals(expectedBase * 3, gotTriple)
        assertEquals(5 * 4, anotherGot())   // Lazily invoking anotherGot
    }
}