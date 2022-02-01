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
}