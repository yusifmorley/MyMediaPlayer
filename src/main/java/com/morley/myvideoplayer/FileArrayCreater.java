package com.morley.myvideoplayer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedList;

public interface FileArrayCreater {
    LinkedList<Path> createFileObjectArray() throws IOException;

}
