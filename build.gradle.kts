plugins {
    application
    kotlin("jvm") version "1.6.10"
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
    implementation("org.jgrapht:jgrapht-core:1.5.1")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.6.10")
    implementation("io.arrow-kt:arrow-core:1.0.1")

    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.7.1")
    testImplementation("org.assertj:assertj-core:3.21.0")
    testImplementation("org.mockito.kotlin:mockito-kotlin:4.0.0")
    testImplementation("com.google.code.gson:gson:2.9.0")
}

apply(plugin = "info.solidsoft.pitest.aggregator")
pitest {
    pitestVersion.set("1.8.0")
    junit5PluginVersion.set("0.16")
    targetClasses.add("com.github.caay2000.ttk.*")
    excludedMethods.addAll("toString")
    outputFormats.addAll("XML", "HTML")
    timestampedReports.set(false)
    exportLineCoverage.set(true)
    avoidCallsTo.add("kotlin.jvm.internal")
    mutators.addAll("STRONGER", "EXTENDED")
    detectInlinedCode.set(true)
    threads.set(4)
    failWhenNoMutations.set(false)
    dependencies {
        pitest("com.groupcdg.pitest:pitest-accelerator-junit5:0.1.0")
        pitest("com.groupcdg.pitest:extended-mutators:0.1.2")
        pitest("com.groupcdg.pitest:pitest-kotlin-plugin:0.1.1")
    }
}
