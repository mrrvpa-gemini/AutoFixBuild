package com.rvpa.autofix;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

public class ErrorDetector {

    private Context context;
    private Map<String, String> errorMap;

    public ErrorDetector(Context context) {
        this.context = context;
        initErrorMap();
    }

    private void initErrorMap() {
        errorMap = new HashMap<>();

        // Git Errors
        errorMap.put("Repository not found", "Repository not found. Check URL or permissions.");
        errorMap.put("Permission denied (publickey)", "SSH key not configured. Use HTTPS or add SSH key.");
        errorMap.put("Authentication failed", "Invalid credentials. Check username/token.");
        errorMap.put("Could not read from remote repository", "Network issue or invalid repository.");
        errorMap.put("fatal: not a git repository", "Not a git repo. Run git init first.");

        // Workflow Errors
        errorMap.put("Process completed with exit code 1", "Workflow failed. Check logs.");
        errorMap.put("The workflow is not valid", "Invalid workflow YAML. Check syntax.");
        errorMap.put("Error parsing workflow", "YAML parsing error. Fix indentation.");
        errorMap.put("No event triggers defined in on", "No trigger event. Add push, pull_request.");
        errorMap.put("Job cancelled", "Job cancelled. Check timeout or dependencies.");
        errorMap.put("Timed out", "Timeout. Increase timeout-minutes.");

        // Flutter Errors
        errorMap.put("Flutter SDK not found", "Flutter not installed. Install Flutter SDK.");
        errorMap.put("Unable to locate flutter", "Flutter path not set. Add to PATH.");
        errorMap.put("Target file lib/main.dart not found", "Main.dart missing. Create lib/main.dart.");
        errorMap.put("pubspec.yaml not found", "pubspec.yaml missing. Initialize Flutter project.");
        errorMap.put("version solving failed", "Dependency conflict. Check pubspec.yaml.");
        errorMap.put("dart: command not found", "Dart not installed. Install Dart SDK.");
        errorMap.put("MissingPluginException", "Plugin not registered. Check main.dart.");

        // Dart Errors
        errorMap.put("dart pub get failed", "Pub get failed. Check pubspec.yaml.");
        errorMap.put("Null check operator used on a null value", "Null safety issue. Use ? operator.");
        errorMap.put("Unhandled exception", "Exception not handled. Use try-catch.");
        errorMap.put("Compilation failed", "Compilation error. Fix syntax.");
        errorMap.put("Build failed due to Dart errors", "Dart errors. Fix code.");

        // Gradle Errors
        errorMap.put("Gradle build failed", "Gradle build failed. Check build.gradle.");
        errorMap.put("Execution failed for task ':app:assembleRelease'", "AssembleRelease failed. Check signing.");
        errorMap.put("Could not resolve dependencies", "Dependency not found. Add repository.");
        errorMap.put("Could not determine java version", "Java not set. Set JAVA_HOME.");
        errorMap.put("Gradle daemon disappeared unexpectedly", "Gradle daemon killed. Increase memory.");
        errorMap.put("OutOfMemoryError", "Out of memory. Increase heap size.");

        // Java / JDK Errors
        errorMap.put("JAVA_HOME is not set", "JAVA_HOME not set. Set to JDK path.");
        errorMap.put("Unsupported class file major version", "JDK version mismatch. Use correct JDK.");
        errorMap.put("Could not find Java", "Java not installed. Install OpenJDK.");
        errorMap.put("Java heap space", "Heap space exhausted. Increase Xmx.");
        errorMap.put("Invalid source release", "Invalid source version. Check build.gradle.");

        // Android SDK Errors
        errorMap.put("SDK location not found", "SDK not found. Set sdk.dir in local.properties.");
        errorMap.put("Failed to find Build Tools", "Build tools missing. Install build-tools.");
        errorMap.put("Android SDK not installed", "Android SDK not installed. Install SDK.");
        errorMap.put("License for package not accepted", "Accept licenses. Run sdkmanager --licenses.");
        errorMap.put("Platform android-XX not found", "Platform not installed. Install platform.");

        // NDK / CMake Errors
        errorMap.put("NDK not configured", "NDK not set. Configure NDK path.");
        errorMap.put("No version of NDK matched", "NDK version mismatch. Install correct NDK.");
        errorMap.put("CMake not found", "CMake not installed. Install CMake.");
        errorMap.put("Native build failed", "Native build failed. Check CMakeLists.txt.");

        // Kotlin Errors
        errorMap.put("Kotlin compiler failed", "Kotlin compiler error. Check Kotlin version.");
        errorMap.put("Unresolved reference", "Unresolved reference. Import correct package.");
        errorMap.put("Module was compiled with an incompatible version of Kotlin", "Kotlin version mismatch. Use same version.");

        // Dependency Errors
        errorMap.put("Duplicate class found", "Duplicate class. Exclude duplicates.");
        errorMap.put("Dependency resolution failed", "Dependency resolution failed. Check repositories.");
        errorMap.put("Could not download dependencies", "Download failed. Check network.");
        errorMap.put("Version conflict", "Version conflict. Use dependencyManagement.");
        errorMap.put("Package not found", "Package not found. Install package.");

        // Signing APK Errors
        errorMap.put("Keystore file not found", "Keystore missing. Generate keystore.");
        errorMap.put("SigningConfig release missing", "Signing config missing. Add signingConfigs.");
        errorMap.put("Key alias not found", "Key alias not found. Check alias.");
        errorMap.put("Keystore password incorrect", "Keystore password incorrect. Check password.");
        errorMap.put("APK signing failed", "APK signing failed. Check keystore.");

        // Memory Errors
        errorMap.put("GC overhead limit exceeded", "GC overhead exceeded. Increase memory.");
        errorMap.put("OutOfMemoryError", "Out of memory. Increase heap size.");

        // Network Errors
        errorMap.put("Connection timed out", "Connection timeout. Check network.");
        errorMap.put("Failed to download", "Download failed. Check network.");
        errorMap.put("SSLHandshakeException", "SSL error. Check certificates.");
        errorMap.put("Temporary failure in name resolution", "DNS error. Check network.");

        // Cache Errors
        errorMap.put("Corrupted Gradle cache", "Gradle cache corrupted. Clear cache.");
        errorMap.put("Pub cache is corrupted", "Pub cache corrupted. Clear cache.");
        errorMap.put("Checksum mismatch", "Checksum mismatch. Clear cache.");

        // File Errors
        errorMap.put("File not found", "File not found. Check path.");
        errorMap.put("No such file or directory", "File/directory not found. Check path.");
        errorMap.put("Permission denied", "Permission denied. Check permissions.");
        errorMap.put("Read-only file system", "Read-only filesystem. Change permissions.");

        // Android Manifest Errors
        errorMap.put("Manifest merger failed", "Manifest merger failed. Check manifest.");
        errorMap.put("uses-sdk:minSdkVersion", "Min SDK version too low. Increase minSdk.");
        errorMap.put("Duplicate provider", "Duplicate provider. Remove duplicate.");
        errorMap.put("Package name conflict", "Package name conflict. Change package name.");

        // Resource Errors
        errorMap.put("AAPT2 error", "AAPT2 error. Check resources.");
        errorMap.put("Resource linking failed", "Resource linking failed. Check XML.");
        errorMap.put("Duplicate resources", "Duplicate resources. Remove duplicates.");
        errorMap.put("Missing resource", "Resource missing. Create resource.");

        // XML Errors
        errorMap.put("XML parsing failed", "XML parsing failed. Check XML syntax.");
        errorMap.put("Malformed XML", "Malformed XML. Fix XML.");
        errorMap.put("Unexpected tag", "Unexpected tag. Check XML.");

        // Umum
        errorMap.put("Build failed", "Build failed. Check logs.");
        errorMap.put("Task failed", "Task failed. Check logs.");
        errorMap.put("Exit code 1", "Exit code 1. General error.");
        errorMap.put("Exit code 137", "Exit code 137. Out of memory.");
        errorMap.put("Exit code 143", "Exit code 143. Process killed.");
        errorMap.put("Segmentation fault", "Segmentation fault. Memory issue.");
        errorMap.put("Internal compiler error", "Compiler error. Update tools.");
    }

    public String detect(String errorMessage) {
        for (Map.Entry<String, String> entry : errorMap.entrySet()) {
            if (errorMessage.toLowerCase().contains(entry.getKey().toLowerCase())) {
                return entry.getValue();
            }
        }
        return "Unknown error: " + errorMessage;
    }
}
