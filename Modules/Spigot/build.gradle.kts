dependencies {
    implementation(project(":Modules:API"))
    implementation(coreLibs.kommander.spigot)
    compileOnly(coreLibs.spigot)
    compileOnly(coreLibs.nbt.api)
}