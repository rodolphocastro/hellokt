/*
    helloKt - A simple sandbox for playing around with Kotlin features
    Copyright (C) 2022  Rodolpho Alves
 */
@file:Suppress("ClassName")

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Tests featuring Kotlin Collections!
 */
class `Collections in Kotlin` {

    /**
     * Access level for a given user
     */
    private enum class UserPermission(val accessCode: Short) {
        Banned(0), User(1), Administrator(99);
    }

    /**
     * A make-believe user for a system.
     */
    private data class User(val username: String, val permission: UserPermission = UserPermission.User)

    /**
     * List<T> and MutableList<T>.
     */
    @Suppress("LocalVariableName")
    @Test
    fun `Lists are ordered collections of items, they can be mutable or not`(): Unit {
        // Arrange
        // An Admin that will be used on our test
        val adminUser = User(
            "ralves", UserPermission.Administrator
        )
        // A user that will be used on our test
        val additionalUser = User("john-doe-2k22")

        val `a mutable list of users` = mutableListOf<User>(
            adminUser, User("rodolpho.alves"), User("evil-doer-666", UserPermission.Banned)
        )   // Creating a mutable list of users (aka: a list we can add or remove elements from
        val `an immutable list of users` = listOf<User>(
            adminUser
        )   // Creating an immutable list of users (aka: a list we cannot add elements into or remove from)
        val `an immutable view of a mutable list`: List<User> =
            `a mutable list of users`   // Creating a "read only" view of an existing mutable list

        // Act
        `a mutable list of users`.add(additionalUser)               // Adding into an existing list
        `a mutable list of users`.add(additionalUser)               // Since lists don't care about duplication, we can add the user again
        //`an immutable list of users`.add(additionalUser)          // Does not compile! We cannot mutate a read-only List.
        //`an immutable view of a mutable list`.add(additionalUser) // Again: Does not compile! We cannot mutate a read-only view of a mutable list.

        // Assert
        assert(`a mutable list of users`.contains(additionalUser))              // The user must've been added to the list!
        assert(`an immutable view of a mutable list`.contains(additionalUser))  // and its read-only view!
        assert(!`an immutable list of users`.contains(additionalUser))          // But *never* to the read-only list!
    }

    /**
     * Set<T> and MutableSet<T>.
     */
    @Suppress("LocalVariableName")
    @Test
    fun `Sets are unordered collections of items, they cannot have duplicates and can be mutable or not`(): Unit {
        // Arrange
        // An Admin that will be used on our test
        val adminUser = User(
            "ralves", UserPermission.Administrator
        )
        // A user that will be used on our test
        val additionalUser = User("john-doe-2k22")

        val `a mutable set of users` = mutableSetOf<User>(
            adminUser, User("rodolpho.alves"), User("evil-doer-666", UserPermission.Banned)
        )   // Creating a mutable list of users (aka: a list we can add or remove elements from
        val `an immutable set of users` = setOf<User>(
            adminUser
        )   // Creating an immutable list of users (aka: a list we cannot add elements into or remove from)
        val `an immutable view of a mutable set`: Set<User> =
            `a mutable set of users`   // Creating a "read only" view of an existing mutable list

        // Act
        val firstGot =
            `a mutable set of users`.add(additionalUser)    // This addition into the existing Set will return true (because something was actually added!)
        val secondGot =
            `a mutable set of users`.add(additionalUser)    // This second addition will return false (because nothing was added since it was a duplicate)
        //`an immutable set of users`.add(additionalUser)   // Doesn't compile! Immutable sets can't be added onto
        //`an immutable view of a mutable set`             // Also doesn't compile! We cannot mutate a read-only view.

        // Assert
        assert(`a mutable set of users`.contains(additionalUser))               // The user must've been added to the set!
        assert(`an immutable view of a mutable set`.contains(additionalUser))   // and its read-only view!
        assert(!`an immutable set of users`.contains(additionalUser))           // But *never* to the read-only set!
        assert(firstGot)                                                        // Should be true because a value was added
        assert(!secondGot)                                                      // Should be false because nothing was added
    }

    /**
     * Map<T> and MutableMap<T>.
     */
    @Suppress("LocalVariableName")
    @Test
    fun `Maps are key-value oriented collections, they cannot have duplicate keys and can be mutable or not`(): Unit {
        // Arrange
        val baseScore = 0
        val expectedScore = 2

        // An Admin that will be used on our test
        val adminUser = User(
            "ralves", UserPermission.Administrator
        )
        // A user that will be used on our test
        val additionalUser = User("john-doe-2k22")

        val `a mutable map of users and scores` = mutableMapOf<User, Int>(adminUser to baseScore)
        val `an immutable map of users and scores` = mapOf<User, Int>(adminUser to baseScore)
        val `a read-only view of a mutable map`: Map<User, Int> = `a mutable map of users and scores`

        // Act
        `a mutable map of users and scores`[additionalUser] = 1 // Adding a new key:value into the map
        `a mutable map of users and scores`[adminUser] =
            `a mutable map of users and scores`.getValue(adminUser) + 2    // Incrementing the admin score to 2
        //`an immutable map of users and scores`[adminUser] =
        //    `a mutable map of users and scores`.getValue(adminUser) + 2 // Doesn't compile! Values are also immutable in a Map<T, A>
        //`an immutable map of users and scores`[additionalUser] = 1    // Doesn't compile! We cannot add into an immutable set
        //`a read-only view of a mutable map`[additionalUser] = 1

        // Assert
        assertEquals(
            expectedScore, `a mutable map of users and scores`[adminUser]
        )                                                                               // The admin's score must've been updated
        assert(`a mutable map of users and scores`.containsKey(additionalUser))         // An JohnDoe must've been added
        assert(!`an immutable map of users and scores`.containsKey(additionalUser))     // It must've not been added to the immutable map
        assert(`a read-only view of a mutable map`.containsKey(additionalUser))         // But should be available in the read-only view!
    }
}