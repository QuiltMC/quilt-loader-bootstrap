package org.quiltmc.boot;

import java.net.URL;
import java.net.URLStreamHandler;
import java.nio.file.spi.FileSystemProvider;

public interface BootstrapContext {

    /** @return {@link BootstrapEnvironment#name()} */
    String environment();

    void putFileSystemProvider(char letter, FileSystemProvider provider, URLStreamHandler handler);

    void addToClassPath(URL url);
}
