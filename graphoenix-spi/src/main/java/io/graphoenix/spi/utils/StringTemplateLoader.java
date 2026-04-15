package io.graphoenix.spi.utils;

import org.jspecify.annotations.NonNull;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;
import org.stringtemplate.v4.STGroupString;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public final class StringTemplateLoader {

  private static final Map<String, String> TEMPLATE_SOURCE_CACHE = new ConcurrentHashMap<>();

  private StringTemplateLoader() {}

  public static STGroup load(String resourcePath) {
    URL resourceUrl = StringTemplateLoader.class.getClassLoader().getResource(resourcePath);
    if (resourceUrl == null) {
      throw new IllegalStateException("StringTemplate resource not found: " + resourcePath);
    }
    try {
      return new STGroupFile(resourceUrl, StandardCharsets.UTF_8.displayName(), '<', '>');
    } catch (RuntimeException e) {
      return new STGroupString(resourcePath, loadTemplateSource(resourcePath));
    }
  }

  private static String loadTemplateSource(String resourcePath) {
    return TEMPLATE_SOURCE_CACHE.computeIfAbsent(
        resourcePath, StringTemplateLoader::readTemplateSource);
  }

  private static String readTemplateSource(String resourcePath) {
    URL resourceUrl = StringTemplateLoader.class.getClassLoader().getResource(resourcePath);
    if (resourceUrl == null) {
      throw new IllegalStateException("StringTemplate resource not found: " + resourcePath);
    }
    try (InputStream inputStream = openResourceStream(resourceUrl, resourcePath)) {
      return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
    } catch (IOException e) {
      throw new UncheckedIOException("Unable to load StringTemplate resource: " + resourcePath, e);
    }
  }

  private static InputStream openResourceStream(URL resourceUrl, String resourcePath)
      throws IOException {
    if (!"jar".equals(resourceUrl.getProtocol())) {
      return resourceUrl.openStream();
    }

    String resource = resourceUrl.toExternalForm();
    int separatorIndex = resource.indexOf("!/");
    if (separatorIndex < 0) {
      throw new IOException("Invalid jar resource URL: " + resource);
    }

    String jarUriString = resource.substring(4, separatorIndex);
    String jarEntryName = resource.substring(separatorIndex + 2);

    try {
      JarFile jarFile = new JarFile(Paths.get(new URI(jarUriString)).toFile());
      JarEntry jarEntry = jarFile.getJarEntry(jarEntryName);
      if (jarEntry == null) {
        jarFile.close();
        throw new IllegalStateException(
            "StringTemplate resource not found in jar: " + resourcePath);
      }
      InputStream entryStream = jarFile.getInputStream(jarEntry);
      return new JarFileInputStream(jarFile, entryStream);
    } catch (URISyntaxException e) {
      throw new IOException("Invalid jar URI: " + jarUriString, e);
    }
  }

  private static final class JarFileInputStream extends InputStream {

    private final JarFile jarFile;
    private final InputStream delegate;

    private JarFileInputStream(JarFile jarFile, InputStream delegate) {
      this.jarFile = jarFile;
      this.delegate = delegate;
    }

    @Override
    public int read() throws IOException {
      return delegate.read();
    }

    @Override
    public int read(byte @NonNull [] b) throws IOException {
      return delegate.read(b);
    }

    @Override
    public int read(byte @NonNull [] b, int off, int len) throws IOException {
      return delegate.read(b, off, len);
    }

    @Override
    public void close() throws IOException {
      try {
        delegate.close();
      } finally {
        jarFile.close();
      }
    }

    @Override
    public int available() throws IOException {
      return delegate.available();
    }

    @Override
    public long skip(long n) throws IOException {
      return delegate.skip(n);
    }

    @Override
    public synchronized void mark(int readlimit) {
      delegate.mark(readlimit);
    }

    @Override
    public synchronized void reset() throws IOException {
      delegate.reset();
    }

    @Override
    public boolean markSupported() {
      return delegate.markSupported();
    }
  }
}
