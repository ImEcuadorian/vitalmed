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
version = project.property("projectVersion") as String

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

repositories {
    mavenCentral()
    flatDir { dirs("libs") }
}

// --- Versión de dependencias desde gradle.properties ---
val jetbrainsAnnotations: String by project
val junitPlatform: String by project
val jasperReports: String by project
val commonCollections: String by project
val openpdf: String by project
val logback: String by project
val modalDialog: String by project
val migLayout: String by project
val flatLaf: String by project
val mysqlConnector: String by project
val bcrypt: String by project
val dotenv: String by project
val hikari: String by project
val cucumber: String by project
val mockito: String by project
val junitJupiterBom: String by project
val bouncycastle: String by project
val logStashLogbackEncoder: String by project

dependencies {
    implementation("io.github.dj-raven:modal-dialog:$modalDialog")
    implementation("com.miglayout:miglayout-swing:$migLayout")
    implementation("com.formdev:flatlaf:$flatLaf")
    implementation("com.formdev:flatlaf-extras:$flatLaf")

    implementation(files("libs/generic-library-1.0.0.jar"))

    implementation("org.jetbrains:annotations:$jetbrainsAnnotations")
    implementation("com.mysql:mysql-connector-j:$mysqlConnector")
    implementation("com.zaxxer:HikariCP:$hikari")
    implementation("io.github.cdimascio:dotenv-java:$dotenv")
    implementation("at.favre.lib:bcrypt:$bcrypt")
    implementation("org.bouncycastle:bcprov-jdk15to18:$bouncycastle")

    implementation("ch.qos.logback:logback-classic:$logback")
    implementation("net.logstash.logback:logstash-logback-encoder:$logStashLogbackEncoder")

    implementation("net.sf.jasperreports:jasperreports:$jasperReports")
    implementation("net.sf.jasperreports:jasperreports-fonts:$jasperReports")
    implementation("net.sf.jasperreports:jasperreports-functions:$jasperReports")
    implementation("net.sf.jasperreports:jasperreports-metadata:$jasperReports")
    implementation("net.sf.jasperreports:jasperreports-javaflow:$jasperReports")
    implementation("net.sf.jasperreports:jasperreports-pdf:$jasperReports")
    implementation("org.apache.commons:commons-collections4:$commonCollections")
    implementation("com.github.librepdf:openpdf:$openpdf")

    implementation("org.mockito:mockito-core:$mockito")

    testImplementation(platform("org.junit:junit-bom:$junitJupiterBom"))
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("org.junit.jupiter:junit-jupiter-params")
    testImplementation("io.cucumber:cucumber-java:$cucumber")
    testImplementation("io.cucumber:cucumber-junit-platform-engine:$cucumber")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:$junitPlatform")
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
            xml.required.set(true)
            csv.required.set(false)
            html.required.set(true)
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

    processResources {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }
}

jacoco { toolVersion = "0.8.13" }

checkstyle {
    toolVersion = "10.24.0"
    configFile = file("config/checkstyle/checkstyle.xml")
    isIgnoreFailures = false
    isShowViolations = true
}
