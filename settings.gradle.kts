rootProject.name = "Shoelace"

val regroupVersion = "1.1-SNAPSHOT"
val kommanderVersion = "1.7.2"
val konverseVersion = "1.0-SNAPSHOT"

val nbtApiVersion = "2.10.0"

val kotlinVersion = "1.8.10"

pluginManagement {
    plugins {
        kotlin("jvm") version "1.8.10"
    }
}

dependencyResolutionManagement {
    versionCatalogs {
        create("coreLibs") {
            library("kotlin", "org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")

            library("konverse", "me.hwiggy.konverse:Konverse:$konverseVersion")
            library("kommander-spigot", "me.hwiggy.kommander:Spigot:$kommanderVersion")
            library("kommander-proxy", "me.hwiggy.kommander:Proxy:$kommanderVersion")
            library("bungeecord-chat", "net.md-5:bungeecord-chat:1.19-R0.1-SNAPSHOT")

            library("regroup-api", "me.hwiggy.regroup:API:$regroupVersion")
            library("regroup-spigot", "me.hwiggy.regroup:Spigot:$regroupVersion")
            library("regroup-proxy", "me.hwiggy.regroup:Proxy:$regroupVersion")

            library("proxy", "net.md-5:bungeecord-api:1.19-R0.1-SNAPSHOT")
            library("spigot", "org.spigotmc:spigot-api:1.20.4-R0.1-SNAPSHOT")
            library("nbt-api", "de.tr7zw:item-nbt-api-plugin:$nbtApiVersion")
            library("hikari", "com.zaxxer:HikariCP:5.0.1")
        }
    }
}

include("Modules")
include("Modules:API")
include("Modules:Implementation")

include("Products")
include("Products:Farming")
include("Products:Sleeping")
include("Products:Staff")
include("Products:Graves")
