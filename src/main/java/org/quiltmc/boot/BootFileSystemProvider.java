package org.quiltmc.boot;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.FileChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.AccessMode;
import java.nio.file.CopyOption;
import java.nio.file.DirectoryStream;
import java.nio.file.DirectoryStream.Filter;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileAttributeView;
import java.nio.file.spi.FileSystemProvider;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;

public abstract class BootFileSystemProvider extends FileSystemProvider {

    final char letter;
    final String scheme;

    private BootFileSystemProvider(char letter) {
        this.letter = letter;
        this.scheme = "quilt." + letter + "fs";
    }

    FileSystemProvider getDelegate() {
        return Bootstrap.instance().currentLaunch().getFileSystemProvider(letter);
    }

    @Override
    public String getScheme() {
        return scheme;
    }

    @Override
    public FileSystem newFileSystem(URI uri, Map<String, ?> env) throws IOException {
        return getDelegate().newFileSystem(uri, env);
    }

    @Override
    public FileSystem getFileSystem(URI uri) {
        FileSystemProvider fsp = getDelegate();
        if (fsp == null) {
            return null;
        } else {
            return fsp.getFileSystem(uri);
        }
    }

    @Override
    public String toString() {
        FileSystemProvider fsp = getDelegate();
        if (fsp == null) {
            return getClass().getName();
        } else {
            return fsp.toString();
        }
    }

    @Override
    public Path getPath(URI uri) {
        FileSystemProvider fsp = getDelegate();
        if (fsp == null) {
            throw new IllegalStateException("No delegate - we can't convert " + uri + " into a path!");
        } else {
            return fsp.getPath(uri);
        }
    }

    private FileSystemProvider delegateOrThrowIO() throws IOException {
        FileSystemProvider fsp = getDelegate();
        if (fsp != null) {
            return fsp;
        }
        throw new IOException("Cannot interact with " + getScheme() + " as the delegate file system has not been set!");
    }

    @Override
    public FileSystem newFileSystem(Path path, Map<String, ?> env) throws IOException {
        return delegateOrThrowIO().newFileSystem(path, env);
    }

    @Override
    public InputStream newInputStream(Path path, OpenOption... options) throws IOException {
        return delegateOrThrowIO().newInputStream(path, options);
    }

    @Override
    public OutputStream newOutputStream(Path path, OpenOption... options) throws IOException {
        return delegateOrThrowIO().newOutputStream(path, options);
    }

    @Override
    public FileChannel newFileChannel(Path path, Set<? extends OpenOption> options, FileAttribute<?>... attrs)
        throws IOException {
        return delegateOrThrowIO().newFileChannel(path, options, attrs);
    }

    @Override
    public AsynchronousFileChannel newAsynchronousFileChannel(
        Path path, Set<? extends OpenOption> options, ExecutorService executor, FileAttribute<?>... attrs
    ) throws IOException {

        return delegateOrThrowIO().newAsynchronousFileChannel(path, options, executor, attrs);
    }

    @Override
    public SeekableByteChannel newByteChannel(Path path, Set<? extends OpenOption> options, FileAttribute<?>... attrs)
        throws IOException {

        return delegateOrThrowIO().newByteChannel(path, options, attrs);
    }

    @Override
    public DirectoryStream<Path> newDirectoryStream(Path dir, Filter<? super Path> filter) throws IOException {
        return delegateOrThrowIO().newDirectoryStream(dir, filter);
    }

    @Override
    public void createDirectory(Path dir, FileAttribute<?>... attrs) throws IOException {
        delegateOrThrowIO().createDirectory(dir, attrs);
    }

    @Override
    public void createSymbolicLink(Path link, Path target, FileAttribute<?>... attrs) throws IOException {
        delegateOrThrowIO().createSymbolicLink(link, target, attrs);
    }

    @Override
    public void createLink(Path link, Path existing) throws IOException {
        delegateOrThrowIO().createLink(link, existing);
    }

    @Override
    public void delete(Path path) throws IOException {
        delegateOrThrowIO().delete(path);
    }

    @Override
    public boolean deleteIfExists(Path path) throws IOException {
        return delegateOrThrowIO().deleteIfExists(path);
    }

    @Override
    public Path readSymbolicLink(Path link) throws IOException {
        return delegateOrThrowIO().readSymbolicLink(link);
    }

    @Override
    public void copy(Path source, Path target, CopyOption... options) throws IOException {
        delegateOrThrowIO().copy(source, target, options);
    }

    @Override
    public void move(Path source, Path target, CopyOption... options) throws IOException {
        delegateOrThrowIO().move(source, target, options);
    }

    @Override
    public boolean isSameFile(Path path, Path path2) throws IOException {
        return delegateOrThrowIO().isSameFile(path, path2);
    }

    @Override
    public boolean isHidden(Path path) throws IOException {
        return delegateOrThrowIO().isHidden(path);
    }

