plugins {
    alias(libs.plugins.android.library)
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
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    testImplementation(libs.junit)
    implementation(libs.kotlin.reflect)
    
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.runner)
}

val versionTag = providers.environmentVariable("VERSION").orNull
    ?: providers.gradleProperty("version").orNull.takeIf { it != "unspecified" }
    ?: project.version.takeIf { it != "unspecified" }?.toString()
    ?: "unspecified"

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                from(components["release"])
                groupId = "com.github.VanKorno"
                artifactId = "VanKornoDB"
                version = versionTag
            }
        }
    }
}
