pluginManagement {
    repositories {
        // Ensure Google repository comes first for AndroidX artifacts
        google()
        gradlePluginPortal()
        mavenCentral()
    }
    // In Declarative Gradle, repository resolution for dependencies is driven from pluginManagement in settings.
    // Ensure both google() and mavenCentral() are available globally.
}

plugins {
    id("org.gradle.experimental.android-ecosystem").version("0.1.43")
}

rootProject.name = "example-android-app"

include("app")
include("list")
include("utilities")

defaults {
    androidApplication {
        jdkVersion = 17
        compileSdk = 34
        minSdk = 30

        // Set app identity for TV app
        applicationId = "org.sportsnow.tv"
        versionCode = 1
        versionName = "0.1"

        testing {
            dependencies {
                implementation("org.junit.jupiter:junit-jupiter:5.10.2")
                runtimeOnly("org.junit.platform:junit-platform-launcher")
            }
        }
    }

    androidLibrary {
        jdkVersion = 17
        compileSdk = 34
        minSdk = 30

        testing {
            dependencies {
                implementation("org.junit.jupiter:junit-jupiter:5.10.2")
                runtimeOnly("org.junit.platform:junit-platform-launcher")
            }
        }
    }
}
