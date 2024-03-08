package org.quiltmc.boot;

public class BootstrapClient extends Bootstrap {
    private BootstrapClient() {
        super(BootstrapEnvironment.CLIENT);
    }

    public static void main(String[] args) {
        new BootstrapClient().start(args);
    }
}
