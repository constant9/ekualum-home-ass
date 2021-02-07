package com.boot.demo.manipulator.processors.stamper;

import com.google.common.io.Resources;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Supplier;

@Configuration
public class StamperConfig {
	private static final Logger logger = LoggerFactory.getLogger(StamperConfig.class);

	@Value("${file.processors.stamper.pattern}")
	String timePattern;

	@Bean
	public Supplier<String> stamper(@Value("${file.processors.stamp.path:none}") String stampPath){
		Path path = Paths.get(stampPath);
		try {
			String stampTemplate;
			if (!Files.exists(path)) {
				logger.error("Stamp file {} does not exist! Using default stamp instead!", stampPath);
				URL url = Resources.getResource("default-stamp.txt");
				stampTemplate = Resources.toString(url, StandardCharsets.UTF_8);
			} else {
				stampTemplate = FileUtils.readFileToString(path.toFile(), StandardCharsets.UTF_8);
			}

			return () -> stampTemplate.replace("TIME_STAMP_PLACEHOLDER",
					LocalDateTime.now().format(DateTimeFormatter.ofPattern(timePattern)));

		}catch (IOException e){
			logger.error("error encountered while loading stamper file!", stampPath);
			throw new RuntimeException("Failed to load stamper",e);
		}
	}

}
