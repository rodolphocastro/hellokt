/*
    helloKt - A simple sandbox for playing around with Kotlin features
    Copyright (C) 2022  Rodolpho Alves
 */
@file:Suppress("ClassName") @file:OptIn(ExperimentalTime::class, ExperimentalCoroutinesApi::class)

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import org.junit.Test
import java.util.*
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.time.ExperimentalTime

data class Enemy(val name: String)

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
            while (isActive) {
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

    // up-next: https://kotlinlang.org/docs/flow.html#flow-builders
}