plugins {
    application
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}

application {
    mainClass.set("dev.worldmesh.regionworker.Main")
}

dependencies {
    implementation(project(":packages:region-model"))
    implementation(project(":packages:transfer-model"))

    implementation("net.minestom:minestom:2026.03.03-1.21.11")
    runtimeOnly("ch.qos.logback:logback-classic:1.5.32")
}