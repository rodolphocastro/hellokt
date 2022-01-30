# HelloKT

This repository is a simple playground for Kotlin features and libraries.

## Building and Testing

Simply do a `gradlew test`, this is everything this project needs to be "executed".

## Project structure

The `main` subdirectory contains a dummy Console app that simply prints something.

The `test` subdirectory contains all the tests that describe the steps taken on playing with this language and its
libraries.

The `.github\workflows\gradle.yml` file contains the steps done in order to build and test this project.

The `build.gradle` file contains our dependencies and build configurations.

## Resources

Most of the tests in this project were based on the [Kotlin by Example](https://play.kotlinlang.org/byExample) website.

Some libraries tested out are from either [Awesome: Java](https://github.com/akullpp/awesome-java)
or [Awesome: Kotlin](https://github.com/KotlinBy/awesome-kotlin).

In order to figure out what the heck Gradle is on how it
works [check out the official Getting Started guide](https://docs.gradle.org/current/userguide/getting_started.html)
and [its resources](https://docs.gradle.org/current/userguide/what_is_gradle.html#what_is_gradle). Basically Gradle is
an automation tool for building JVM (Java, Kotlin, etc.) applications. Once you're acquainted to Gradle, check
out [Awesome Gradle](https://github.com/ksoichiro/awesome-gradle) as well.

## A TL:DR for C# folks

Kotlin is pretty similar to modern C# (think 9+) - the biggest hassle you'll have is getting used to Coroutines (which "
replaces" our beloved async-await and Tasks all over the place) and Gradle + Maven (they differ a lot from what we're
used to when it comes to csproj and NuGet).

JUnit behaves quite similarly to NUnit, even on its verbosity 🤷‍♂️. If you're used to xUnit, better read up on NUnit
prior to diving knee-deep into JUnit.