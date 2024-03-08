package org.quiltmc.boot;

public class BootstrapServer extends Bootstrap {
    private BootstrapServer() {
        super(BootstrapEnvironment.SERVER);
    }

    public static void main(String[] args) {
        new BootstrapServer().start(args);
    }
}
