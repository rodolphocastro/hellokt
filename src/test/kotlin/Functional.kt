/*
    helloKt - A simple sandbox for playing around with Kotlin features
    Copyright (C) 2022  Rodolpho Alves
 */
@file:Suppress("ClassName")

import org.junit.Test
import kotlin.math.exp
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

    /**
     * Since we can reference and store functions Kotlin allows us to use higher-order functions.
     * Aka: Passing and returning functions just works. For the C# nerds: This means we can use Action and Func on Kt ;)
     */
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

    /**
     * We already used some in the previous tests but hey! Time for a specific explanation for Lambdas :)
     */
    @Suppress("LocalVariableName")
    @Test
    fun `Lambdas (aka ad-hoc functions) are also available in Kotlin, with and without type inference`(): Unit {
        // Arrange
        val base = 2
        val expected = base * 4

        val `a Lambda without ANY type inference`: (Int) -> Int =
            { subject: Int -> subject * 4 }  // Quite verbose, right?
        val `a Lambda with type inference on the inside`: (Int) -> Int =
            { subject -> subject * 4 } // A bit tidier... but we can do better
        val `a Lambda with type inference on the outside` = { subject: Int ->
            subject * 4
        } // A lot better!
        // val aLambdaThatWontCompile = { subject -> subject * 4 }
        // The line above won't compile because there's not enough information for types to be inferred
        val `a Lambda with a single parameter can use the it keyword inside`: (Int) -> Int = {
            it * 4
        }  // As long as there's only one parameter you can use the "it" variable instead of naming stuff just for lulz

        // Act
        val firstGot = `a Lambda without ANY type inference`(base)
        val secondGot = `a Lambda with type inference on the inside`(base)
        val thirdGot = `a Lambda with type inference on the outside`(base)
        val fourthGot = `a Lambda with a single parameter can use the it keyword inside`(base)  // The cleanest one imho

        // Assert
        assertEquals(expected, firstGot)
        assertEquals(expected, secondGot)
        assertEquals(expected, thirdGot)
        assertEquals(expected, fourthGot)
    }
}