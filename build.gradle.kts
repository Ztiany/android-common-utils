plugins {
    alias(libs.plugins.app.common.library)
}

android {
    namespace = "com.android.base.utils"

    sourceSets {
        getByName("main") {
            java.srcDir("src/github/java")
            res.srcDir("src/github/res")
        }
    }
}

dependencies {
    // androidx
    compileOnly(libs.androidx.annotations)
    compileOnly(libs.androidx.activity.ktx)
    compileOnly(libs.androidx.fragment.ktx)
    compileOnly(libs.androidx.appcompat)
    compileOnly(libs.google.ui.material)
    compileOnly(libs.androidx.viewpager2)

    // kotlin
    compileOnly(libs.kotlin.stdlib)
    compileOnly(libs.kotlin.reflect)
    compileOnly(libs.kotlinx.coroutines)
    compileOnly(libs.kotlinx.coroutines.android)

    // utils
    implementation(libs.square.okio)
    implementation(libs.jakewharton.timber)
    implementation(libs.google.gson)
}