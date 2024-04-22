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

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.net.URL;
import java.net.URLStreamHandler;
import java.nio.file.spi.FileSystemProvider;

import org.quiltmc.boot.urlhandler.RedirectedUrlStreamHandler;

public interface BootstrapContext {

	/** @return {@link BootstrapEnvironment#name()} */
	String environment();

	/** @param letter The letter for the filesystem. The resulting schema will be "quilt.#fs", where "#" is replaced
	 *            with the character. Only [a-z] are supported, as those are the only filesystems provided in this
	 *            bootstrap.
	 * @param handler A {@link URLStreamHandler} for the {@link FileSystemProvider}. There are a few additional
	 *            constraints:
	 *            <ul>
	 *            <li>All overridden methods must be public, as they will be found via
	 *            {@link MethodHandles#publicLookup()}.</li>
	 *            <li>If the stream handler overrides parseURL or setURL then they should also provide the method
	 *            {@link RedirectedUrlStreamHandler#quilt_passSetURL(java.lang.invoke.MethodHandle)}. (This is invoked
	 *            via reflection in all cases - the interface is provided for clarity)</li>
	 *            </ul>
	 */
	void putFileSystemProvider(char letter, FileSystemProvider provider, URLStreamHandler handler);

	/** @param letter The letter for the filesystem. The resulting schema will be "quilt.#fs", where "#" is replaced
	 *            with the character. Only [a-z] are supported, as those are the only filesystems provided in this
	 *            bootstrap.
	 * @param handler A {@link URLStreamHandler} for the {@link FileSystemProvider}. There are a few additional
	 *            constraints:
	 *            <ul>
	 *            <li>All overridden methods must be visible to the given {@link Lookup}, as they will be invoked via a
	 *            {@link MethodHandle} found by that lookup.</li>
	 *            <li>If the stream handler overrides parseURL or setURL then they should also provide the method
	 *            {@link RedirectedUrlStreamHandler#quilt_passSetURL(java.lang.invoke.MethodHandle)}. (This is invoked
	 *            via reflection in all cases - the interface is provided for clarity)</li>
	 *            </ul>
	 * @param urlLookup A {@link Lookup} object which will be used instead of {@link MethodHandles#publicLookup()}
	 *            when {@link Lookup#findVirtual(Class, String, java.lang.invoke.MethodType) finding methods}. */
	void putFileSystemProvider(char letter, FileSystemProvider provider, URLStreamHandler handler, Lookup urlLookup);

	void addToClassPath(URL url);
}
