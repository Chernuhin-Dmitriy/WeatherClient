plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.androidx.room)
    alias(libs.plugins.androidx.navigation.safeargs)
}
hilt {
    enableAggregatingTask = false
}


android {
    namespace = "com.example.weatherclient"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.weatherclient"
        minSdk = 23
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Добавляем для диагностики
//        multiDexEnabled = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            // Для production реальный API ключ
            buildConfigField("String", "WEATHER_API_KEY", "\"3923bd19a54441ee81785438250907\"")
        }
        debug {
            isMinifyEnabled = false
            // Для debug тестовый API ключ
            buildConfigField("String", "WEATHER_API_KEY", "\"3923bd19a54441ee81785438250907\"")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.7"
    }
    room {
        schemaDirectory("$projectDir/schemas")
    }
//    packaging {
//        resources {
//            excludes += "/META-INF/{AL2.0,LGPL2.1}"
//            excludes += "/META-INF/DEPENDENCIES"
//            excludes += "/META-INF/LICENSE"
//            excludes += "/META-INF/LICENSE.txt"
//            excludes += "/META-INF/NOTICE"
//            excludes += "/META-INF/NOTICE.txt"
//        }
//    }
    configurations.configureEach {
        resolutionStrategy {
            force("org.jetbrains.kotlin:kotlin-stdlib:1.9.24")
            force("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.9.24")
            force("com.squareup:javapoet:1.13.0")

            // Исключаем конфликтующие зависимости
            exclude(group = "xmlpull", module = "xmlpull")
            exclude(group = "xpp3", module = "xpp3")
            exclude(group = "org.apache.httpcomponents", module = "httpclient")
        }
    }
}

dependencies {
    // Core Android
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.material)

    implementation(libs.compose.ui)
    implementation(libs.compose.ui.tooling)
    implementation(libs.compose.material3)
    implementation(libs.androidx.compose.material3.pullrefresh)

    // Navigation
    implementation(libs.navigation.compose)

    // ViewModel
    implementation(libs.lifecycle.viewmodel.compose)
    implementation(libs.lifecycle.runtime.ktx)

    // Hilt
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation.compose)
    implementation(libs.androidx.navigation.safe.args.generator)
    ksp(libs.hilt.compiler)

    // Room
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    // Network
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.moshi)
    implementation(libs.moshi.kotlin)
    implementation(libs.okhttp.logging)

    // Coroutines
    implementation(libs.coroutines.android)

    // Coil
    implementation(libs.coil.compose)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}