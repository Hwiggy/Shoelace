plugins {
    kotlin("jvm")
    id("com.github.johnrengelman.shadow")
}

dependencies {

    compileOnly(coreLibs.kotlin)
    compileOnly(coreLibs.spigot)
    compileOnly(coreLibs.nbt.api)

    api(coreLibs.kommander.spigot)
    api(coreLibs.regroup.spigot)
    api(coreLibs.konverse)

    implementation(coreLibs.kommander.proxy)
    compileOnly(coreLibs.proxy)

    api(project(":Modules:API"))
}

tasks.withType<Jar> {
    archiveBaseName.set("Shoelace")
}
