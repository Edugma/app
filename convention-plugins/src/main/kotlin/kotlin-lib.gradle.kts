@file:Suppress("UnstableApiUsage")

import gradle.kotlin.dsl.accessors._4590a75db674704ccaebc8ee790afa92.implementation
import org.gradle.accessors.dm.LibrariesForLibs

plugins {
    id("java-library")
    kotlin("jvm")
    id("lint")
}

// https://github.com/gradle/gradle/issues/15383
val libs = the<LibrariesForLibs>()

java {
    sourceCompatibility = JavaVersion.VERSION_19
    targetCompatibility = JavaVersion.VERSION_19

    sourceSets {
        val main by getting
        val test by getting
    }
}

dependencies {
    implementation(libs.uuid)
}

