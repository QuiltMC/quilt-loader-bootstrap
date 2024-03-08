package org.quiltmc.boot;

import java.net.URL;
import java.net.URLClassLoader;

public class BootstrapClassLoader extends URLClassLoader {

    public BootstrapClassLoader() {
        super(new URL[0]);
    }

    public void add(URL url) {
        super.addURL(url);
    }
}
