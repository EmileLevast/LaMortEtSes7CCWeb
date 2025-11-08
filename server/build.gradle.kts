import org.gradle.wrapper.Install

val kmongoVersion = "5.1.0"

plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ktor)
    application
    alias(libs.plugins.kotlinSerialization)
}

group = "org.levast.project"
version = "1.0.0"
application {
    mainClass.set("org.levast.project.ApplicationKt")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=${extra["io.ktor.development"] ?: "false"}")


}

dependencies {
    implementation(projects.shared)
    implementation(libs.logback)
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    implementation(project(":composeApp"))
    testImplementation(libs.kotlin.test.junit)
    implementation("org.litote.kmongo:kmongo-coroutine-serialization:$kmongoVersion")


    implementation(libs.ktor.serialization)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.server.cors)
    implementation(libs.ktor.server.compression)
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.ktor.server.netty)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.server.call.logging)
    implementation(libs.kmongo.coroutine)

    implementation(libs.ktor.network.tls.certificates)
    implementation(awssdk.services.secretsmanager)
    implementation(libs.regions)

    implementation(libs.ktor.server.auth)
}

tasks.withType<Zip>() {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks.withType<Tar>() {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks.installDist {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}
