/*
    helloKt - A simple sandbox for playing around with Kotlin features
    Copyright (C) 2022  Rodolpho Alves
 */
@file:Suppress("ClassName") @file:OptIn(ExperimentalTime::class)

import kotlinx.coroutines.*
import kotlin.test.*
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

class `Asynchronous programming in Kotlin` {
    @Test
    fun `A coroutine is similar to a thread - it takes some code and runs attempts to run it in parallel`(): Unit {
        // Tells we're running a blocking operation on this chunk,
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

    /**
     * Similar to C#'s "Task" we can spin off Jobs from within a CoroutineScope by using the launch{} block.
     * One can then "join" the job to await its completion.
     */
    @Test
    fun `One can spin-off new coroutines (and track their progress) by using the launch function when no return is expected`() =
        runBlocking {
            // Arrange
            var result = 1
            val expected = 2

            // Act
            val incrementByOne = launch {
                delay(1000) // Wait a bit, just for the lulz
                result++    // Increment the result variable
            }

            // Assert
            assertNotEquals(
                expected, result
            )   // Since incrementByOne waits for 1s we don't expect result to have been incremented yet
            assertFalse(incrementByOne.isCompleted) // Neither should it be complete
            incrementByOne.join()   // Now we're telling this scope "yo, wait for that job to complete"
            assertEquals(expected, result)  // Finally - result should be expected
            assertTrue(incrementByOne.isCompleted)  // And the job should read "completed"
        }

    /**
     * Similar to C#'s "Task" we can spin off Jobs from within a CoroutineScope by using the async{} block.
     * One can then get the job's result by awaiting it.
     */
    @Test
    fun `One can spin off new coroutines by using the async function when a return is expected`() = runBlocking {
        // Arrange
        val expected = 42
        val deferredRoutine = async {
            expected
        }

        // Act
        assertFalse(deferredRoutine.isCompleted)    // Since we didn't await or anything, it should not be completed yet
        val result = deferredRoutine.await()

        // Assert
        assertEquals(expected, result)  // After awaiting and returning - things should just work (TM)
    }

    /**
     * One can cancel a coroutine by asking its job to be canceled.
     * It works the same way for async and launch created jobs, with the exception that awaiting on a cancelled
     * job throws an Exception.
     */
    @Test
    fun `Cancelling is supported on coroutines`() = runBlocking {
        // Arrange
        val expected = 42

        /**
         * A job that returns something.
         */
        val deferred = async {
            delay(50000)    // Simulating a very long operation
            45
        }

        var result = expected

        /**
         * Another job that mutates something.
         */
        val jobyJob = launch {
            delay(50000)
            result = 45
        }

        // Act
        delay(500)  // Wait for a while, just in case
        deferred.cancel()   // Manually cancelling the coroutine!
        jobyJob.cancelAndJoin() // Cancels and awaits the job
//      Errors out - because the job is already cancelled!
//      result = deferred.await()

        // Assert
        assertEquals(
            expected, result
        )  // Since we canceled the jobs - the result should never have mutated away from expected
        // And the jobs themselves should be cancelled.
        assertTrue(deferred.isCancelled)
        assertTrue(jobyJob.isCancelled)
    }

    /**
     * In order to gracefully support cancellation while doing a computation routine one must code its coroutine
     * in a way to check its active state. Whenever isActive returns false any computation should stop.
     */
    @Test
    fun `Coroutine cancellation while computing requires cooperation - aka you gotta code that in`() = runBlocking {
        // Arrange
        val expected = 42.0
        var result = expected / 4

        // a job that will be cancelled
        val keepMultiplying = launch {
            // by looking at "isActive" we can check if the coroutine is cancelled or not
            while (isActive) {
                delay(100)
                result *= 2
            }
        }

        // Act
        delay(225) // Waiting for a while to give the job some time to run
        keepMultiplying.cancelAndJoin() // Finally - cancel the job. This should halt the multiplication

        // Assert
        assertEquals(
            expected,
            result
        )  // Since we only allowed the job to compute twice - we should be back to the expected value
    }

}