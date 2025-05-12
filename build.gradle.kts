import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    application
    id("java")
    id("io.freefair.lombok") version "8.13.1"
    id("com.gradleup.shadow") version "9.0.0-beta13"
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

val modalDialogVersion = "2.5.0"
val migLayoutVersion = "11.4.2"
val flatLafVersion = "3.6"

val mysqlConnectorVersion = "9.3.0"

dependencies {

    implementation("io.github.dj-raven:modal-dialog:$modalDialogVersion")
    implementation("com.miglayout:miglayout-swing:$migLayoutVersion")
    implementation("com.formdev:flatlaf:$flatLafVersion")
    implementation("com.formdev:flatlaf-extras:$flatLafVersion")
    implementation(files("libs/generic-library-1.0.0.jar"))
    implementation("org.jetbrains:annotations:$jetbrainsAnnotationsVersion")
    implementation("com.mysql:mysql-connector-j:$mysqlConnectorVersion")

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

tasks.named<ShadowJar>("shadowJar") {
    archiveBaseName.set("vitalmed")
    archiveClassifier.set("")
    archiveVersion.set("")
    manifest {
        attributes(mapOf("Main-Class" to "io.github.imecuadorian.vitalmed.Vitalmed"))
    }
}

application {
    mainClass.set("io.github.imecuadorian.vitalmed.Vitalmed")
}

tasks.register<Exec>("packageInstaller") {
    group = "distribution"
    description = "Empaqueta la aplicación Vitalmed como un instalador .exe usando jpackage"

    dependsOn("shadowJar")

    val outputDir = layout.buildDirectory.dir("installer")
    val inputDir = layout.buildDirectory.dir("libs")
    val iconPath = "$projectDir/src/main/resources/icon.ico"

    doFirst {
        mkdir(outputDir.get().asFile)
    }

    commandLine = listOf(
        "jpackage",
        "--type", "msi",
        "--name", "Vitalmed",
        "--input", inputDir.get().asFile.absolutePath,
        "--main-jar", "vitalmed.jar",
        "--main-class", "io.github.imecuadorian.vitalmed.Vitalmed",
        "--dest", outputDir.get().asFile.absolutePath,
        "--icon", iconPath,
        "--win-console",
        "--vendor", "ImEcuadorian",
        "--app-version", "1.0"
    )
}
