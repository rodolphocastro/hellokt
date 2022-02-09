/*
    helloKt - A simple sandbox for playing around with Kotlin features
    Copyright (C) 2022  Rodolpho Alves
 */
@file:Suppress("ClassName")

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Test suite describing interfaces in Kotlin.
 */
class `Kotlin Interfaces` {

    /**
     * Interfaces in Kotlin are defined by using the interface keyword.
     */
    interface Playable {
        /**
         * They can define abstract methods
         * ie: methods that must be implemented from scratch.
         */
        fun play(): Unit

        /**
         * They can also define methods with default implementations.
         * ie: methods that can be overridden but work out-of-the-box.
         * (They don't need to be an expression body, this is just to save some lines)
         */
        fun reportProgress(message: String): String = message.uppercase()

        /**
         * They can define abstract values (immutable).
         */
        val nameOfTheGame: String

        /**
         * They can define abstract variables (mutable).
         * ie: mutable with getters and setters
         */
        var score: Int

        /**
         * They can also define properties with default implementations.
         */
        val gameTitle: String
            get() = "PLACEHOLDER GAME INC"
    }

    /**
     * In order to "implement" an interface you just need to declare it on the class.
     * Use the "override" keyword to tell the compiler you're implementing something from an interface.
     * Methods or Properties with default bodies don't need to be overridden unless you want to change their behavior.
     */
    class BethesdaGame(override val nameOfTheGame: String, override var score: Int) : Playable {
        /**
         * Since play is abstract we *need* to implement it.
         */
        override fun play() {
            TODO("Not yet implemented")
        }

        /**
         * Neither "gameTitle" nor "reportProgress" are abstract, thus we aren't obliged to override them.
         * Uncomment the lines below if you're like to see how we can extend their default functionalities
         */
        //override val gameTitle: String
        //    get() = "Elder Scrolls: $nameOfTheGame"
        //override fun reportProgress(message: String): String {
        //    return "$message: $score"
        //}
    }

    @Test
    fun `A class may implement one or more interfaces, thus allowing us to cast a class back to an interface`(): Unit {
        // Arrange
        val subject = BethesdaGame("Skyrim 2.0", 1)

        // Act
        val got: Playable = subject

        // Assert
        assertEquals(subject, got)
    }
}