package com.pojo.app;

import org.apache.commons.lang.StringUtils;

/**
 * 文件类型
 * @author fourer
 *
 */
public enum FileType {

	TXT(1,"txt"),
	//doc
	DOC(2,"doc"),
	DOCX(3,"docx"),
	//ppt
	PPT(4,"ppt"),
	PPTX(18,"pptx"),
	//pdf
	PDF(5,"pdf"),
	//mp3
	MP3(6,"mp3"),
	//video
	FLASH(7,"swf"),
	MP4(8,"mp4"),
	AVI(9,"avi"),
	FLV(10,"flv"),
	MKV(11,"mkv"),
	MOV(12,"mov"),
	MPG(13,"mpg"),
	THREE_GP(14,"3gp"),
	RMVB(15,"rmvb"),
	WMV(16,"wmv"),
	VOB(17,"vob"),
	OTHER_VIDEO(100,"VIDEO"), //其他视频格式文件
	;
	public static final Integer[] PDF_TYPES=new Integer[]{DOC.getType(),DOCX.getType(),PPT.getType(),PPTX.getType(),PDF.getType()};
	
	
	
	
	private int type;
	private String name;
	
	
	
	private FileType(int type, String name) {
		this.type = type;
		this.name = name;
	}
	
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
	public static FileType getFileType(int type)
	{
		for(FileType ft:FileType.values())
		{
			if(ft.getType()==type)
			{
				return ft;
			}
		}
		return null;
	}
	
	public static FileType getFileType(String fileType)
	{
		if(StringUtils.isNotBlank(fileType))
		{
			for(FileType ft:FileType.values())
			{
				if(ft.getName().equalsIgnoreCase(fileType))
				{
					return ft;
				}
			}
		}
		
		return null;
	}
}
