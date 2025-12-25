# Mosberg API

## build.gradle - Gradle build script for Mosberg API mod using Fabric Loom

```gradle
plugins {
    id 'fabric-loom' version "${loom_version}"
    id 'maven-publish'
    id 'java'
}

version = project.mod_version
group = project.maven_group

base {
    archivesName = project.archives_base_name
}

repositories {
    mavenCentral()
    maven {
        name = "Terraformers"
        url = "https://maven.terraformersmc.com/releases/"
    }
    maven {
        name = "Shedaniel"
        url = "https://maven.shedaniel.me/"
    }
}

loom {
    splitEnvironmentSourceSets()

    mods {
        mosbergapi {
            sourceSet sourceSets.main
            sourceSet sourceSets.client
        }
    }

    runs {
        // Enhanced client run configuration
        client {
            client()
            configName = "Minecraft Client"
            ideConfigGenerated = true
            runDir = "run"
        }

        // Enhanced server run configuration
        server {
            server()
            configName = "Minecraft Server"
            ideConfigGenerated = true
            runDir = "run-server"
        }
    }
}

// Configure data generation - this automatically creates the 'datagen' run config
fabricApi {
    configureDataGeneration {
        client = true
    }
}

dependencies {
    // Minecraft & Fabric
    minecraft "com.mojang:minecraft:${project.minecraft_version}"
    mappings "net.fabricmc:yarn:${project.yarn_mappings}:v2"
    modImplementation "net.fabricmc:fabric-loader:${project.loader_version}"
    modImplementation "net.fabricmc.fabric-api:fabric-api:${project.fabric_version}"

    // Optional Integrations
    modImplementation "com.terraformersmc:modmenu:${project.modmenu_version}"
    modApi("me.shedaniel.cloth:cloth-config-fabric:${project.cloth_config_version}") {
        exclude group: "net.fabricmc.fabric-api"
    }

    // Libraries (bundled with mod)
    include implementation("com.google.code.gson:gson:${project.gson_version}")

    // Compile-only annotations
    compileOnly "org.jetbrains:annotations:${project.annotations_version}"

    // Testing (JUnit 6)
    testImplementation "org.junit.jupiter:junit-jupiter-api:${project.junit_version}"
    testRuntimeOnly "org.junit.jupiter:junit-jupiter-engine:${project.junit_version}"
    testRuntimeOnly "org.junit.platform:junit-platform-launcher:${project.junit_version}"
}

sourceSets {
    main {
        resources {
            srcDirs += [
                "src/main/generated/resources"
            ]
            exclude ".cache"
        }
    }
}

processResources {
    inputs.property "version", project.version
    inputs.property "mod_id", project.mod_id
    inputs.property "mod_name", project.mod_name
    inputs.property "mod_description", project.mod_description
    inputs.property "mod_author", project.mod_author
    inputs.property "mod_license", project.mod_license
    inputs.property "mod_version", project.mod_version
    inputs.property "mod_homepage", project.mod_homepage
    inputs.property "mod_sources", project.mod_sources
    inputs.property "mod_issues", project.mod_issues
    inputs.property "loader_version", project.loader_version
    inputs.property "minecraft_version", project.minecraft_version

    filesMatching("fabric.mod.json") {
        expand([
            "version": project.version,
            "mod_id": project.mod_id,
            "mod_name": project.mod_name,
            "mod_description": project.mod_description,
            "mod_author": project.mod_author,
            "mod_license": project.mod_license,
            "mod_version": project.mod_version,
            "mod_homepage": project.mod_homepage,
            "mod_sources": project.mod_sources,
            "mod_issues": project.mod_issues,
            "loader_version": project.loader_version,
            "minecraft_version": project.minecraft_version
        ])
    }
}

tasks.withType(JavaCompile).configureEach {
    it.options.encoding = "UTF-8"
    it.options.release = 21
}

java {
    withSourcesJar()
    withJavadocJar()

    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

jar {
    from("LICENSE") {
        rename { "${it}_${project.archives_base_name}" }
    }

    manifest {
        attributes([
            "Specification-Title": project.mod_name,
            "Specification-Vendor": project.mod_author,
            "Specification-Version": "1",
            "Implementation-Title": project.mod_name,
            "Implementation-Version": project.version,
            "Implementation-Vendor": project.mod_author,
            "Implementation-Timestamp": new Date().format("yyyy-MM-dd'T'HH:mm:ssZ")
        ])
    }
}

javadoc {
    options.encoding = 'UTF-8'
    options.charSet = 'UTF-8'
    options.addStringOption('Xdoclint:none', '-quiet')
}

test {
    useJUnitPlatform()
    testLogging {
        events "passed", "skipped", "failed"
        exceptionFormat = "full"  // ✅ Gradle 9+ compatible
        showStandardStreams = false
    }
}

// Publishing configuration
publishing {
    publications {
        mavenJava(MavenPublication) {
            artifactId = project.archives_base_name
            from components.java

            pom {
                name = project.mod_name
                description = project.mod_description
                url = project.mod_homepage

                licenses {
                    license {
                        name = project.mod_license
                    }
                }

                developers {
                    developer {
                        name = project.mod_author
                    }
                }

                scm {
                    url = project.mod_sources
                    connection = "scm:git:${project.mod_sources}.git"
                }
            }
        }
    }

    repositories {
        // Example: Local maven repository
        // mavenLocal()

        // Example: Custom maven repository
        // maven {
        //     name = "MyMaven"
        //     url = "https://maven.example.com/releases"
        //     credentials {
        //         username = project.findProperty("maven.username") ?: System.getenv("MAVEN_USERNAME")
        //         password = project.findProperty("maven.password") ?: System.getenv("MAVEN_PASSWORD")
        //     }
        // }
    }
}

// Clean generated resources
clean {
    delete "src/main/generated"
}

// Task to display project info
task projectInfo {
    doLast {
        println "======================================"
        println "Mosberg API Build Information"
        println "======================================"
        println "Mod Name:       ${project.mod_name}"
        println "Version:        ${project.version}"
        println "Minecraft:      ${project.minecraft_version}"
        println "Fabric Loader:  ${project.loader_version}"
        println "Fabric API:     ${project.fabric_version}"
        println "Java Version:   ${JavaVersion.current()}"
        println "Gradle Version: ${gradle.gradleVersion}"
        println "======================================"
    }
}
```

