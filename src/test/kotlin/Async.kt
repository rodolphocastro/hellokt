/*
    helloKt - A simple sandbox for playing around with Kotlin features
    Copyright (C) 2022  Rodolpho Alves
 */
@file:Suppress("ClassName") @file:OptIn(ExperimentalTime::class, ExperimentalCoroutinesApi::class)

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.produce
import kotlin.math.exp
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
            expected, result
        )  // Since we only allowed the job to compute twice - we should be back to the expected value
    }

    /**
     * In order to share information between different coroutines one can utilize a "Channel<T>".
     */
    @Test
    fun `Channels are ways different Scopes can communicate and process data`() = runBlocking {
        // Arrange
        val expected = 42.0
        val times = 4
        val increment = expected / times
        val aChannel = Channel<Double>()
        var result = 0.0

        // Act
        // Launching a coroutine that sends information into the channel
        launch {
            for (i in 1..times) {
                aChannel.send(increment)    // Sending information into the channel
                delay(50)
            }
            aChannel.close()    // Tell consumers that we're done with this channel
        }

        // Iterating the channel while it's not closed
        for (piece in aChannel) {
            result += piece
        }

        // Assert
        assertEquals(expected, result)
    }

    /**
     * For Producer-Consumer (or Observer-Observable) scenarios we can leverage the official "produce" and
     * "consumeEach" functions to allow us to cut down on boilerplate.
     */
    @Test
    fun `The produce and consumeEach extension methods can be used to handle Producer and Consumer scenarios`(): Unit =
        runBlocking {
            // Arrange
            val expected = 42.0
            val times = 5
            val increment = expected / times
            var result = 0.0

            // Act
            // Using the produce function we can create a Couroutine and a Channel in a single go
            val act = produce {
                for (i in 1..times) {
                    send(increment)
                    delay(50)
                }
                // Notice how we don't need to *close* the channel!
            }
            // and by using "consumeEach" function we can react to whatever comes out from that channel!
            act.consumeEach { result += it }

            // Assert
            assertEquals(expected, result)
        }

    @Test
    fun `A pipeline is a pattern where one coroutine produces and another routines consumes while filtering`() =
        runBlocking {
            // Arrange
            val base = (1..10).toList().toIntArray()    // Creating an array from 1 to 10
            val result = mutableListOf<Int>()
            val expected = base.filter { it % 2 == 0 }.toIntArray()  // Expected pairs

            /**
             * A producer that sends all numbers from 1 to 10.
             */
            val `produces ints from 1 to 10` = produce {
                for (i in base) {
                    send(i)
                    delay(10)
                }
            }

            /**
             * Extension function that filters something from a receiving channel and produces it back
             */
            fun CoroutineScope.filterPairs(channel: ReceiveChannel<Int>) = produce() {
                for (number in channel) if (number % 2 == 0) send(number)
            }

            // Act
            /**
             * Actually filtering what's coming from the channel and adding into the result set.
             */
            filterPairs(`produces ints from 1 to 10`).consumeEach { result.add(it) }

            // Assert
            assertContentEquals(expected, result.toIntArray())
        }

    /**
     * When using Channels coroutine automatically split up consumption so we duplications don't occur.
     */
    @Test
    fun `Multiple coroutines can read from the same channel`() = runBlocking {
        // Arrange
        val expected = (1..10).toList()
        val consumers = mutableSetOf<Int>()
        val result = mutableListOf<Int>()
        val producesExpected = produce {
            for (i in expected) {
                send(i)
                delay(1)
            }
        }

        // Act
        /**
         * Creating 5 jobs that run in parallel to consume from the channel
         */
        repeat(5) { jobNum ->
            launch {
                consumers.add(jobNum)
                producesExpected.consumeEach {
                    println("Scope $jobNum consumed $it")
                    result.add(it)
                    val delayTime = jobNum + it
                    delay(delayTime.toLong())
                }
            }
        }

        delay(100)  // Ensuring we give some time for both jobs to run

        // Assert
        assertContentEquals(expected.sortedDescending(), result.sortedDescending())
        assertTrue { consumers.count() == 5 }
    }

    /**
     * Multiple coroutines can also output into the same channel - allowing other to consume from a single place.
     */
    @Test
    fun `Multiple coroutines can send that into the same channel`() = runBlocking {
        // Arrange
        val expected = (1..20).toList()
        val theIntegerChannel = Channel<Int>()
        val result = mutableListOf<Int>()

        // Act
        /**
         * Creating two producers - one that will send down evens and one that will send down odds.
         * Once both finish running we should see all numbers (from the expected list) being sent, regardless of their
         * output order.
         */
        repeat(2) { repeatNum ->
            val consumer = repeatNum + 1
            launch {
                if (consumer == 1) {
                    expected.filter { it % 2 == 0 }.forEach {
                        println("$consumer sending down $it")
                        theIntegerChannel.send(it)
                    }
                } else {
                    expected.filter { it % 2 != 0 }.forEach {
                        println("$consumer sending down $it")
                        theIntegerChannel.send(it)
                    }
                }
            }
        }

        // Giving some time for the producers to run
        delay(100)
        repeat(20) {
            val receivedInt = theIntegerChannel.receive()
            result.add(receivedInt)
        }
        coroutineContext.cancelChildren()

        // Assert
        assertContentEquals(expected.sortedDescending(), result.sortedDescending())
    }

    @Test
    fun `Buffered channels allow us to throttle the rate at which events are sent`() = runBlocking {
        // Arrange
        /**
         * A channel with a buffer size of 2 items
         */
        val channel = Channel<Int>(3)
        val expected = listOf<Int>(1, 2, 3)
        val result = mutableListOf<Int>()

        // Act
        /**
         * Attempting to send 10 ints into that channel
         */
        val act = launch {
            try {
                repeat(10) {
                    channel.send(it + 1)
                }
            } finally {
                // Upon cancellation, close the channel
                channel.close()
            }
        }

        // Wait for a while, then cancel
        delay(250)
        act.cancel()
        /**
         * Reacting to channel changes.
         */
        channel.consumeEach { result.add(it) }

        // Assert
        assertContentEquals(expected, result)
    }

    // up-next: Tickers
    // https://kotlinlang.org/docs/channels.html#ticker-channels
}
