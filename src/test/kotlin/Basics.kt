@file:Suppress("ClassName")

import org.junit.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class `Kotlin basics` {

    /**
     * Kotlin's "void" is actually called "Unit".
     * You don't need to specify the void return type, the compiler automatically does that for you when it's void.
     */
    private fun `No parameters, no return`(): Unit {
        // Do nothing
    }

    /**
     * When you need to return something you need to tell the compiler what you're returning.
     * You can also declare obligatory parameters by not assigning them a default value.
     */
    private fun `Obligatory parameters, with a return type`(firstParam: String): String {
        return firstParam.uppercase()
    }

    /**
     * Optional parameters can be created by assigning a default value on creation.
     * If a function has multiple optional parameters may need to name them while invoking the function
     */
    private fun `Multiple named parameters`(firstParam: String = "hello", secondParam: Int = 2): String {
        return firstParam.repeat(secondParam)
    }

    /**
     * Any function with a single parameter may be turned into an "Infix" function
     * Infix funcions can be invoked by a single keyword and without parenthesis.
     */
    private infix fun Int.times(message: String): String {
        return message.repeat(this)
    }

    /**
     * A short explanation on how to declare functions in Kotlin.
     */
    @Test
    fun `Functions in kotlin`() {
        // Arrange
        val someString = "Cupcake"

        // Act
        val secondResult = `Obligatory parameters, with a return type`(someString)
        val thirdResult = `Multiple named parameters`(secondParam = 2, firstParam = someString)
        val fourthResult = 2 times someString   // Invoking an infix function

        // Assert
        `No parameters, no return`()    // Should not throw
        assertEquals(someString.uppercase(), secondResult)
        assertContains(thirdResult, someString, ignoreCase = true)
        assertEquals(thirdResult, fourthResult)
    }
}