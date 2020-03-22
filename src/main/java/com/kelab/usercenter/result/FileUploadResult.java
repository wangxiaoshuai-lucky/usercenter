package com.kelab.usercenter.result;

public class FileUploadResult {

	private String filename;

	private String url;

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public FileUploadResult(String filename, String url) {
		this.filename = filename;
		this.url = url;
	}

	public FileUploadResult() {
	}
}
