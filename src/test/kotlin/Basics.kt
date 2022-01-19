/*
    helloKt - A simple sandbox for playing around with Kotlin features
    Copyright (C) 2022  Rodolpho Alves
 */
@file:Suppress("ClassName")

import org.junit.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

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

    /**
     * The basics for variables in Kotlin.
     */
    @Test
    fun `Variables and assignment`() {
        // Arrange
        val `this is a constant` = "something that is immutable and has its type inferred"  // val is the keyword for constants
        val `this is a typed constant`: () -> Unit = {} // you can help the compiler to infer a type by providing : Type
        var `this is a variable` = `this is a constant` // var is the keyword for variables
        `this is a variable` += `this is a variable`    // vars can be reassigned
        //`this is a constant` = `this is a variable`     // vals cannot, uncommenting would break the compiler

        // Act
        `this is a typed constant`()    // Invoking a val that contains a lambda/anonymous function

        // Assert
        assertNotEquals(`this is a constant`, `this is a variable`)
    }

    /**
     * Nullity is a lot different between Kotlin and most OO languages (Java, C#)
     */
    @Test
    fun `Null safety`() {
        // Arrange
        var `no nulls ever allowed` = "This won't ever be null" // Unless we specify, the compiler always infer non-nullable types
        //`no nulls ever allowed` = null        // Uncommenting this would break the compiler
        var `nulls are allowed here`: String? = "This might be null"    // By specifying something as nullable (? operator), we can now assign nulls
        `nulls are allowed here` = null     // It just works

        fun act(aString: String): String {
            return aString.uppercase()
        }

        // Act
        val firstResult = act(`no nulls ever allowed`)
        //val secondResult = act(`nulls are allowed here`)    // Uncomment for Compilation error
        val secondResult = act(`nulls are allowed here` ?: "Oh no") // By using the null coalesce operator "?:" we can pass in a default value in case a variable might be null

        // Assert
        assertEquals(`no nulls ever allowed`.uppercase(), firstResult)
        assertEquals("Oh no".uppercase(), secondResult)
    }
}