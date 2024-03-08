package org.quiltmc.boot;

import java.nio.file.Path;

/** WHat loader should implement to be launched by {@link BootstrapLaunch}. Invoked with reflection. */
public interface BootstrapInvoked {

    /** Alternative to main(String[] args) that can re-launch.
     * 
     * @param launcher The context. The actual method should replace this parameter with {@link Object}, if the
     *            implementor is invoked via reflection.
     * @return The new loader to re-launch with, or null if this should exit. */
    Path run(BootstrapContext launcher, String[] args);
}
