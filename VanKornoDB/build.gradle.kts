plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("maven-publish")
}

android {
    namespace = "com.vankorno.vankornodb"
    compileSdk = 37
    
    defaultConfig {
        minSdk = 21
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }
    
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_25
        targetCompatibility = JavaVersion.VERSION_25
    }
    publishing {
        singleVariant("release") {
            // Replaces manual sourcesJar task
            withSourcesJar()
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    testImplementation(libs.junit)
    implementation(libs.kotlin.reflect)
}

val versionTag = System.getenv("VERSION") ?: "unspecified"

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                from(components.findByName("release"))
                groupId = "com.github.VanKorno"
                artifactId = "VanKornoDB"
                version = versionTag
            }
        }
    }
}