---

## gradle.properties - Gradle project properties for Mosberg API mod

```properties
# Gradle Configuration
org.gradle.jvmargs=-Xmx4G -XX:+UseG1GC -XX:+ParallelRefProcEnabled -XX:MaxGCPauseMillis=200
org.gradle.parallel=true
org.gradle.caching=true
org.gradle.configuration-cache=false

# Mod Properties
mod_version=1.0.0
maven_group=dk.mosberg.api
archives_base_name=mosbergapi
mod_id=mosbergapi
mod_name=Mosberg API
mod_description=Comprehensive library mod providing utilities, helpers, and systems for Fabric mod development
mod_author=Mosberg
mod_license=MIT
mod_homepage=https://github.com/mosberg/mosbergapi
mod_sources=https://github.com/mosberg/mosbergapi
mod_issues=https://github.com/mosberg/mosbergapi/issues

# Fabric Properties
# Check versions at https://fabricmc.net/develop
minecraft_version=1.21.10
yarn_mappings=1.21.10+build.3
loader_version=0.18.3
loom_version=1.14.10

# Dependencies
fabric_version=0.138.4+1.21.10

# Libraries
gson_version=2.13.2
slf4j_version=2.0.17
annotations_version=26.0.2

# Testing
junit_version=6.0.1
mockito_version=5.21.0
assertj_version=3.27.6

# Optional Dependencies
cloth_config_version=19.0.147
modmenu_version=16.0.0-rc.1
```

---

## settings.gradle - Gradle settings for Mosberg API mod

````gradle
pluginManagement {
	repositories {
		maven {
			name = 'Fabric'
			url = 'https://maven.fabricmc.net/'
		}
		mavenCentral()
		gradlePluginPortal()
	}
}
```
````
