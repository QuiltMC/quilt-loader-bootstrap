package org.quiltmc.boot.urlhandler;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

/** Allows {@link BootUrlStreamHandler} to invoke methods here by promoting them to protected (in this package).
 * 
 * @deprecated Replaced by {@link DelegateUrlHandler} */
@Deprecated
public abstract class InvokedUrlHandler extends URLStreamHandler {

    @Override
    protected abstract URLConnection openConnection(URL u) throws IOException;

    @Override
    protected URLConnection openConnection(URL u, Proxy p) throws IOException {
        return super.openConnection(u, p);
    }

    @Override
    protected void parseURL(URL u, String spec, int start, int limit) {
        super.parseURL(u, spec, start, limit);
    }

    @Override
    protected int getDefaultPort() {
        return super.getDefaultPort();
    }

    @Override
    protected boolean equals(URL u1, URL u2) {
        return super.equals(u1, u2);
    }

    @Override
    protected int hashCode(URL u) {
        return super.hashCode(u);
    }

    @Override
    protected boolean sameFile(URL u1, URL u2) {
        return super.sameFile(u1, u2);
    }

    @Override
    protected InetAddress getHostAddress(URL u) {
        return super.getHostAddress(u);
    }

    @Override
    protected boolean hostsEqual(URL u1, URL u2) {
        return super.hostsEqual(u1, u2);
    }

    @Override
    protected String toExternalForm(URL u) {
        return super.toExternalForm(u);
    }

    @Override
    protected void setURL(
        URL u, String protocol, String host, int port, String authority, String userInfo, String path, String query,
        String ref
    ) {
        super.setURL(u, protocol, host, port, authority, userInfo, path, query, ref);
    }

    @Override
    @Deprecated
    protected void setURL(URL u, String protocol, String host, int port, String file, String ref) {
        super.setURL(u, protocol, host, port, file, ref);
    }
}
