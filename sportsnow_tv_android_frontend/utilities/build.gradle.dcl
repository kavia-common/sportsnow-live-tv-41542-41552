androidLibrary {
    namespace = "org.gradle.experimental.android.utilities"

    dependencies {
        repositories {
            google()
            mavenCentral()
        }
        api(project(":list"))
    }
}
