package com.github.andrewlalis.running_every_day;

import java.awt.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public final class Resources {
    private Resources() {}

    public static String readResourceAsString(String name) throws IOException {
        try (var in = Resources.class.getClassLoader().getResourceAsStream(name)) {
            if (in == null) {
                throw new IOException("Missing resource: " + name);
            }
            return new String(in.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    public static Font readTTFFont(String name) throws IOException {
        try (var in = Resources.class.getClassLoader().getResourceAsStream(name)) {
            if (in == null) {
                throw new IOException("Missing resource: " + name);
            }
            try {
                return Font.createFont(Font.TRUETYPE_FONT, in);
            } catch (FontFormatException e) {
                throw new IOException(e);
            }
        }
    }
}
