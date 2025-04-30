plugins {
    id("java")
    id("io.freefair.lombok") version "8.13.1"
}

group = "io.github.imecuadorian.vitalmed"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    flatDir {
        dirs("libs")
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

val jetbrainsAnnotationsVersion = "26.0.2"
val junitVersion = "5.12.1"
val junitPlatformVersion = "1.12.1"

dependencies {
    implementation(files("libs/generic-library-1.0.0.jar"))
    implementation("org.jetbrains:annotations:$jetbrainsAnnotationsVersion")

    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:$junitPlatformVersion")
}

tasks.test {
    useJUnitPlatform()
}