# Quilt Loader Bootstrap

This allows quilt loader, and it's libraries, to be added to the classpath after the bootstrap itself has launched.

This also allows for quilt-loader to swap itself out for a different local version.

This is intended to be as small as possible, and updated as infrequently as possible. This should allow quilt-loader to be updated to any future version and still work on this bootstrap.

Loader is expected to be installed *somewhere* already.

## Launch types

### Full Client Launcher

*"Full Launchers"* are launchers that support easily changing the loader version - like MultiMC, Prism, and a few others. These don't use this bootstrap, since it's unnecessary.

### Simple Client Launcher

The vanilla launcher, and some other custom launchers, don't have special support for updating quilt-loader, so this bootstrap is used.

This bootstrap should be the only quilt-loader library on the classpath.
(Intermediary / hashed / minecraft itself should still be on the classpath).

Quilt's Installer will add a profile containing just this bootstrap. It will also download the full set of loader libraries for that version, and put them in a maven-like folder. (Probably similar to how the vanilla launcher currently works).

It will also fill in the system property `quiltmc.boot.loader_jar_file` which points to the loader jar file.

(The `"arguments": { "jvm": [] }` part of a launch json can add system properties which the user can't change).

This bootrap then finds said jar file, adds it to its classloader, and invokes loaders main method.

Loader itself is expected to handle being bootstrapped, and add its libraries via BootstrapLaunch.classLoader.add(URL). (The library root will need to be specified via a loader property, then loader can read it's own `quilt_installer.json` file to determine which libraries it needs).

### Installed Server

This is where the installer GUI's "Server" tab is selected and used. This could download both the minecraft server jar and a version of loader.

The bootstrap is unnecessary for this. (Alternatively, the GUI installer could install like `Dedicated Server Launcher`).

### Dedicated Server Launcher

This is expected to be launched from a bash/batch script, or just a command prompt, or via an unmodifiable paid server host.
Either way the executed command would be something like `java -jar server.jar`, possibly with some memory limits added. (This is unmodifiable).

We'll need to create an automatic dedicated server installer jar and download place. It will need to contain:

- The bootstrap jar contents
- The installer? (CLI only)
- The minecraft version and loader version in a text file. (We might be able to append this to a jar file with javascript?)

The installer jar (when run) will:

- Download the right minecraft server jar, and the loader jar, and its libraries.
- Set the system property `Bootstrap#SYSTEM_PROPERTY_LOADER_JAR` to the loader jar
- Set a loader-specific system property to the library root, to let loader find the libraries it needs.
- Invoke `BootstrapServer.main` with the arguments given originally to it.
