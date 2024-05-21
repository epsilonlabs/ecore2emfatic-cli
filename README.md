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

## Running

To produce Emfatic sources from an `.ecore` file, using the all-in-one JAR file in `build/libs/*-all.jar`:

```sh
java -jar path/to/ecore2emfatic-VERSION-all.jar path/to/your.ecore
```

When using one of the native binaries, this can be simplified to:

```sh
path/to/ecore2emfatic path/to/your.ecore
```

## Using native binaries as a Git filter

In order to compute differences between `.ecore` file versions using a Git `textconv`, first add the native binary to your path.
In Linux/Mac:

```sh
cp build/native/nativeCompile/ecore2emfatic /folder/in/PATH
```

You will then need to define the `ecore` differencing algorithm:

```sh
git config --global diff.ecore.textconv ecore2emfatic
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

## Providing URI mappings

If your `.ecore` files import other metamodels through URIs, you can provide mappings from URIs to specific `.ecore` files or folders via the `--from` and `--to` options:

```sh
path/to/ecore2emfatic --from platform:/resource --to path/to/base/folder your.ecore
```

You can specify multiple pairs of `--from` and `--to` options, as in:

```sh
path/to/ecore2emfatic --from A --to B --from C --to D your.ecore
```

If you are using this tool as a `textconv` filter via Git, you would need to provide these options in your repository's configuration.
For example:

```sh
git config diff.ecore.textconv "ecore2emfatic --from A --to B"
```
