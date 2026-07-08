#!/bin/bash

echo "[+] Fixing Gradle build..."

# 1. Gradle wrapper
mkdir -p android/gradle/wrapper
cat > android/gradle/wrapper/gradle-wrapper.properties << 'EOL'
distributionBase=GRADLE_USER_HOME
distributionPath=wrapper/dists
distributionUrl=https\://services.gradle.org/distributions/gradle-8.4-bin.zip
zipStoreBase=GRADLE_USER_HOME
zipStorePath=wrapper/dists
EOL

# 2. Project build.gradle
cat > android/build.gradle << 'EOL'
buildscript {
    ext.kotlin_version = '1.9.20'
    repositories { google(); mavenCentral() }
    dependencies {
        classpath 'com.android.tools.build:gradle:8.1.4'
        classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version"
    }
}
allprojects { repositories { google(); mavenCentral() } }
task clean(type: Delete) { delete rootProject.buildDir }
EOL

# 3. App build.gradle
cat > android/app/build.gradle << 'EOL'
plugins {
    id 'com.android.application'
    id 'kotlin-android'
}
android {
    compileSdk 34
    defaultConfig {
        applicationId "com.apkbuilder.app"
        minSdk 21
        targetSdk 34
        versionCode 1
        versionName "1.0"
    }
    buildTypes {
        release {
            signingConfig signingConfigs.debug
            minifyEnabled true
            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
        }
    }
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_17
        targetCompatibility JavaVersion.VERSION_17
    }
    kotlinOptions { jvmTarget = '17' }
}
dependencies {
    implementation 'androidx.core:core-ktx:1.12.0'
    implementation 'androidx.appcompat:appcompat:1.6.1'
    implementation 'com.google.android.material:material:1.11.0'
    implementation 'androidx.constraintlayout:constraintlayout:2.1.4'
}
EOL

# 4. settings.gradle
cat > settings.gradle << 'EOL'
pluginManagement {
    repositories { google(); mavenCentral(); gradlePluginPortal() }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories { google(); mavenCentral() }
}
rootProject.name = "apk-builder"
include ':app'
EOL

# 5. gradle.properties
cat > gradle.properties << 'EOL'
org.gradle.jvmargs=-Xmx2048m
org.gradle.daemon=true
android.useAndroidX=true
android.enableJetifier=true
EOL

# 6. local.properties
echo "sdk.dir=$HOME/Android/Sdk" > local.properties

echo "[✓] All fixed! Run: ./gradlew assembleRelease"
