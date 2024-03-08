package org.quiltmc.boot;

/** Indicates which bootstrap main class was started. */
public enum BootstrapEnvironment {

    /** Bootstrap.
     * <p>
     * I'm not sure what should be run - "org.quiltmc.loader.impl.launch.knot.Knot" does contain a main method, but it
     * doesn't actually run the game. */
    NONE,

    /** BootstrapClient.
     * <p>
     * Generally means that "org.quiltmc.loader.impl.launch.knot.KnotClient" should be used to run the game. */
    CLIENT,

    /** BootstrapServer.
     * <p>
     * Generally means that "org.quiltmc.loader.impl.launch.knot.KnotServer" should be used to run the game. */
    SERVER,

    /** BootstrapServerLauncher.
     * <p>
     * Generally this means that "org.quiltmc.loader.impl.launch.server.QuiltServerLauncher" should be used to run the
     * game. */
    SERVER_LAUNCHER
}
