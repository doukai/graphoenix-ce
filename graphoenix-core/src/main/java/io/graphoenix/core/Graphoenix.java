package io.graphoenix.core;

import io.graphoenix.spi.bootstrap.Launcher;
import jakarta.enterprise.inject.spi.CDI;

public final class Graphoenix {
  private static final Launcher launcher = CDI.current().select(Launcher.class).get();

  public static Launcher launcher() {
    return launcher;
  }

  public static void run(String... args) {
    launcher.run(args);
  }
}
