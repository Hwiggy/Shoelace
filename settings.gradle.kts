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
            library("bungeecord-chat", "net.md-5:bungeecord-chat:1.19-R0.1-SNAPSHOT")
            library("proxy", "net.md-5:bungeecord-api:1.19-R0.1-SNAPSHOT")
            library("spigot", "org.spigotmc:spigot-api:1.19.2-R0.1-SNAPSHOT")
            library("hikari", "com.zaxxer:HikariCP:5.0.1")
        }
    }
}

include("Modules")
include("Modules:API")
include("Modules:Proxy")
include("Modules:Spigot")

include("Products")