package org.quiltmc.boot.urlhandler;

import java.io.IOException;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

public final class DelegateUrlHandler extends URLStreamHandler {

    final URLStreamHandler delegate;
    // Reflected methods
    final MethodHandle openConnection_URL;
    final MethodHandle openConnection_URL_Proxy;
    final MethodHandle parseURL;
    final MethodHandle getDefaultPort;
    final MethodHandle equals_URL_URL;
    final MethodHandle hashCode_URL;
    final MethodHandle sameFile;
    final MethodHandle getHostAddress;
    final MethodHandle hostsEqual;
    final MethodHandle toExternalForm;
    final MethodHandle setURL_NEW;
    final MethodHandle setURL_DEPRECATED;

    public DelegateUrlHandler(URLStreamHandler delegate) {
        this.delegate = delegate;
        openConnection_URL = lookup("openConnection", URLConnection.class, URL.class);
        if (openConnection_URL == null) {
            // It's an abstract method, so it's required
            throw new IllegalArgumentException(
                "The delegate '" + delegate.getClass() + "' doesn't declare a public method "
            );
        }
        openConnection_URL_Proxy = lookup("openConnection", URLConnection.class, URL.class, Proxy.class);
        parseURL = lookup("parseURL", void.class, URL.class, String.class, int.class, int.class);
        getDefaultPort = lookup("getDefaultPort", int.class);
        equals_URL_URL = lookup("equals", boolean.class, URL.class, URL.class);
        hashCode_URL = lookup("hashCode", int.class, URL.class);
        sameFile = lookup("sameFile", boolean.class, URL.class, URL.class);
        getHostAddress = lookup("getHostAddress", InetAddress.class, URL.class);
        hostsEqual = lookup("hostsEqual", boolean.class, URL.class, URL.class);
        toExternalForm = lookup("toExternalForm", String.class, URL.class);
        setURL_NEW = lookup(
            "setURL", void.class, URL.class, String.class, String.class, int.class, String.class, String.class,
            String.class, String.class, String.class
        );
        setURL_DEPRECATED = lookup(
            "setURL", void.class, URL.class, String.class, String.class, int.class, String.class, String.class
        );
    }

    private MethodHandle lookup(String methodName, Class<?> returnType, Class<?>... argTypes) {
        try {

            return MethodHandles.publicLookup()
                .findVirtual(delegate.getClass(), methodName, MethodType.methodType(returnType, argTypes))
                .bindTo(delegate);

        } catch (NoSuchMethodException | IllegalAccessException e) {
            try {
                Method realMethod = delegate.getClass().getDeclaredMethod(methodName, argTypes);

                if (realMethod.getReturnType().equals(returnType)) {
                    throw new IllegalArgumentException(
                        "Unable to reflect " + realMethod + " because it is not public!"
                    );
                } else {
                    return null;
                }

            } catch (NoSuchMethodException | SecurityException e2) {
                // That's okay
                return null;
            }
        }
    }

    private static IOException asIOException(Throwable e) {
        if (e instanceof IOException) {
            return (IOException) e;
        } else if (e instanceof RuntimeException) {
            throw (RuntimeException) e;
        } else if (e instanceof Error) {
            throw (Error) e;
        } else {
            return new IOException(e);
        }
    }

    private static RuntimeException asUnchecked(Throwable e) {
        if (e instanceof RuntimeException) {
            return (RuntimeException) e;
        } else if (e instanceof Error) {
            throw (Error) e;
        } else {
            return new RuntimeException(e);
        }
    }

    @Override
    protected URLConnection openConnection(URL u) throws IOException {
        try {
            return (URLConnection) openConnection_URL.invokeExact(u);
        } catch (Throwable e) {
            throw asIOException(e);
        }
    }

    @Override
    protected URLConnection openConnection(URL u, Proxy p) throws IOException {
        if (openConnection_URL_Proxy == null) {
            return super.openConnection(u, p);
        }

        try {
            return (URLConnection) openConnection_URL_Proxy.invokeExact(u, p);
        } catch (Throwable e) {
            throw asIOException(e);
        }
    }

    @Override
    protected void parseURL(URL u, String spec, int start, int limit) {
        if (parseURL == null) {
            super.parseURL(u, spec, start, limit);
            return;
        }

        try {
            parseURL.invokeExact(u, spec, start, limit);
            return;
        } catch (Throwable e) {
            throw asUnchecked(e);
        }
    }

    @Override
    protected int getDefaultPort() {
        if (getDefaultPort == null) {
            return super.getDefaultPort();
        }

        try {
            return (int) getDefaultPort.invokeExact();
        } catch (Throwable e) {
            throw asUnchecked(e);
        }
    }

    @Override
    protected boolean equals(URL u1, URL u2) {
        if (equals_URL_URL == null) {
            return super.equals(u1, u2);
        }

        try {
            return (boolean) equals_URL_URL.invokeExact(u1, u2);
        } catch (Throwable e) {
            throw asUnchecked(e);
        }
    }

    @Override
    protected int hashCode(URL u) {
        if (hashCode_URL == null) {
            return super.hashCode(u);
        }

        try {
            return (int) hashCode_URL.invokeExact(u);
        } catch (Throwable e) {
            throw asUnchecked(e);
        }
    }

    @Override
    protected boolean sameFile(URL u1, URL u2) {
        if (sameFile == null) {
            return super.sameFile(u1, u2);
        }

        try {
            return (boolean) sameFile.invokeExact(u1, u2);
        } catch (Throwable e) {
            throw asUnchecked(e);
        }
    }

    @Override
    protected synchronized InetAddress getHostAddress(URL u) {
        if (getHostAddress == null) {
            return super.getHostAddress(u);
        }

        try {
            return (InetAddress) getHostAddress.invokeExact(u);
        } catch (Throwable e) {
            throw asUnchecked(e);
        }
    }

    @Override
    protected boolean hostsEqual(URL u1, URL u2) {
        if (hostsEqual == null) {
            return super.hostsEqual(u1, u2);
        }

        try {
            return (boolean) hostsEqual.invokeExact(u1, u2);
        } catch (Throwable e) {
            throw asUnchecked(e);
        }
    }

    @Override
    protected String toExternalForm(URL u) {
        if (toExternalForm == null) {
            return super.toExternalForm(u);
        }

        try {
            return (String) toExternalForm.invokeExact(u);
        } catch (Throwable e) {
            throw asUnchecked(e);
        }
    }

    @Override
    protected void setURL(
        URL u, String protocol, String host, int port, String authority, String userInfo, String path, String query,
        String ref
    ) {
        if (setURL_NEW == null) {
            super.setURL(u, protocol, host, port, authority, userInfo, path, query, ref);
            return;
        }

        try {
            setURL_NEW.invokeExact(u, protocol, host, port, authority, userInfo, path, query, ref);
            return;
        } catch (Throwable e) {
            throw asUnchecked(e);
        }
    }

    @Override
    @Deprecated
    protected void setURL(URL u, String protocol, String host, int port, String file, String ref) {
        if (setURL_DEPRECATED == null) {
            super.setURL(u, protocol, host, port, file, ref);
            return;
        }

        try {
            setURL_DEPRECATED.invokeExact(u, protocol, host, port, file, ref);
            return;
        } catch (Throwable e) {
            throw asUnchecked(e);
        }
    }
}
