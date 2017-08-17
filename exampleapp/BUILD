android_binary(
    name = "exampleapp",
    srcs = ["src/main/java/com/google/keytransparency/exampleapp/KeyTransparencyExample.java"],    
    custom_package = "com.google.keytransparency.exampleapp",
    manifest = "src/main/AndroidManifest.xml",
    resource_files = glob(["src/main/res/**"]),
    deps = [
	"//keytransparency:keytransparency",
	"@androidsdk//com.android.support:appcompat-v7-25.0.0",
	"@androidsdk//com.android.support.constraint:constraint-layout-1.0.2",
	"@androidsdk//com.android.support:design-25.0.0",
	],
)
