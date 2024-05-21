# Command-line Ecore->Emfatic converter

This is a small command-line tool that produces [Emfatic](https://eclipse.dev/emfatic/) source from an `.ecore` metamodel.
It is intended to be used as a `textconv` filter for Git differencing.

## Building

To build and test the Java and native binary versions of the project, run:

```sh
./gradlew build nativeCompile nativeTest
```

Note that for the native binaries, you will need [GraalVM](https://www.graalvm.org/) 17 or newer.
It is recommended to use [SDKMAN](https://sdkman.io/) for installing and managing JDKs.

## Using native binaries as a Git filter

In order to compute differences between `.ecore` file versions using a Git `textconv`, first add the native binary to your path.
In Linux/Mac:

```sh
cp build/native/nativeCompile/Ecore2Emfatic /folder/in/PATH
```

You will then need to define the `ecore` differencing algorithm:

```sh
git config --global diff.ecore.textconv Ecore2Emfatic
```

You can then use this conversion from any Git repository, by adding a `.gitattributes` file to its root folder with this content:

```text
*.ecore  diff=ecore
```

You should then be able to compute differences between versions of `.ecore` file using regular Git tools.
The results would look like this:

```diff
diff --git a/OO.ecore b/OO.ecore
index 84da8c9..f611dd1 100644
--- a/OO.ecore
+++ b/OO.ecore
@@ -68,7 +68,7 @@ class Attribute extends StructuralFeature {
 
 enum VisibilityEnum {
        public = 1;
-       private = 2;
+       private = 3;
 }
```
