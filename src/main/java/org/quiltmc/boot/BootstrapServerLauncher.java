package org.quiltmc.boot;

public class BootstrapServerLauncher extends Bootstrap {
    private BootstrapServerLauncher() {
        super(BootstrapEnvironment.SERVER_LAUNCHER);
    }

    public static void main(String[] args) {
        new BootstrapServerLauncher().start(args);
    }
}
