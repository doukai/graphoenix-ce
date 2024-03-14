package io.graphoenix.spi.handler;

import io.graphoenix.spi.dto.PackageURL;

import java.util.Iterator;
import java.util.List;

public interface PackageProvider {

    List<PackageURL> getProtocolURLList(String packageName, String schema);

    Iterator<PackageURL> getProtocolURLIterator(String packageName, String schema);
}
