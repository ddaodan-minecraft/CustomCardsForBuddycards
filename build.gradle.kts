import org.gradle.api.Action
import org.gradle.jvm.tasks.Jar
import net.neoforged.moddevgradle.legacyforge.dsl.LegacyForgeExtension
import net.neoforged.moddevgradle.legacyforge.dsl.LegacyForgeModdingSettings

plugins {
    java
    id("net.neoforged.moddev.legacyforge") version "2.0.107"
}

val modId = property("mod_id") as String
val modVersion = property("mod_version") as String
val archivesBaseName = property("archives_base_name") as String
val minecraftVersion = property("minecraft_version") as String
val forgeVersion = property("forge_version") as String
val parchmentMinecraft = property("parchment_minecraft") as String
val parchmentVersion = property("parchment_version") as String

group = property("maven_group") as String
version = modVersion

base {
    archivesName.set("$archivesBaseName-$minecraftVersion")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

repositories {
    maven("https://mirrors.cloud.tencent.com/nexus/repository/maven-public/")
    maven("https://maven.minecraftforge.net/")
    mavenCentral()
    maven("https://maven.architectury.dev/")
    maven("https://maven.latvian.dev/releases")
    maven("https://cursemaven.com") {
        content {
            includeGroup("curse.maven")
        }
    }
    flatDir {
        dir("libs")
    }
}

dependencies {
    // KubeJS 6 (Forge 1.20.1) 作为 compileOnly，允许在没有 KubeJS 时平滑运行
    compileOnly("dev.latvian.mods:kubejs-forge:2001.6.5-build.7") {
        isTransitive = false
    }
    compileOnly("dev.latvian.mods:rhino-forge:2001.2.2-build.18") {
        isTransitive = false
    }
    compileOnly("dev.architectury:architectury-forge:9.2.14") {
        isTransitive = false
    }

    // 支持将自定义或反编译的 buddycards jar / kubejs jar 放入 libs 目录
    compileOnly(fileTree("libs") { include("*.jar") })
}

val legacyForge = extensions.getByType<LegacyForgeExtension>()

legacyForge.enable(object : Action<LegacyForgeModdingSettings> {
    override fun execute(settings: LegacyForgeModdingSettings) {
        settings.setForgeVersion("$minecraftVersion-$forgeVersion")
    }
})

legacyForge.parchment {
    minecraftVersion = parchmentMinecraft
    mappingsVersion = parchmentVersion
}

legacyForge.runs {
    create("client") {
        client()
    }
    create("server") {
        server()
    }
}

legacyForge.mods {
    create(modId) {
        sourceSet(sourceSets.main.get())
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
}

tasks.named<Jar>("jar").configure {
    manifest {
        attributes(
            mapOf(
                "Specification-Title" to "Buddycards Expansion",
                "Specification-Vendor" to "ddaodan",
                "Specification-Version" to "1",
                "Implementation-Title" to project.name,
                "Implementation-Version" to project.version,
                "Implementation-Vendor" to "ddaodan"
            )
        )
    }
}
