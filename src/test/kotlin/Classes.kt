/*
    helloKt - A simple sandbox for playing around with Kotlin features
    Copyright (C) 2022  Rodolpho Alves
 */
@file:Suppress("ClassName")

import org.junit.Test
import kotlin.math.exp
import kotlin.test.*

/**
 * Classes in Kotlin can have a default (aka primary) constructor defined at its declaration, which help us save a
 * few lines of code in comparison to C# and Java.
 * If any extra logic is required after the constructor is executed we can utilize an "init" method to run validation.
 */
open class Telephone(val longDistanceDigit: Int, val number: String) {
    init {
        if (longDistanceDigit <= 0) throw Exception("the digit should never be zero or less")
        if (number.isEmpty()) throw Exception("a telephone number should never be empty")
    }
}

/**
 * This is a class that inherits Telephone but sets a constant for the longDigit.
 * In order to make classes inheritable we need to add the "open" keyword to a method or superclass declaration.
 */
class BrazilianTelephone(number: String) : Telephone(55, number)

/**
 * Data Classes in Kotlin are a special type of class meant to help you more easily store data.
 * The compiler will work its magic to automatically give us the toString, equals and hashCode methods.
 * Additionally, a copy method and "componentN" methods are also provided, to help you handle the data within such objects.
 */
data class Contact(val name: String, val age: Int)

private const val s = "1234-4321"

class `Kotlin classes` {

    @Test
    fun `a class with a primary constructor and properties`() {
        // Arrange
        val expectedDigit = 41
        val expectedNumber = "9 9877-2345"
        var subject: Telephone? = null
        val act = {
            subject = Telephone(expectedDigit, expectedNumber)
        }

        // Act
        act()

        // Assert
        assertNotNull(subject)
        assertEquals(expectedDigit, subject?.longDistanceDigit)
        assertEquals(expectedNumber, subject?.number)
    }

    @Test
    fun `a class init method can contain validation logic`() {
        // Arrange
        var subject: Telephone? = null
        val act = {
            try {
                // This will throw, thus subject will continue being null
                subject = Telephone(-41, "")
            } catch (ex: Exception) {
                // Do nothing
            }
        }

        // Act
        act()

        // Assert
        assertNull(subject)
    }

    @Test
    fun `a data class is a special class with some methods scaffolded by the compiler, usually they're done for immutability`() {
        // Arrange
        val subject = Contact("Alves, Rodolpho", 29)
        val (expectedName, unexpectedAge) = subject // Using destructuring to fetch values from a data class

        // Act
        val agedSubject =
            subject.copy(age = subject.age + 1)    // Using the copy method to create a new subject, with a different age
        val (gotName, gotAge) = agedSubject // Again using destructuring

        // Assert
        assertEquals(expectedName, gotName)
        assertNotEquals(unexpectedAge, gotAge)
    }

    @Test
    fun `an open class or method allows others to inherit it and modify behavior`() {
        // Arrange
        val expectedPhone = "9 9999-9999"
        val subject = BrazilianTelephone(expectedPhone)
        var otherSubject: Telephone? = null
        val act = {
            try {
                // This will throw because Telephone's "init" method will throw.
                otherSubject = BrazilianTelephone("")
            } catch (ex: Exception) {
                // Do nothing
            }
        }

        // Act
        act()

        // Assert
        assertEquals(expectedPhone, subject.number)
        assertEquals(55, subject.longDistanceDigit)
        assertNull(otherSubject)
    }

    @Test
    fun `classes can also contain generic parameters`() {
        // Arrange
        /**
         * In Kotlin we can have generic arguments for classes' members and functions.
         * In order for that to work, in a class level, you need to declare the generic constraint at the class' declaration
         * Afterwards, for methods, you'll need to declare it before the method if it's different than the class' level
         * generic constraint.
         */
        class MagicTelephone<E>(longDistanceDigit: Int, number: String, val genericThing: E) : Telephone(longDistanceDigit, number) {
            fun getThing(): E {
                return genericThing
            }

            fun <T> getSomethingElse() : T {
                return genericThing as T
            }

            override fun toString(): String {
                return number
            }
        }
        val expected = null
        val number = "1234-4321"
        val subject = MagicTelephone(55, number, expected)
        val secondSubject = MagicTelephone(55, number, subject)

        // Act
        val got = subject.getThing()
        val gotAgain = secondSubject.getSomethingElse<Telephone>().number

        // Assert
        assertEquals(expected, got)
        assertEquals(number, gotAgain)
    }
}