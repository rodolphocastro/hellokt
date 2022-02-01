/*
    helloKt - A simple sandbox for playing around with Kotlin features
    Copyright (C) 2022  Rodolpho Alves
 */
@file:Suppress("ClassName")

import kotlin.random.Random
import kotlin.test.*

/**
 * Tests featuring Kotlin scope helps!
 */
class `Scopes in Kotlin` {
    /**
     * A make-believe user for a system.
     */
    private data class User(val username: String, var isBanned: Boolean = false)

    @Test
    fun `let allows us to scope logic and run null-checks on objects, it returns the last expression on its block as a result`(): Unit {
        // Arrange
        val shouldExist = Random.nextBoolean()
        val subject: User? = if (shouldExist) User("ralves") else null

        // Act
        val got = subject?.let {
            // if subject isn't null we get access to it
            it.copy(isBanned = true)    // returns a banned copy of the user
        }

        // Assert
        if (shouldExist) {
            assert(got?.isBanned ?: false)
            return
        }

        assertNull(got)
    }

    @Test
    fun `run allows us to scope logic and run null-checks on objects, it returns the last expression on its block as a result`(): Unit {
        // Arrange
        val shouldExist = Random.nextBoolean()
        val subject: User? = if (shouldExist) User("ralves") else null

        // Act
        val got = subject?.run {
            // if subject isn't null we get access to "this"
            copy(isBanned = true)    // returns a banned copy of the user
        }

        // Assert
        if (shouldExist) {
            assert(got?.isBanned ?: false)
            return
        }

        assertNull(got)
    }

    @Test
    fun `with is a non-extension that allows us to access members of its arguments as if we were 'this'`(): Unit {
        // Arrange
        val shouldExist = Random.nextBoolean()
        val subject: User? = if (shouldExist) User("ralves") else null

        // Act
        val got = subject?.run {
            // if subject isn't null we get access to "this"
            copy(isBanned = true)    // returns a banned copy of the user
        }

        // Assert
        with(got) {
            assert(if (shouldExist) this != null else this == null)
        }
    }

    @Test
    fun `apply allows us to execute a block of code on an object, thus scoping to 'this' and returns the object itself with any modifications`(): Unit {
        // Arrange
        val subject = User("ralves")

        // Act
        //val got = subject?.apply { this.username = username.uppercase() }   // Won't compile, cannot reassign to val
        val got = subject?.apply { isBanned = true }?.isBanned

        // Assert
        assertNotEquals(false, got)
    }

    @Test
    fun `also allows us to "append" a block of code after something is executed, scoping the previous object to "it" within its scope`(): Unit {
        // Arrange
        var shadowCopy: User?

        // Act
        val subject = User("ralves").also { shadowCopy = it.copy(isBanned = true) }

        // Assert
        assertNotEquals(subject, shadowCopy)
    }
}