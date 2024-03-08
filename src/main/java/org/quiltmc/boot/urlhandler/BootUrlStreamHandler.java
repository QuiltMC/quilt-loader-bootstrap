package org.quiltmc.boot.urlhandler;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

import org.quiltmc.boot.Bootstrap;

public abstract class BootUrlStreamHandler extends URLStreamHandler {

    final char letter;

    public BootUrlStreamHandler(char letter) {
        this.letter = letter;
    }

    private DelegateUrlHandler getDelegate() {
        return Bootstrap.instance().currentLaunch().getUrlStreamHandler(letter);
    }

    private DelegateUrlHandler getDelegateOrThrow() throws IOException {
        DelegateUrlHandler delegate = getDelegate();
        if (delegate == null) {
            throw new IOException("No delegate has been set for 'quilt." + letter + "fs'");
        }
        return delegate;
    }

    @Override
    protected URLConnection openConnection(URL u) throws IOException {
        return getDelegateOrThrow().openConnection(u);
    }

    @Override
    protected URLConnection openConnection(URL u, Proxy p) throws IOException {
        return getDelegateOrThrow().openConnection(u, p);
    }

    @Override
    protected void parseURL(URL u, String spec, int start, int limit) {
        DelegateUrlHandler delegate = getDelegate();
        if (delegate == null) {
            super.parseURL(u, spec, start, limit);
        } else {
            delegate.parseURL(u, spec, start, limit);
        }
    }

    @Override
    protected int getDefaultPort() {
        DelegateUrlHandler delegate = getDelegate();
        if (delegate == null) {
            return super.getDefaultPort();
        } else {
            return delegate.getDefaultPort();
        }
    }

    @Override
    protected boolean equals(URL u1, URL u2) {
        DelegateUrlHandler delegate = getDelegate();
        if (delegate == null) {
            return super.equals(u1, u2);
        } else {
            return delegate.equals(u1, u2);
        }
    }

    @Override
    protected int hashCode(URL u) {
        DelegateUrlHandler delegate = getDelegate();
        if (delegate == null) {
            return super.hashCode(u);
        } else {
            return delegate.hashCode(u);
        }
    }

    @Override
    protected boolean sameFile(URL u1, URL u2) {
        DelegateUrlHandler delegate = getDelegate();
        if (delegate == null) {
            return super.sameFile(u1, u2);
        } else {
            return delegate.sameFile(u1, u2);
        }
    }

    @Override
    protected synchronized InetAddress getHostAddress(URL u) {
        DelegateUrlHandler delegate = getDelegate();
        if (delegate == null) {
            return super.getHostAddress(u);
        } else {
            return delegate.getHostAddress(u);
        }
    }

    @Override
    protected boolean hostsEqual(URL u1, URL u2) {
        DelegateUrlHandler delegate = getDelegate();
        if (delegate == null) {
            return super.hostsEqual(u1, u2);
        } else {
            return delegate.hostsEqual(u1, u2);
        }
    }

    @Override
    protected String toExternalForm(URL u) {
        DelegateUrlHandler delegate = getDelegate();
        if (delegate == null) {
            return super.toExternalForm(u);
        } else {
            return delegate.toExternalForm(u);
        }
    }

    @Override
    protected void setURL(
        URL u, String protocol, String host, int port, String authority, String userInfo, String path, String query,
        String ref
    ) {
        DelegateUrlHandler delegate = getDelegate();
        if (delegate == null) {
            super.setURL(u, protocol, host, port, authority, userInfo, path, query, ref);
        } else {
            delegate.setURL(u, protocol, host, port, authority, userInfo, path, query, ref);
        }
    }

    @Override
    @Deprecated
    protected void setURL(URL u, String protocol, String host, int port, String file, String ref) {

        DelegateUrlHandler delegate = getDelegate();
        if (delegate == null) {
            super.setURL(u, protocol, host, port, file, ref);
        } else {
            delegate.setURL(u, protocol, host, port, file, ref);
        }
    }
}
