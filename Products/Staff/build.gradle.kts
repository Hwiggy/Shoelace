plugins {
    kotlin("jvm")
    id("com.github.johnrengelman.shadow")
}

dependencies {
    compileOnly(coreLibs.spigot)
    compileOnly(coreLibs.kotlin)
    compileOnly(project(":Modules:Implementation"))
    implementation("dev.samstevens.totp:totp:1.7.1")
}
tasks {
    shadowJar {
        minimize()
    }
}