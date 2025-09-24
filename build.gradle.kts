plugins {
    kotlin("jvm")
    id("fabric-loom")
    `maven-publish`
    java
}

group = property("maven_group")!!
version = property("mod_version")!!

repositories {
    maven("https://maven.impactdev.net/repository/development/")

    maven(url = "https://maven.nucleoid.xyz/") { name = "Nucleoid" } // Server GUI

    maven(url = "https://s01.oss.sonatype.org/content/repositories/snapshots/") {
        name = "sonatype-oss-snapshots" // For MiniMessage snapshot builds
    }
}

dependencies {
    minecraft("com.mojang:minecraft:${property("minecraft_version")}")
    mappings("net.fabricmc:yarn:${property("yarn_mappings")}")
    modImplementation("net.fabricmc:fabric-loader:${property("loader_version")}")

    modImplementation("net.fabricmc.fabric-api:fabric-api:${property("fabric_api_version")}")
    modImplementation("com.cobblemon:fabric:${property("cobblemon_version")}")

    // LuckPerms API
    modImplementation ("me.lucko:fabric-permissions-api:${property("lucko_version")}")
    compileOnly ("net.luckperms:api:${property("luckperms_version")}")

    // MiniMessage
    modImplementation(include("net.kyori:adventure-platform-fabric:${property("adventure_platform_version")}")!!)

    // Server GUI
    modImplementation("eu.pb4:sgui:${property("sgui_version")}")
    include("eu.pb4:sgui:${property("sgui_version")}")
}

tasks {

    processResources {
        inputs.property("version", project.version)

        filesMatching("fabric.mod.json") {
            expand(mutableMapOf("version" to project.version))
        }
    }

    jar {
        from("LICENSE")
    }

    publishing {
        publications {
            create<MavenPublication>("mavenJava") {
                artifact(remapJar) {
                    builtBy(remapJar)
                }
                artifact(kotlinSourcesJar) {
                    builtBy(remapSourcesJar)
                }
            }
        }

        // select the repositories you want to publish to
        repositories {
            // uncomment to publish to the local maven
            // mavenLocal()
        }
    }

    compileKotlin {
        kotlinOptions.jvmTarget = "21"
    }

}

java {
    // Loom will automatically attach sourcesJar to a RemapSourcesJar task and to the "build" task
    // if it is present.
    // If you remove this line, sources will not be generated.
    withSourcesJar()
}



// configure the maven publication