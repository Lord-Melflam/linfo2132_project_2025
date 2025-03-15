/*
 * This file was generated by the Gradle 'init' task.
 *
 * This generated file contains a sample Java application project to get you started.
 * For more details take a look at the 'Building Java & JVM projects' chapter in the Gradle
 * User Manual available at https://docs.gradle.org/7.6/userguide/building_java_projects.html
 */

plugins {
    // Apply the application plugin to add support for building a CLI application in Java.
    application
    java
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

sourceSets.main.get().java.srcDir("src")
sourceSets.test.get().java.srcDir("test")

dependencies {
    // Use JUnit test framework.
    testImplementation("junit:junit:4.13.2")
    implementation("junit:junit:4.13.2")

    // This dependency is used by the application.
    implementation("com.google.guava:guava:31.1-jre")
    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.0")
    implementation("com.fasterxml.jackson.core:jackson-core:2.15.0")
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.15.0")
}
application {
    // Define the main class for the application.
    mainClass.set("compiler.Compiler")
}
tasks.test {
    exclude("**/TestFile")
    exclude("**/Utils")
}

tasks.register<Zip>("packageSource") {
    dependsOn("test")
    archiveBaseName.set("${project.name}-source")
    destinationDirectory.set(file("${layout.buildDirectory.get()}/distributions"))
    from(projectDir) {
        into(projectDir.name)

        include("src/**", "test/**", "build.gradle.kts")
    }
}