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
val jasperReportsVersion = "7.0.3"
val commonCollectionsVersion = "4.5.0"
val itextPdfVersion = "5.5.13.4"
val slf4jVersion = "2.0.17"

dependencies {
    implementation(files("libs/generic-library-1.0.0.jar"))
    implementation("org.jetbrains:annotations:$jetbrainsAnnotationsVersion")

    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:$junitPlatformVersion")

    implementation("net.sf.jasperreports:jasperreports:$jasperReportsVersion")
    implementation("net.sf.jasperreports:jasperreports-fonts:$jasperReportsVersion")
    implementation("net.sf.jasperreports:jasperreports-functions:$jasperReportsVersion")
    implementation("net.sf.jasperreports:jasperreports-metadata:$jasperReportsVersion")
    implementation("net.sf.jasperreports:jasperreports-javaflow:$jasperReportsVersion")
    implementation("net.sf.jasperreports:jasperreports-pdf:$jasperReportsVersion")
    implementation("org.apache.commons:commons-collections4:$commonCollectionsVersion")
    implementation("com.itextpdf:itextpdf:$itextPdfVersion")
    implementation("org.slf4j:slf4j-simple:$slf4jVersion")
}

sourceSets {
    main {
        resources {
            srcDir("src/main/resources")
        }
    }
}

tasks.test {
    useJUnitPlatform()
}

tasks.processResources {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}