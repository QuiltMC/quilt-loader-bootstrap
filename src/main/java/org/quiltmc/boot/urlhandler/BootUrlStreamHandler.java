/*
 * Copyright 2024 QuiltMC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.quiltmc.boot.urlhandler;

import java.io.IOException;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.net.InetAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

import org.quiltmc.boot.Bootstrap;

public abstract class BootUrlStreamHandler extends URLStreamHandler {

	final char letter;
	// Reference to this::directSetUrl
	final MethodHandle directSetUrl;

	public BootUrlStreamHandler(char letter) {
		this.letter = letter;
		try {
			MethodType mType = MethodType.methodType(
				void.class, URL.class, String.class, String.class, int.class, String.class, String.class,
				String.class, String.class, String.class
			);
			directSetUrl = MethodHandles.lookup().findVirtual(getClass(), "directSetUrl", mType).bindTo(this);
		} catch (NoSuchMethodException | IllegalAccessException e) {
			throw new Error("Failed to lookup our own method!");
		}
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
		if (delegate == null || delegate.quilt_passSetURL == null) {
			super.parseURL(u, spec, start, limit);
		} else {
			passDirectSetUrl(delegate);
			delegate.parseURL(u, spec, start, limit);
		}
	}

	private void passDirectSetUrl(DelegateUrlHandler delegate) {
		try {
			delegate.quilt_passSetURL.invokeExact(directSetUrl);
		} catch (Throwable e) {
			throw DelegateUrlHandler.asUnchecked(e);
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
		if (delegate == null || delegate.quilt_passSetURL == null) {
			super.setURL(u, protocol, host, port, authority, userInfo, path, query, ref);
		} else {
			passDirectSetUrl(delegate);
			delegate.setURL(u, protocol, host, port, authority, userInfo, path, query, ref);
		}
	}

	@Override
	@Deprecated
	protected void setURL(URL u, String protocol, String host, int port, String file, String ref) {

		DelegateUrlHandler delegate = getDelegate();
		if (delegate == null || delegate.quilt_passSetURL == null) {
			super.setURL(u, protocol, host, port, file, ref);
		} else {
			passDirectSetUrl(delegate);
			delegate.setURL(u, protocol, host, port, file, ref);
		}
	}

	/** Exposed as a method handle in {@link DelegateUrlHandler} */
	void directSetUrl(
		URL u, String protocol, String host, int port, String authority, String userInfo, String path, String query,
		String ref
	) {
		super.setURL(u, protocol, host, port, authority, userInfo, path, query, ref);
	}
}
