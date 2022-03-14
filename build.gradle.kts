import com.adarshr.gradle.testlogger.theme.ThemeType.MOCHA
import org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
import org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED
import org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED
import org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.10"
    kotlin("plugin.serialization") version "1.6.10"
    id("com.adarshr.test-logger") version "3.2.0"
}

val restAssuredVersion = "4.5.1"
val kotlinxSerializationJsonVersion = "1.3.2"
val properltyVersion = "1.9.0"

group = "uk.co.logiccache"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    testImplementation("io.rest-assured:kotlin-extensions:$restAssuredVersion")
    testImplementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinxSerializationJsonVersion")
    testImplementation("com.ufoscout.properlty:properlty-kotlin:$properltyVersion")
}

testlogger {
    theme = MOCHA
}

tasks.test {
    System.getProperties().forEach { systemProperty(it.key as String, it.value as String) }
    useJUnitPlatform()
    testLogging {
        events(PASSED, SKIPPED, FAILED)
        exceptionFormat = FULL
        showCauses = true
        showExceptions = true
        showStackTraces = true
    }
}

task<Test>("smoke") {
    System.getProperties().forEach { systemProperty(it.key as String, it.value as String) }
    useJUnitPlatform {
        includeTags("smoke")
        excludeTags("!smoke")
    }
    testLogging {
        events(PASSED, SKIPPED, FAILED)
        exceptionFormat = FULL
        showCauses = true
        showExceptions = true
        showStackTraces = true
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}
