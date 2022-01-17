/*
    helloKt - A simple sandbox for playing around with Kotlin features
    Copyright (C) 2022  Rodolpho Alves
 */

import org.junit.Test

import org.junit.Assert.*

class SomethingTest {

    @Test
    fun doSomething() {
        // Arrange
        val expected = 2
        val subject = Something()

        // Act
        val got = subject.DoSomething()

        // Assert
        assertEquals(expected, got)
    }
}