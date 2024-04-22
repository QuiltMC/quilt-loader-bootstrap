package org.quiltmc.boot.urlhandler;

import java.lang.invoke.MethodHandle;
import java.net.URL;
import java.net.URLStreamHandler;

/** Optional interface for {@link URLStreamHandler}s to implement if they override parseURL or setURL. */
public interface RedirectedUrlStreamHandler {

	/** Invoked by the delegate before invocations of URLStreamHandler.parseURL or setURL. The given handler should be
	 * stored in a variable. It is not expected to change over the course of the program, so only setting it once is
	 * acceptable.
	 * 
	 * @param handle A method handle which should be invoked instead of invoking the parent method for setURL. This
	 *            expects the following arguments (identical to the larger setURL method):
	 *            <ol>
	 *            <li>The {@link URL} to modify.</li>
	 *            <li>{@link String} protocol name. Ignored by the URL method.</li>
	 *            <li>{@link String} remote host value for the URL.</li>
	 *            <li>int port ( {@link URL#getPort()} )</li>
	 *            <li>{@link String} authority ( {@link URL#getAuthority()} )</li>
	 *            <li>{@link String} userInfo ( {@link URL#getUserInfo()} )</li>
	 *            <li>{@link String} path ( {@link URL#getPath()} )</li>
	 *            <li>{@link String} query ( {@link URL#getQuery()} )</li>
	 *            <li>{@link String} reference ( {@link URL#getRef()} )</li>
	 *            </ol>
	 */
	void quilt_passSetURL(MethodHandle handle);

	/** Helper method which acts like URL.setURL.
	 * 
	 * @param handle The object passed to {@link #quilt_passSetURL(MethodHandle)}. */
	public static void invokeSetURL(MethodHandle handle, URL u, String protocol, String host, int port,
		String authority, String userInfo, String path, String query, String ref) {

		try {
			handle.invokeExact(u, protocol, host, port, authority, userInfo, path, query, ref);
		} catch (Throwable t) {
			throw DelegateUrlHandler.asUnchecked(t);
		}
	}
}
