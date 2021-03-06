plugins {
    id(BuildPlugin.androidApplication)

    id(BuildPlugin.kotlinAndroid)
    id(BuildPlugin.kotlinAndroidExtensions)
    id(BuildPlugin.kotlinKapt)
}

android {
    compileSdkVersion(AndroidSdk.targetVersion)
    defaultConfig {
        applicationId = "ltd.evilcorp.atox"
        minSdkVersion(AndroidSdk.minVersion)
        targetSdkVersion(AndroidSdk.targetVersion)
        versionCode = 5
        versionName = "0.3.1"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        multiDexEnabled = true
        vectorDrawables.useSupportLibrary = true
    }
    buildTypes {
        getByName("debug") {
            applicationIdSuffix = ".debug"
        }
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"))
        }
    }
    compileOptions {
        sourceCompatibility = Java.version
        targetCompatibility = Java.version
    }
    kotlinOptions {
        jvmTarget = Java.version.toString()
        freeCompilerArgs = listOf(
            "-XXLanguage:+InlineClasses",
            "-Xuse-experimental=kotlinx.coroutines.ObsoleteCoroutinesApi"
        )
    }
    lintOptions {
        disable("GoogleAppIndexingWarning", "MissingTranslation", "InvalidPackage")
    }
    sourceSets["main"].java.srcDir("src/main/kotlin")
    sourceSets["androidTest"].java.srcDir("src/androidTest/kotlin")
    packagingOptions {
        // Work around scala-compiler and scala-library (via tox4j) trying to place files in the
        // same place.
        exclude("rootdoc.txt")
    }
}

dependencies {
    implementation(Libraries.kotlinStdLib)

    implementation(project(":core"))
    implementation(project(":domain"))

    implementation(Libraries.androidxCoreKtx)

    implementation(Libraries.appcompat)
    implementation(Libraries.constraintlayout)

    implementation(Libraries.material)
    implementation(Libraries.ktxCoroutinesCore)
    implementation(Libraries.ktxCoroutinesAndroid)

    implementation(Libraries.navigationFragmentKtx)
    implementation(Libraries.navigationUiKtx)

    implementation(Libraries.preference)

    implementation(Libraries.dagger)
    kapt(Libraries.daggerCompiler)

    implementation(Libraries.multidex)

    implementation(Libraries.zxingAndroidEmbedded)

    debugImplementation(Libraries.leakcanaryAndroid)

    testImplementation(Libraries.junit)
    androidTestImplementation(Libraries.rules)
    androidTestImplementation(Libraries.runner)
    androidTestImplementation(Libraries.espressoCore)
    androidTestImplementation(Libraries.espressoContrib)
    androidTestImplementation(Libraries.androidJUnit)
    androidTestImplementation(Libraries.mockk)
    kaptAndroidTest(Libraries.daggerCompiler)
}
