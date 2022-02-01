/*
    helloKt - A simple sandbox for playing around with Kotlin features
    Copyright (C) 2022  Rodolpho Alves
 */
@file:Suppress("ClassName")

import kotlin.test.*

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
    fun `lists are ordered collections of items, they can be mutable or not`(): Unit {
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
    fun `sets are unordered collections of items, they cannot have duplicates and can be mutable or not`(): Unit {
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
    fun `maps are key-value oriented collections, they cannot have duplicate keys and can be mutable or not`(): Unit {
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

    /**
     * Creates a not-so-random list of Users.
     */
    private fun createListOfUsers(qty: Int): List<User> {
        val result = mutableListOf<User>()
        val baseUser = User("user-", UserPermission.User)
        val baseAdmin = User("admin-", UserPermission.Administrator)
        for (idx in 1..qty) {
            // Odds of being banned is 1 in 7
            val isBanned = (idx % 7 == 0)
            // Odds of being an Admin is 1 in 3
            val userOrAdmin = if (idx % 3 == 0) baseAdmin else baseUser
            // Creating our final subject based on the previous outcomes
            val finalUser = baseUser.copy(
                username = "${userOrAdmin.username}${idx}",
                permission = if (isBanned) UserPermission.Banned else userOrAdmin.permission
            )
            result.add(finalUser)
        }
        return result
    }

    /**
     * The "filter" function is the equivalent of the LINQ "where" function for C# nerds.
     * It allows us to apply a predicate to a collection and retrieve just the elements which the predicate returns true.
     */
    @Test
    fun `the filter function allows us to filter elements within a collection based on a predicate`(): Unit {
        // Arrange
        val listOfUsers = createListOfUsers(100)

        // Act
        val gotBanned =
            listOfUsers.filter { x -> x.permission == UserPermission.Banned }       // Using a complete lambda as a predicate
        val gotAdmins =
            listOfUsers.filter { it.permission == UserPermission.Administrator }    // Using the implicit "it" notation as a predicate

        // Assert
        for (user in gotBanned) {
            assert(user.permission == UserPermission.Banned)            // There should only be banned users in this list
        }
        for (user in gotAdmins) {
            assert(user.permission == UserPermission.Administrator)     // There should only be admins in this list
        }
        // Don't worry... there's a better way to query. We'll get there 🙂
    }

    /**
     * The "map" function is the equivalent of the LINQ "Select" function to C# nerds.
     * It allows us to apply a function to every element within a collection and return a brand new copy of the previous
     * collection with the applied mutations.
     */
    @Test
    fun `the map function allows us to run a function on every element of a collection`(): Unit {
        // Arrange
        val listOfUsers = createListOfUsers(1000)
        val bannedUsers = listOfUsers.filter { it.permission == UserPermission.Banned }

        // Act
        val gotBanAllUsers =
            listOfUsers.map { u -> u.copy(permission = UserPermission.Banned) } // Banning ALL users with a "verbose" lambda
        val gotUnbannedUsers =
            bannedUsers.map { it.copy(permission = UserPermission.User) } // Unbanning all the banned users with the implicit it operator

        // Assert
        assertNotEquals(listOfUsers, gotBanAllUsers)
        for (user in gotBanAllUsers) {
            assert(user.permission == UserPermission.Banned)
        }
        assertNotEquals(bannedUsers, gotUnbannedUsers)
        for (user in gotUnbannedUsers) {
            assert(user.permission != UserPermission.Banned)
        }
    }

    /**
     * Any, All and None are existential queries in function format.
     * They allow us to quickly execute a predicate on an entire collection and get its result.
     */
    @Test
    @Suppress("LocalVariableName")
    fun `there are three existential queries we can run on collections - any, all and none`(): Unit {
        // Arrange
        val listOfUsers = createListOfUsers(1000)
        val bannedUsers = listOfUsers.filter { it.permission == UserPermission.Banned }
        val nonBannedUsers = listOfUsers.filter { it.permission != UserPermission.Banned }

        // Act
        val `are there any Admins among all users?` = listOfUsers.any { it.permission == UserPermission.Administrator }
        val `are all banned users really banned?` = bannedUsers.all { it.permission == UserPermission.Banned }
        val `are there no banned users among active users` =
            nonBannedUsers.none() { it.permission == UserPermission.Banned }


        // Assert
        assert(`are there any Admins among all users?`)
        assert(`are all banned users really banned?`)
        assert(`are there no banned users among active users`)
    }

    @Test
    @Suppress("LocalVariableName")
    fun `find and findLast can be used to fetch the first, last or null item in the collection that matches a given predicate`(): Unit {
        // Arrange
        val listOfUsers = createListOfUsers(1000)

        // Act
        val `first found Admin` =
            listOfUsers.find { it.permission == UserPermission.Administrator }              // Finding the first Admin in the collection
        val `last found Admin` =
            listOfUsers.findLast { it.permission == UserPermission.Administrator }          // Finding the last Admin in the collection
        val `an user named Alligator` =
            listOfUsers.find { it.username.equals("Alligator", true) }    // Finding a user named Alligator

        // Assert
        assert(`first found Admin`?.permission == UserPermission.Administrator) // The first result should really be an Admin
        assert(`last found Admin`?.permission == UserPermission.Administrator)  // And so should the last one
        assertNull(`an user named Alligator`)                                   // Since we never have an Alligator, this should be null!
    }

    /**
     * The first and last functions allow us to find the first/last element in a collection that matches a given predicate.
     * They both throw an Exception if nothing is found.
     * The firstOrNull and lastOrNull return Null instead of Throwing in case nothing is found.
     */
    @Test
    @Suppress("LocalVariableName")
    fun `first, last, firstOrNull, lastOrNull can be used to fetch the first and last items in the collection that matches a given predicate`() {
        // Arrange
        val listOfUsers = createListOfUsers(1000)

        // Act
        val `first found Admin` =
            listOfUsers.first { it.permission == UserPermission.Administrator }              // Finding the first Admin in the collection
        val `last found Admin` =
            listOfUsers.last { it.permission == UserPermission.Administrator }          // Finding the last Admin in the collection
        val `first throws if nothing is found` = {
            val result = listOfUsers.first { it.username.equals("Alligator", true) }
        }
        val `last throws if nothing is found` = {
            val result = listOfUsers.last { it.username.equals("Alligator", true) }
        }
        val `an user named Alligator` = listOfUsers.lastOrNull() { it.username.equals("Alligator", true) }

        // Assert
        assertFailsWith(NoSuchElementException::class, `first throws if nothing is found`)
        assertFailsWith(NoSuchElementException::class, `last throws if nothing is found`)
        assert(`first found Admin`.permission == UserPermission.Administrator) // The first result should really be an Admin
        assert(`last found Admin`.permission == UserPermission.Administrator)  // And so should the last one
        assertNull(`an user named Alligator`)                                   // Since we never have an Alligator, this should be null!
    }


    @Test
    fun `count allows us to quickly realize how many items within a collection match a specific predicate`() {
        // Arrange
        val expected = 1000
        val listOfUsers = createListOfUsers(expected)

        // Act
        val got =
            listOfUsers.count()                                                         // Count of all the elements in the collection
        val gotAdmins =
            listOfUsers.count { it.permission == UserPermission.Administrator }         // Count of all the Admins in the collection

        // Assert
        assertEquals(expected, got)
        assert(gotAdmins < got)
    }
}