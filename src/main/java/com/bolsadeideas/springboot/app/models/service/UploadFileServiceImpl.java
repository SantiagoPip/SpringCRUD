package com.bolsadeideas.springboot.app.models.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.Resource;
@Service
public class UploadFileServiceImpl implements IUploadFileService{
	private final Logger log = LoggerFactory.getLogger(getClass());
	private final static String UPLOADS_FOLDER = "uploads";
	@Override
	public Resource load(String filename) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String copy(MultipartFile file) throws IOException {
		String  uniqueFileName = UUID.randomUUID().toString()+"_"+file.getOriginalFilename();
		Path rootPath = getPath(uniqueFileName);
	
		log.info("root Path"+ rootPath);
		log.info("rootAbsolutePath"+ rootPath);

		
//			byte[] bytes = foto.getBytes();
//			Path rutaCompleta = Paths.get(rootPath+"//"+foto.getOriginalFilename());
//			Files.write(rutaCompleta,bytes);
			Files.copy(file.getInputStream(), rootPath);
		
				return uniqueFileName;
	}

	@Override
	public boolean delete(String filename) {
		Path rootPath = getPath(filename);
		File archivo = rootPath.toFile();
		if(archivo.exists() && archivo.canRead()) {
			if(archivo.delete()) {
				return true;
			};
		}
		return false;
	}
	
	public Path getPath(String filename) {
		return Paths.get(UPLOADS_FOLDER).resolve(filename).toAbsolutePath();
	}

	@Override
	public void deleteAll() {
		FileSystemUtils.deleteRecursively(Paths.get(UPLOADS_FOLDER).toFile());
		
	}

	@Override
	public void init() throws IOException {
		// TODO Auto-generated method stub
		Files.createDirectories(Paths.get(UPLOADS_FOLDER));
	}

}
