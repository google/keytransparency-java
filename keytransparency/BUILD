android_library(
  name = "keytransparency",
  visibility = ["//visibility:public"],
  srcs = ["src/main/java/com/google/keytransparency/KeyTransparencyException.java",
          "src/main/java/com/google/keytransparency/KTClient.java"],
  custom_package = "com.google.keytransparency",
  manifest = "src/main/AndroidManifest.xml",
  resource_files = glob(["src/main/res/**"]),
  deps = [":gobind_keytransparency_android"],
)

aar_import(
        name = "gobind_keytransparency_android",
        aar = "keytransparency_gobind.aar",
    )

genrule(
    name = "gobind_keytransparency_android_gen_aar",
    srcs = [
       # "//some:files",  # a filegroup with multiple files in it ==> $(locations)
       # "//other:gen",   # a genrule with a single output ==> $(location)
    ],
    outs = ["keytransparency_gobind.aar"],
    tags=["local"],
    cmd = "gomobile bind -target android -o $@ -javapkg com.google.keytransparency.gobind github.com/google/keytransparency/core/client/gobindClient", # github.com/google/keytransparency/core/client/kt github.com/google/keytransparency/core/crypto/commitments",
)


