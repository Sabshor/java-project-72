plugins {
    application
    checkstyle
    jacoco
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("io.freefair.lombok") version "8.6"
}

group = "hexlet.code"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

application {
    mainClass.set("hexlet.code.App")
}

dependencies {
    //compileOnly("org.projectlombok:lombok:1.18.32")
    //annotationProcessor("org.projectlombok:lombok:1.18.32")

    implementation("com.h2database:h2:2.2.220")
    implementation("org.postgresql:postgresql:42.7.3")
    implementation("com.zaxxer:HikariCP:5.0.1")
    implementation("io.javalin:javalin:6.1.3")
    implementation("org.slf4j:slf4j-simple:2.0.7")
    implementation("io.javalin:javalin-bundle:6.1.3")
    // Шаблонизатор и его интеграция с Javalin
    implementation("io.javalin:javalin-rendering:6.1.3")
    // Шаблонизатор Java Template Engine
    implementation("gg.jte:jte:3.1.9")
    implementation("com.konghq:unirest-java:3.13.0")

    testImplementation("org.assertj:assertj-core:3.25.3")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.2")
    testImplementation("com.squareup.okhttp3:mockwebserver:4.12.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.2")
}

tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport) // report is always generated after tests run
}

tasks.jacocoTestReport {
    dependsOn(tasks.test) // tests are required to run before generating the report
    reports {
        xml.required = true
    }
}