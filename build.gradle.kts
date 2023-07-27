import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.0"
    id("cc.polyfrost.loom") version "0.10.0.5"
    id("dev.architectury.architectury-pack200") version "0.1.3"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("net.kyori.blossom") version "1.3.1"
}

group = "de.tomjuri"
version = "1.0.0"

repositories {
    mavenCentral()
    maven("https://repo.polyfrost.cc/releases")
    maven("https://repo.spongepowered.org/repository/maven-public")
    maven("https://pkgs.dev.azure.com/djtheredstoner/DevAuth/_packaging/public/maven/v1")
}

val embed: Configuration by configurations.creating
configurations.implementation.get().extendsFrom(embed)

dependencies {
    minecraft("com.mojang:minecraft:1.8.9")
    mappings("de.oceanlabs.mcp:mcp_stable:22-1.8.9")
    forge("net.minecraftforge:forge:1.8.9-11.15.1.2318-1.8.9")

    compileOnly("cc.polyfrost:oneconfig-1.8.9-forge:0.2.0-alpha+")
    embed("cc.polyfrost:oneconfig-wrapper-launchwrapper:1.0.0-beta+")

    compileOnly("org.spongepowered:mixin:0.8.5-SNAPSHOT")
    annotationProcessor("org.spongepowered:mixin:0.8.5-SNAPSHOT:processor")

    modRuntimeOnly("me.djtheredstoner:DevAuth-forge-legacy:1.1.2")

    runtimeOnly("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.2")
    embed("org.jetbrains.kotlin:kotlin-stdlib:1.9.0")
    embed("org.json:json:20230227")
    embed("com.squareup.okhttp3:okhttp:3.14.9")
    embed("org.bouncycastle:bcprov-jdk15on:1.70")
}

blossom {
    replaceToken("%%VERSION%%", version)
}

loom {
    runConfigs {
        named("client") {
            ideConfigGenerated(true)
        }
    }

    launchConfigs {
        getByName("client") {
            arg("--tweakClass", "cc.polyfrost.oneconfig.loader.stage0.LaunchWrapperTweaker")
            property("devauth.enabled", "true")
        }
    }

    forge {
        pack200Provider.set(dev.architectury.pack200.java.Pack200Adapter())
        mixinConfig("mixins.armageddon.json")
    }
}

tasks {
    jar {
        manifest.attributes(
                mapOf(
                        "ModSide" to "CLIENT",
                        "TweakOrder" to "0",
                        "ForceLoadAsMod" to true,
                        "TweakClass" to "cc.polyfrost.oneconfig.loader.stage0.LaunchWrapperTweaker",
                        "MixinConfigs" to "mixins.armageddon.json"
                )
        )
        dependsOn(shadowJar)
    }

    remapJar {
        input.set(shadowJar.get().archiveFile)
        archiveClassifier.set("")
    }

    shadowJar {
        relocate("kotlin", "de.tomjuri.armageddon.relocate.kotlin")
        relocate("org.json", "de.tomjuri.armageddon.relocate.org.json")
        relocate("okhttp3", "de.tomjuri.armageddon.relocate.okhttp3")
        relocate("okio", "de.tomjuri.armageddon.relocate.okio")
        relocate("org.bouncycastle", "de.tomjuri.armageddon.relocate.org.bouncycastle")
        configurations = listOf(embed)
    }

    processResources {
        inputs.property("version", version)
        filesMatching(listOf("mcmod.info")) {
            expand(mapOf("version" to version))
        }
    }

    withType<JavaCompile> {
        targetCompatibility = "1.8"
        sourceCompatibility = "1.8"
    }

    withType<KotlinCompile> {
        compilerOptions.jvmTarget.set(JvmTarget.JVM_1_8)
    }
}