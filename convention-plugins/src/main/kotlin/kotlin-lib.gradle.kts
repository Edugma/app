@file:Suppress("UnstableApiUsage")

plugins {
    id("java-library")
    kotlin("jvm")
    id("lint")
}

java {
    sourceCompatibility = JavaVersion.VERSION_19
    targetCompatibility = JavaVersion.VERSION_19

    sourceSets {
        val main by getting
        val test by getting
    }
}
