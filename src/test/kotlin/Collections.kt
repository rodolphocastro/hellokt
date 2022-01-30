/*
    helloKt - A simple sandbox for playing around with Kotlin features
    Copyright (C) 2022  Rodolpho Alves
 */
@file:Suppress("ClassName")

import kotlin.test.Test

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
}