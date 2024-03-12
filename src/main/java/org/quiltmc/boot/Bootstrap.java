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

import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Bootstrap {
	public static final String SYSTEM_PROPERTY_LOADER_JAR = "quiltmc.boot.loader_jar_file";

	private static Bootstrap instance;

	public final BootstrapEnvironment environment;
	private BootstrapLaunch launch = new BootstrapLaunch();

	Bootstrap(BootstrapEnvironment environment) {
		this.environment = environment;

		instance = this;
	}

	public static Bootstrap instance() {
		return instance;
	}

	public static void main(String[] args) {
		new Bootstrap(BootstrapEnvironment.NONE).start(args);
	}

	public BootstrapLaunch currentLaunch() {
		return launch;
	}

	void start(String[] args) {
		loadUrlHandlerProp();

		Path loaderPath = null;

		String sysprop = System.getProperty(SYSTEM_PROPERTY_LOADER_JAR);
		if (sysprop == null) {
			throw new Error(
				"Missing required system property '" + SYSTEM_PROPERTY_LOADER_JAR + "' for bootstrapping loader!"
			);
		}

		try {
			loaderPath = Paths.get(sysprop);
		} catch (InvalidPathException e) {
			throw new Error("Invalid loader jar file path " + sysprop, e);
		}

		if (!Files.exists(loaderPath)) {
			throw new Error("Cannot launch with missing path " + loaderPath);
		}

		while (true) {
			Path replacementLoader = launch.run(loaderPath, args);

			if (replacementLoader == null) {
				return;
			}

			launch = new BootstrapLaunch();
			loaderPath = replacementLoader;
		}
	}

	private static void loadUrlHandlerProp() {
		// Java requires we create a class named "Handler"
		// in the package "<system-prop>.<scheme>"
		// See java.net.URL#URL(java.lang.String, java.lang.String, int, java.lang.String)
		final String key = "java.protocol.handler.pkgs";
		final String pkg = "org.quiltmc.boot.urlhandler";
		String prop = System.getProperty(key);
		if (prop == null) {
			System.setProperty(key, pkg);
		} else if (!prop.contains(pkg)) {
			System.setProperty(key, prop + "|" + pkg);
		}
	}
}
