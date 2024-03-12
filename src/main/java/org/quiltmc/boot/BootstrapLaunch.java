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

package org.quiltmc.boot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.MethodType;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLStreamHandler;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.spi.FileSystemProvider;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.quiltmc.boot.urlhandler.DelegateUrlHandler;

public class BootstrapLaunch implements BootstrapContext {
	private static final String BOOTSTRAP_TARGET_FILE = "META-INF/quilt-bootstrap/target.txt";

	public final BootstrapClassLoader classLoader = new BootstrapClassLoader();
	private final Map<Character, FileSystemProvider> fileSystemProviders = new ConcurrentHashMap<>();
	private final Map<Character, DelegateUrlHandler> urlStreamHandlers = new ConcurrentHashMap<>();

	public FileSystemProvider getFileSystemProvider(char letter) {
		return fileSystemProviders.get(letter);
	}

	public DelegateUrlHandler getUrlStreamHandler(char letter) {
		return urlStreamHandlers.get(letter);
	}

	@Override
	public void putFileSystemProvider(char letter, FileSystemProvider provider, URLStreamHandler handler) {
		if (letter < 'a' || letter > 'z') {
			throw new IllegalArgumentException("Only 'a-z' filesystems are supported!");
		}
		fileSystemProviders.put(letter, provider);
		urlStreamHandlers.put(letter, new DelegateUrlHandler(handler));
	}

	Path run(Path currentPath, String[] args) {
		final String bootstrapTarget;
		final BootstrapInvoked invoked;
		try {
			classLoader.add(currentPath.toUri().toURL());
			try (InputStream stream = classLoader.getResourceAsStream(BOOTSTRAP_TARGET_FILE)) {
				if (stream == null) {
					throw new Error(currentPath + " did not contain '" + BOOTSTRAP_TARGET_FILE + "'");
				}
				bootstrapTarget = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8)).readLine();
			} catch (IOException io) {
				throw new Error(currentPath + " couldn't read '" + BOOTSTRAP_TARGET_FILE + "'", io);
			}
			invoked = wrap(classLoader.loadClass(bootstrapTarget).getConstructor().newInstance());
		} catch (MalformedURLException | ReflectiveOperationException e) {
			throw new Error(e);
		}

		Path relaunchJar = invoked.run(this, args);

		if (relaunchJar == null) {
			return null;
		}

		if (!Files.exists(relaunchJar)) {
			throw new Error(
				"Relaunch failed: '" + relaunchJar + "' doesn't exist! Previous launch is " + bootstrapTarget + " from "
					+ currentPath
			);
		}

		return relaunchJar;
	}

	private static BootstrapInvoked wrap(Object targetObject) {

		if (targetObject instanceof BootstrapInvoked) {
			return (BootstrapInvoked) targetObject;
		}

		Class<?> cls = targetObject.getClass();
		try {
			Lookup lookup = MethodHandles.lookup();
			MethodType type = MethodType.methodType(Path.class, Object.class, String[].class);
			MethodHandle handle = lookup.findVirtual(cls, "run", type).bindTo(targetObject);
			return new BootstrapInvoked() {
				@Override
				public Path run(BootstrapContext launcher, String[] args) {
					try {
						return (Path) handle.invokeExact((Object) launcher, args);
					} catch (Throwable e) {
						if (e instanceof RuntimeException) {
							throw (RuntimeException) e;
						}
						if (e instanceof Error) {
							throw (Error) e;
						}
						throw new RuntimeException(e);
					}
				}
			};
		} catch (NoSuchMethodException | IllegalAccessException e) {
			throw new Error("Unable to find target method ", e);
		}
	}

	@Override
	public String environment() {
		return Bootstrap.instance().environment.name();
	}

	@Override
	public void addToClassPath(URL url) {
		classLoader.add(url);
	}
}
