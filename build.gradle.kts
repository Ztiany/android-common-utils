plugins {
    alias(libs.plugins.app.common.library)
    alias(libs.plugins.vanniktech.maven.publisher)
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
    compileOnly(libs.square.okio)
    compileOnly(libs.google.gson)
    // log
    implementation(libs.jakewharton.timber)
}