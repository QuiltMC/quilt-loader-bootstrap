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

/** Indicates which bootstrap main class was started. */
public enum BootstrapEnvironment {

	/** Bootstrap.
	 * <p>
	 * I'm not sure what should be run - "org.quiltmc.loader.impl.launch.knot.Knot" does contain a main method, but it
	 * doesn't actually run the game. */
	NONE,

	/** BootstrapClient.
	 * <p>
	 * Generally means that "org.quiltmc.loader.impl.launch.knot.KnotClient" should be used to run the game. */
	CLIENT,

	/** BootstrapServer.
	 * <p>
	 * Generally means that "org.quiltmc.loader.impl.launch.knot.KnotServer" should be used to run the game. */
	SERVER,

	/** BootstrapServerLauncher.
	 * <p>
	 * Generally this means that "org.quiltmc.loader.impl.launch.server.QuiltServerLauncher" should be used to run the
	 * game. */
	SERVER_LAUNCHER
}
