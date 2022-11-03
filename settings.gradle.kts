rootProject.name = "Shoelace"

pluginManagement {
    plugins {
        kotlin("jvm") version "1.7.20"
    }
}

dependencyResolutionManagement {
    versionCatalogs {
        create("coreLibs") {
            library("kommander-spigot", "me.hwiggy.kommander:Spigot:1.7.2")
            library("kommander-proxy", "me.hwiggy.kommander:Proxy:1.7.2")
            library("proxy", "net.md-5:bungeecord-api:1.19-R0.1-SNAPSHOT")
            library("spigot", "org.spigotmc:spigot-api:1.19.2-R0.1-SNAPSHOT")
        }
    }
}

include("Modules")
include("Modules:API")
include("Modules:Proxy")
include("Modules:Spigot")

include("Products")