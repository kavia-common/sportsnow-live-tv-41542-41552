androidLibrary {
    namespace = "org.gradle.experimental.android.utilities"

    dependencies {
        // repositories block is not allowed in module DCL; repositories are defined in settings.gradle.dcl
        api(project(":list"))
    }
}
