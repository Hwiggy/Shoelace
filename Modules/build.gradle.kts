plugins {
    kotlin("jvm") apply false
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "org.jetbrains.kotlin.jvm")
    group = "me.hwiggy.shoelace"
    version = "1.0-SNAPSHOT"

    repositories {
        mavenLocal()
        mavenCentral()
        maven("https://nexus.mcdevs.us/repository/mcdevs")
        maven("https://repo.codemc.org/repository/maven-public/")
    }
}