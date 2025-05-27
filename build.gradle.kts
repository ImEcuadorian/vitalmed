import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.api.tasks.testing.logging.TestExceptionFormat

plugins {
    application
    id("java")
    id("io.freefair.lombok") version "8.13.1"
    id("com.gradleup.shadow") version "9.0.0-beta13"
    id("org.gradle.test-retry") version "1.6.2"
    id("checkstyle")
    jacoco
}

group = "io.github.imecuadorian.vitalmed"
version = "1.0-SNAPSHOT"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

repositories {
    mavenCentral()
    flatDir { dirs("libs") }
}

object V {
    const val jetbrainsAnnotations = "26.0.2"
    const val junitPlatform = "1.12.1"
    const val jasperReports = "7.0.3"
    const val commonCollections = "4.5.0"
    const val openpdf = "2.0.4"
    const val logback = "1.5.18"
    const val modalDialog = "2.5.0"
    const val migLayout = "11.4.2"
    const val flatLaf = "3.6"
    const val mysqlConnector = "9.3.0"
    const val bcrypt = "0.10.2"
    const val dotenv = "3.2.0"
    const val hikari = "6.3.0"
    const val cucumber = "7.22.2"
    const val mockito = "5.18.0"
    const val junitJupiterBom = "5.12.2"
    const val bouncycastle = "1.80"
    const val logStashLogbackEncoder = "8.1"
}

dependencies {
    implementation("io.github.dj-raven:modal-dialog:${V.modalDialog}")
    implementation("com.miglayout:miglayout-swing:${V.migLayout}")
    implementation("com.formdev:flatlaf:${V.flatLaf}")
    implementation("com.formdev:flatlaf-extras:${V.flatLaf}")

    implementation(files("libs/generic-library-1.0.0.jar"))

    implementation("org.jetbrains:annotations:${V.jetbrainsAnnotations}")
    implementation("com.mysql:mysql-connector-j:${V.mysqlConnector}")
    implementation("com.zaxxer:HikariCP:${V.hikari}")
    implementation("io.github.cdimascio:dotenv-java:${V.dotenv}")
    implementation("at.favre.lib:bcrypt:${V.bcrypt}")
    implementation("org.bouncycastle:bcprov-jdk15to18:${V.bouncycastle}")

    implementation("ch.qos.logback:logback-classic:${V.logback}")
    implementation("net.logstash.logback:logstash-logback-encoder:${V.logStashLogbackEncoder}")

    implementation("net.sf.jasperreports:jasperreports:${V.jasperReports}")
    implementation("net.sf.jasperreports:jasperreports-fonts:${V.jasperReports}")
    implementation("net.sf.jasperreports:jasperreports-functions:${V.jasperReports}")
    implementation("net.sf.jasperreports:jasperreports-metadata:${V.jasperReports}")
    implementation("net.sf.jasperreports:jasperreports-javaflow:${V.jasperReports}")
    implementation("net.sf.jasperreports:jasperreports-pdf:${V.jasperReports}")
    implementation("org.apache.commons:commons-collections4:${V.commonCollections}")
    implementation("com.github.librepdf:openpdf:${V.openpdf}")

    implementation("org.mockito:mockito-core:${V.mockito}")

    testImplementation(platform("org.junit:junit-bom:${V.junitJupiterBom}"))
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("org.junit.jupiter:junit-jupiter-params")
    testImplementation("io.cucumber:cucumber-java:${V.cucumber}")
    testImplementation("io.cucumber:cucumber-junit-platform-engine:${V.cucumber}")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:${V.junitPlatform}")
}

sourceSets {
    main {
        resources {
            srcDir("src/main/resources")
            exclude(".env")
        }
    }
}

application {
    mainClass.set("io.github.imecuadorian.vitalmed.Vitalmed")
}

tasks {

    withType<JavaCompile> { options.encoding = "UTF-8" }

    test {
        useJUnitPlatform()
        systemProperty("cucumber.junit-platform.naming-strategy", "long")
        retry {
            maxRetries.set(2)
            maxFailures.set(20)
            failOnPassedAfterRetry.set(true)
            failOnSkippedAfterRetry.set(true)
        }
        testLogging {
            exceptionFormat = TestExceptionFormat.FULL
        }
    }

    named<ShadowJar>("shadowJar") {
        archiveBaseName.set("vitalmed")
        archiveClassifier.set("")
        archiveVersion.set("")
        manifest {
            attributes["Main-Class"] = "io.github.imecuadorian.vitalmed.Vitalmed"
        }
        from("src/main/resources") { include("logback.xml") }
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        mergeServiceFiles()
        minimize()
    }

    register<Exec>("packageInstaller") {
        group = "distribution"
        description = "Empaqueta Vitalmed como instalador .exe (jpackage)"
        dependsOn("shadowJar")

        val outputDir = layout.buildDirectory.dir("installer")
        val inputDir = layout.buildDirectory.dir("libs")
        val iconPath = "$projectDir/src/main/resources/icon.ico"

        doFirst { mkdir(outputDir.get().asFile) }

        commandLine(
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
            "--app-version", project.version.toString()
        )
    }

    jacocoTestReport {
        dependsOn(test)
        reports {
            xml.required.set(true)      // Codecov / Sonar
            csv.required.set(false)
            html.required.set(true)     // Navegable local
            html.outputLocation.set(layout.buildDirectory.dir("jacocoHtml"))
        }
        finalizedBy("openCoverage")
    }

    register("openCoverage") {
        group = "verification"
        description = "Abre el reporte de cobertura en el navegador"
        doLast {
            val report = layout.buildDirectory.file("jacocoHtml/index.html").get().asFile
            println("Reporte de cobertura generado en: ${report.toURI()}")
        }
    }

    register("ci") {
        group = "verification"
        description = "Clean, test, and generate coverage report (ideal para CI)"
        dependsOn("clean", "test", "jacocoTestReport")
    }

    processResources { duplicatesStrategy = DuplicatesStrategy.EXCLUDE }
}

jacoco { toolVersion = "0.8.13" }

checkstyle {
    toolVersion = "10.24.0" // o la versión más reciente
    configFile = file("config/checkstyle/checkstyle.xml")
    isIgnoreFailures = false
    isShowViolations = true
}