    @Override
    public FileStore getFileStore(Path path) throws IOException {
        return delegateOrThrowIO().getFileStore(path);
    }

    @Override
    public void checkAccess(Path path, AccessMode... modes) throws IOException {
        delegateOrThrowIO().checkAccess(path, modes);
    }

    @Override
    public <V extends FileAttributeView> V getFileAttributeView(Path path, Class<V> type, LinkOption... options) {

        FileSystemProvider fsp = getDelegate();
        if (fsp != null) {
            return fsp.getFileAttributeView(path, type, options);
        }
        throw new IllegalStateException("Cannot interact with " + getScheme() + " as the delegate file system has not been set!");
    }

    @Override
    public <A extends BasicFileAttributes> A readAttributes(Path path, Class<A> type, LinkOption... options)
        throws IOException {

        return delegateOrThrowIO().readAttributes(path, type, options);
    }

    @Override
    public Map<String, Object> readAttributes(Path path, String attributes, LinkOption... options) throws IOException {
        return delegateOrThrowIO().readAttributes(path, attributes, options);
    }

    @Override
    public void setAttribute(Path path, String attribute, Object value, LinkOption... options) throws IOException {
        delegateOrThrowIO().setAttribute(path, attribute, value, options);
    }

    public static class BootFileSystemProviderA extends BootFileSystemProvider {
        public BootFileSystemProviderA() {
            super('a');
        }
    }

    public static class BootFileSystemProviderB extends BootFileSystemProvider {
        public BootFileSystemProviderB() {
            super('b');
        }
    }

    public static class BootFileSystemProviderC extends BootFileSystemProvider {
        public BootFileSystemProviderC() {
            super('c');
        }
    }

    public static class BootFileSystemProviderD extends BootFileSystemProvider {
        public BootFileSystemProviderD() {
            super('d');
        }
    }

    public static class BootFileSystemProviderE extends BootFileSystemProvider {
        public BootFileSystemProviderE() {
            super('e');
        }
    }

    public static class BootFileSystemProviderF extends BootFileSystemProvider {
        public BootFileSystemProviderF() {
            super('f');
        }
    }

    public static class BootFileSystemProviderG extends BootFileSystemProvider {
        public BootFileSystemProviderG() {
            super('g');
        }
    }

    public static class BootFileSystemProviderH extends BootFileSystemProvider {
        public BootFileSystemProviderH() {
            super('h');
        }
    }

    public static class BootFileSystemProviderI extends BootFileSystemProvider {
        public BootFileSystemProviderI() {
            super('i');
        }
    }

    public static class BootFileSystemProviderJ extends BootFileSystemProvider {
        public BootFileSystemProviderJ() {
            super('j');
        }
    }

    public static class BootFileSystemProviderK extends BootFileSystemProvider {
        public BootFileSystemProviderK() {
            super('k');
        }
    }

    public static class BootFileSystemProviderL extends BootFileSystemProvider {
        public BootFileSystemProviderL() {
            super('l');
        }
    }

    public static class BootFileSystemProviderM extends BootFileSystemProvider {
        public BootFileSystemProviderM() {
            super('m');
        }
    }

    public static class BootFileSystemProviderN extends BootFileSystemProvider {
        public BootFileSystemProviderN() {
            super('n');
        }
    }

    public static class BootFileSystemProviderO extends BootFileSystemProvider {
        public BootFileSystemProviderO() {
            super('o');
        }
    }

    public static class BootFileSystemProviderP extends BootFileSystemProvider {
        public BootFileSystemProviderP() {
            super('p');
        }
    }

    public static class BootFileSystemProviderQ extends BootFileSystemProvider {
        public BootFileSystemProviderQ() {
            super('q');
        }
    }

    public static class BootFileSystemProviderR extends BootFileSystemProvider {
        public BootFileSystemProviderR() {
            super('r');
        }
    }

    public static class BootFileSystemProviderS extends BootFileSystemProvider {
        public BootFileSystemProviderS() {
            super('s');
        }
    }

    public static class BootFileSystemProviderT extends BootFileSystemProvider {
        public BootFileSystemProviderT() {
            super('t');
        }
    }

    public static class BootFileSystemProviderU extends BootFileSystemProvider {
        public BootFileSystemProviderU() {
            super('u');
        }
    }

    public static class BootFileSystemProviderV extends BootFileSystemProvider {
        public BootFileSystemProviderV() {
            super('v');
        }
    }

    public static class BootFileSystemProviderW extends BootFileSystemProvider {
        public BootFileSystemProviderW() {
            super('w');
        }
    }

    public static class BootFileSystemProviderX extends BootFileSystemProvider {
        public BootFileSystemProviderX() {
            super('x');
        }
    }

    public static class BootFileSystemProviderY extends BootFileSystemProvider {
        public BootFileSystemProviderY() {
            super('y');
        }
    }

    public static class BootFileSystemProviderZ extends BootFileSystemProvider {
        public BootFileSystemProviderZ() {
            super('z');
        }
    }
}
