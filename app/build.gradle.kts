plugins { id("com.android.application"); id("org.jetbrains.kotlin.android") }
android {
 namespace = "ir.ruzban.app"
 compileSdk = 35
 defaultConfig {
  applicationId = "ir.ruzban.app"
  minSdk = 26
  targetSdk = 35
  versionCode = 1
  versionName = "0.1.0"
  val url = providers.gradleProperty("ruzbanUrl").orNull ?: "https://example.invalid/ruzban/"
  buildConfigField("String", "RUZBAN_URL", "\"$url\"")
 }
 buildFeatures { buildConfig = true }
 compileOptions { sourceCompatibility = JavaVersion.VERSION_17; targetCompatibility = JavaVersion.VERSION_17 }
 kotlinOptions { jvmTarget = "17" }
}
