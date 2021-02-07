package com.boot.demo.manipulator.processors.stamper;

import com.boot.demo.manipulator.processors.FileProcessor;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.function.Supplier;

@Component
public class StamperProcessor implements FileProcessor {
	private static final Logger logger = LoggerFactory.getLogger(StamperProcessor.class);


	@Autowired
	private Supplier<String> stamper;

	@Override
	public void process(File file) throws IOException {
		if(!file.exists()){
			logger.warn("File {} doesn`t exist.", file.toString());
		}

		String stamp = stamper.get();
		FileUtils.writeStringToFile(
				file, stamp, StandardCharsets.UTF_8, true);
	}
}
