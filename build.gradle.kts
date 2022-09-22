plugins {
    application
    kotlin("jvm") version "1.7.10"
    id("info.solidsoft.pitest") version "1.7.4"
    id("org.jlleitschuh.gradle.ktlint") version "10.2.0"
}

group = "com.github.caay2000"
version = "1.0-SNAPSHOT"

application {
    mainClass.set("com.github.caay2000.ttk.AppKt")
}

repositories {
    mavenCentral()
}

tasks.withType<Wrapper> {
    gradleVersion = "7.5"
}

tasks.test {
    useJUnitPlatform()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.7.10")
    implementation("io.arrow-kt:arrow-core:1.0.1")

    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.2")
    testImplementation("org.assertj:assertj-core:3.21.0")
    testImplementation("org.mockito.kotlin:mockito-kotlin:4.0.0")
    testImplementation("com.google.code.gson:gson:2.9.0")
    testImplementation("io.kotest.extensions:kotest-assertions-arrow:1.0.2")
}

pitest {
    pitestVersion.set("1.9.4")
    junit5PluginVersion.set("1.0.0")
    targetClasses.add("com.github.caay2000.ttk.*")
    excludedMethods.addAll("toString")
    excludedTestClasses.add("**.*IntegrationTest")
    outputFormats.addAll("HTML")
    avoidCallsTo.add("kotlin.jvm.internal")
    mutators.addAll("STRONGER", "EXTENDED")
    detectInlinedCode.set(true)
    timestampedReports.set(false)
    threads.set(4)
    failWhenNoMutations.set(false)
    dependencies {
        pitest("com.groupcdg.pitest:pitest-accelerator-junit5:1.0.0")
        pitest("com.groupcdg.pitest:extended-mutators:0.1.4")
        pitest("com.groupcdg.pitest:pitest-kotlin-plugin:0.1.3")
    }
}
