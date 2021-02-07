package com.boot.demo.manipulator.processors;

import java.io.File;
import java.io.IOException;

public interface FileProcessor {
	void process(File file) throws IOException;
}
