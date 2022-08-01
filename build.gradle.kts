plugins {
    kotlin("js") version "1.7.10"
    kotlin("plugin.serialization") version "1.7.10"
}

val reactVersion = "18.2.0-pre.361"
val serializationVersion = "1.3.2"
val ktorVersion = "1.6.7"
val datetimeVersion = "0.4.0"
val emotionVersion = "11.9.3-pre.361"

group = "fr.lewon.rosa"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$serializationVersion")
    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-json:$ktorVersion")
    implementation("io.ktor:ktor-client-serialization:$ktorVersion")
    implementation("io.ktor:ktor-client-js:$ktorVersion")
    implementation("org.jetbrains.kotlin-wrappers:kotlin-react:$reactVersion")
    implementation("org.jetbrains.kotlin-wrappers:kotlin-react-dom:$reactVersion")
    implementation("org.jetbrains.kotlin-wrappers:kotlin-emotion:$emotionVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:${datetimeVersion}")
}

kotlin {
    js(LEGACY) {
        binaries.executable()
        browser {
            commonWebpackConfig {
                cssSupport.enabled = true
            }
            binaries.executable()
        }
    }
}

tasks.withType(org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpack::class.java).forEach { t ->
    t.inputs.files(fileTree("src/main/resources"))
}