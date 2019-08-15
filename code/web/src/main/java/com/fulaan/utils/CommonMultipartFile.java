package com.fulaan.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;

public class CommonMultipartFile implements MultipartFile {

	private File f;
	public CommonMultipartFile(File f)
	{
		this.f=f;
	}
	
	@Override
	public String getName() {
	  return f.getName();
	}

	@Override
	public String getOriginalFilename() {
		 return f.getName();
	}

	@Override
	public String getContentType() {
		return null;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public long getSize() {
		return this.f.length();
	}

	@Override
	public byte[] getBytes() throws IOException {
		return null;
	}

	@Override
	public InputStream getInputStream() throws IOException {
		return new FileInputStream(this.f);
	}

	@Override
	public void transferTo(File dest) throws IOException, IllegalStateException {

	}

}
