package com.morley.myvideoplayer;

import java.io.File;
import java.io.IOException;

public interface MediaConfiguration {
    File start();
    char read(File file) throws IOException;
    void write(File file, String str) throws IOException;
}
