/*
    helloKt - A simple sandbox for playing around with Kotlin features
    Copyright (C) 2022  Rodolpho Alves
 */
@file:Suppress("ClassName")
@file:OptIn(ExperimentalTime::class)

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

class `Asynchronous programming in Kotlin` {
    @Test
    fun `A coroutine is similar to a thread - it takes some code and runs attempts to run it in parallel`(): Unit {
        // Tells we're running a blocking operation on this chunk
        // and it should wait for all coroutines within to complete
        // before unblocking
        runBlocking {
            // Arrange
            val expectedDelay: Duration = Duration.seconds(5)
            var isCompleted = false

            // Act
            // Launching a new coroutine on its own to wait a bit then change isCompleted to true
            launch {
                delay(expectedDelay)    // Holds execution on the current scope by a set duration
                isCompleted = true
                // Assert
                assert(isCompleted) // Should be true, because this is only executed after setting that var to true
            }


            // Assert
            // Should be false, since this block continues executing it'll read the initial value of the var
            // not the new value being set by the coroutine
            assertFalse(isCompleted)
        }
    }

    /**
     * A function with the 'suspend' keyword is granted its own Coroutine Scope by default,
     * which means it can benefit from other coroutines without having to create any type of coroutine scope.
     */
    private suspend fun returnCaps(source: String, delayBy: Duration): String {
        delay(delayBy)
        return source.uppercase()
    }
    
    @Test
    fun `In order to consume 'suspend' functions you need to create a scope, be it a blocking or a suspending one`(): Unit {
        // Arrange
        var result = "I have no mouth"
        val expected = result.uppercase()

        // Act
        /**
         * In order to invoke a 'suspend fun' we need to create a blocking call
         */
        runBlocking {
            result = returnCaps(result, Duration.seconds(5))
        }

        // Assert
        assertEquals(expected, result)
    }
}