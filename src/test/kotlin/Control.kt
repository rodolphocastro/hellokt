/*
    helloKt - A simple sandbox for playing around with Kotlin features
    Copyright (C) 2022  Rodolpho Alves
 */
@file:Suppress("ClassName")

import org.junit.Test
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
}