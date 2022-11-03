dependencies {
    implementation(project(":Modules:API"))
    implementation(coreLibs.kommander.proxy)
    compileOnly(coreLibs.proxy)
}