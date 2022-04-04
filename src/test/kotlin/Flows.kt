/*
    helloKt - A simple sandbox for playing around with Kotlin features
    Copyright (C) 2022  Rodolpho Alves
 */
@file:Suppress("ClassName") @file:OptIn(ExperimentalTime::class, ExperimentalCoroutinesApi::class)

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.junit.Test
import java.util.*
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue
import kotlin.time.ExperimentalTime

/**
 * A fake enemy for testing purposes.
 */
data class Enemy(val name: String) {
    companion object {

        /**
         * Generates a set amount of Enemies.
         */
        fun generateEnemies(quantity: Int): Flow<Enemy> = flow {
            repeat(quantity) {
                emit(createEnemy())
            }
        }
    }
}

/**
 * Creates a random enemy.
 */
fun createEnemy(): Enemy {
    return Enemy(UUID.randomUUID().toString())
}

class `Asynchronous Enumerations in Kotlin` {

    @Test
    fun `Flows are coroutines that return multiple elements without blocking the caller`() = runBlocking {
        // Arrange
        val expected = listOf(1, 1, 2, 3, 5, 8, 13)
        val got = mutableListOf<Int>()
        val flowAllExpected = flow {
            for (element in expected) emit(element)
        }

        // Act
        // By collecting results from the flow we can invoke other functions to react to them.
        flowAllExpected.collect() { got.add(it) }

        // Assert
        assertContentEquals(expected, got)
    }

    @Test
    fun `Flows can take a while and - still - wont block the caller`() = runBlocking {
        // Arrange
        val got = mutableListOf<Enemy>()
        val timeBetweenEnemies: Long = 50
        val continuouslyCreateEnemies = flow {
            while (true) {
                emit(createEnemy()) // now return something
                delay(timeBetweenEnemies)   // wait a bit, be lazy!
            }
        }

        // Act
        // Give it time to create only 2 enemies
        withTimeoutOrNull(2 * timeBetweenEnemies) {
            continuouslyCreateEnemies.collect() { got.add(it) }
        }

        // Assert
        assertEquals(2, got.count())
    }

    @Test
    fun `Flows are cold, which means that their logic is only called once someone collects them`() = runBlocking {
        // Arrange
        val numberOfEmits = 5
        val expectedCollectCalls = 2
        val firstGot = mutableListOf<Enemy>()
        val secondGot = mutableListOf<Enemy>()
        var timesCollected = 0
        val letItFlow = flow {
            timesCollected++    // Only incremented once collect is called!
            repeat(numberOfEmits) {
                emit(createEnemy()) // Creating something random every emit
            }
        }

        // Act
        letItFlow.collect() { firstGot.add(it) }    // Collecting a first time...
        letItFlow.collect() { secondGot.add(it) }   // Collecting a second time!

        // Assert
        assertEquals(
            expectedCollectCalls, timesCollected, "because the value should only be incremented when collected"
        )
        assertNotEquals(
            firstGot.first(), secondGot.first(), "because each time we call the flow a new random emit is done"
        )
    }

    @Test
    fun `There are different type of builders we can use to create flows`() = runBlocking {
        // Arrange
        val expectedRange = 1..50 step 3
        val expectedAsArray = expectedRange.toList().toTypedArray()
        val expected = expectedRange.toList()

        // Act
        val asFlow = expected.asFlow()  // Creating a flow from an existing collection
        val flowOf = flowOf(*expectedAsArray) // Creating a flow to emit a fixed set of values
        // note: that * is not a pointer - it's a spread operator
        val flow = flow {
            for (i in expectedRange) emit(i)
        }   // Usual way to create a flow

        val gotAsFlow = asFlow.toList()
        val gotFlowOf = flowOf.toList()
        val gotFlow = flow.toList()

        // Assert
        assertContentEquals(expected, gotAsFlow)
        assertContentEquals(expected, gotFlowOf)
        assertContentEquals(expected, gotFlow)
    }

    /**
     * Basic operators such as map and filter allow us to mutate a flow's output without blocking it.
     */
    @Test
    fun `Intermediate operators allow us to mutate outputs from Flows without blocking`() = runBlocking {
        // Arrange
        val numberOfEnemies = 5
        val aName = "An Enemy"
        val expected = Enemy.generateEnemies(numberOfEnemies).toList()
        val expectedMapped = expected.map { it.copy(name = aName) }
        val expectedFiltered = expected.filter { it.name.contains("6", true) }
        val baseFlow = expected.asFlow()

        // Act
        val gotMap = mutableListOf<Enemy>()
        val gotFiltered = mutableListOf<Enemy>()
        baseFlow.map { it.copy(name = aName) }
            .collect { gotMap.add(it) } // Mapping out the flow's outputs into a new flow, then collecting
        baseFlow.filter { it.name.contains("6", true) }
            .collect { gotFiltered.add(it) } // Filtering out the flow's outputs into a new flow, then collecting

        // Assert
        assertContentEquals(expectedMapped, gotMap)
        assertContentEquals(expectedFiltered, gotFiltered)
    }

    /**
     * The transform operator allows us to do more complex routines
     */
    @Test
    fun `The transform operator allow us to expand upon the basic intermediate operators by leveraging emit`() =
        runBlocking {
            // Arrange
            val numberOfEnemies = 5
            val expected = Enemy.generateEnemies(numberOfEnemies).toList()
            val baseFlow = expected.asFlow()

            // Act
            val got = mutableListOf<Enemy>()
            /**
             * By using Transform we can mutate, emit different things or even do some logging.
             */
            baseFlow.transform {
                emit(it)
                println("double emitting ${it.name} as a Double")
                emit(it.copy(name = "Double"))
            }.collect() {
                got.add(it)
            }

            // Assert
            assertEquals(numberOfEnemies * 2, got.count())
        }

    @Test
    fun `The take operator allows us to limit what is received from a flow - then cancel its execution`() =
        runBlocking {
            // Arrange
            val enemy = Enemy("Grafted Scion")
            var cancelled = false
            val subject = flow {
                try {
                    while (true) {
                        emit(enemy)
                        delay(1000000)  // Wait for a very long time if someone wants more enemies
                    }
                } finally {
                    cancelled = true
                }
            }

            // Act
            val got = mutableListOf<Enemy>()
            subject.take(1).collect() { got.add(it) }

            // Assert
            assertTrue { cancelled }
            assertEquals(enemy, got.firstOrNull())
        }

    @Test
    fun `Terminal operators are suspending functions that start the collection of a flow`(): Unit = runBlocking {
        // Arrange

        // Act

        // Assert
    }
}