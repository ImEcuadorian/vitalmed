import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    application
    id("java")
    id("io.freefair.lombok") version "8.13.1"
    id("com.gradleup.shadow") version "9.0.0-beta13"
    id("org.gradle.test-retry") version "1.6.2"
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
val openpdfVersion = "2.0.4"
val logbackVersion = "1.5.18"

val modalDialogVersion = "2.5.0"
val migLayoutVersion = "11.4.2"
val flatLafVersion = "3.6"
val mysqlConnectorVersion = "9.3.0"

val bcryptVersion = "0.10.2"
val dotenvVersion = "3.2.0"
val hikariVersion = "6.3.0"
val cucumberVersion = "7.22.2"
val cucumberJunitPlatformVersion = "7.22.2"
val junitPlatformSuiteVersion = "1.13.0-RC1"
val mockitoVersion = "5.18.0"
val junitJupiterVersion = "5.12.2"
val bouncycastleVersion = "1.80"

val logStashLogbackEncoderVersion = "8.1"


dependencies {

    implementation("io.github.dj-raven:modal-dialog:$modalDialogVersion")
    implementation("com.miglayout:miglayout-swing:$migLayoutVersion")
    implementation("com.formdev:flatlaf:$flatLafVersion")
    implementation("com.formdev:flatlaf-extras:$flatLafVersion")
    implementation(files("libs/generic-library-1.0.0.jar"))
    implementation("org.jetbrains:annotations:$jetbrainsAnnotationsVersion")
    implementation("com.mysql:mysql-connector-j:$mysqlConnectorVersion")
    implementation("net.sf.jasperreports:jasperreports:${jasperReportsVersion}")
    implementation("net.sf.jasperreports:jasperreports-fonts:${jasperReportsVersion}")
    implementation("net.sf.jasperreports:jasperreports-functions:${jasperReportsVersion}")
    implementation("net.sf.jasperreports:jasperreports-metadata:${jasperReportsVersion}")
    implementation("net.sf.jasperreports:jasperreports-javaflow:${jasperReportsVersion}")
    implementation("net.sf.jasperreports:jasperreports-pdf:${jasperReportsVersion}")
    implementation("org.apache.commons:commons-collections4:${commonCollectionsVersion}")
    implementation("com.github.librepdf:openpdf:${openpdfVersion}")
    implementation("ch.qos.logback:logback-classic:${logbackVersion}")
    implementation("org.mockito:mockito-core:${mockitoVersion}")
    implementation("org.junit.platform:junit-platform-suite:${junitPlatformSuiteVersion}")
    implementation("com.zaxxer:HikariCP:${hikariVersion}")
    implementation("io.github.cdimascio:dotenv-java:${dotenvVersion}")
    implementation("at.favre.lib:bcrypt:${bcryptVersion}")
    implementation("org.bouncycastle:bcprov-jdk15to18:${bouncycastleVersion}")
    implementation("net.logstash.logback:logstash-logback-encoder:${logStashLogbackEncoderVersion}")

    testImplementation(platform("org.junit:junit-bom:$junitJupiterVersion"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testImplementation("io.cucumber:cucumber-java:$cucumberVersion")
    testImplementation("io.cucumber:cucumber-junit-platform-engine:$cucumberJunitPlatformVersion")
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.junit.jupiter:junit-jupiter-params")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher:$junitPlatformVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
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
    from("src/main/resources") {
        include("logback.xml")
    }
    mergeServiceFiles()
    minimize()
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
        "--type", "exe",
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

tasks.test {
    useJUnitPlatform()
    systemProperty("cucumber.junit-platform.naming-strategy", "long")

    retry {
        maxRetries.set(2)
        maxFailures.set(20)
        failOnPassedAfterRetry.set(true)
        failOnSkippedAfterRetry.set(true)
    }
}
