package com.boot.demo.manipulator.crawlers;

import com.boot.demo.manipulator.processors.FileProcessor;
import lombok.var;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;


public class LocalFilesCrawler {
	private static final Logger logger = LoggerFactory.getLogger(LocalFilesCrawler.class);

	@Autowired
	FileProcessor fileProcessor;

	@Value("${file.processor.extension:txt}")
	String fileExtension;

	private static final String errorMessage = "data preparation failed for folder ";
	private static final String successMessage = "data preparation succeeds for folder %s. Marked %s files.";

	ExecutorService folderDiscoveryWorkers = Executors.newFixedThreadPool(1);


	public void crawl(String folderPath){
		Path path = Paths.get(folderPath);
		if (!Files.exists(path)) {
			System.out.println(errorMessage + folderPath);
			logger.error("Path {} doesn`t exist.", folderPath);
			return;
		}

		try {
			Files.list(path)
					.filter(Files::isDirectory)
					.forEach(dir ->
							folderDiscoveryWorkers.submit(()-> crawl(dir.toString())));

			var successfulFilesCounter = new AtomicInteger();
			Files.list(path)
					.filter(Files::isRegularFile)
					.filter(f -> fileExtension.equals(FilenameUtils.getExtension(f.toString())))
					.map(Path::toFile)
					.forEach(file ->
							{
								try {
									fileProcessor.process(file);
									successfulFilesCounter.incrementAndGet();
								} catch (Exception e) {
									logger.error("Failed to process file " + file.toString());
								}
							});
			if(successfulFilesCounter.get() > 0){
				System.out.println(String.format(successMessage, path.toString(), successfulFilesCounter.get()));
			}

		} catch (IOException e) {
			System.out.println(errorMessage + folderPath);
			logger.error("Failed to scan folder: ",e.getMessage());
		}
	}
}
