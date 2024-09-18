plugins {
    kotlin("jvm")
    id("com.github.johnrengelman.shadow")
}

dependencies {
    compileOnly(coreLibs.spigot)
    compileOnly(coreLibs.kotlin)
    compileOnly(project(":Modules:Implementation"))
}