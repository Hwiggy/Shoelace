plugins {
    kotlin("jvm")
}

dependencies {
    compileOnly(kotlin("stdlib"))
    compileOnly(coreLibs.bungeecord.chat)

    implementation(coreLibs.hikari)
    implementation(coreLibs.regroup.api)
}