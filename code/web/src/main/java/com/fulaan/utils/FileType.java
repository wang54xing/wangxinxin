package com.fulaan.utils;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wang_xinxin on 2016/11/17.
 */
public enum FileType {
    //文档
    TXT(1,"txt"),
    //doc
    DOC(2,"doc"),
    DOCX(3,"docx"),
    //ppt
    PPT(4,"ppt"),
    PPTX(5,"pptx"),
    //XLS
    XLS(6,"xls"),
    XLSX(7,"xlsx"),
    //pdf
    PDF(8,"pdf"),

    //音视频
    MP3(9,"mp3"),
    AMR(27,"amr"),
    //video
    FLASH(10,"swf"),
    MP4(11,"mp4"),
    AVI(12,"avi"),
    FLV(13,"flv"),
    MKV(14,"mkv"),
    MOV(15,"mov"),
    MPG(16,"mpg"),
    THREE_GP(17,"3gp"),
    RMVB(18,"rmvb"),
    WMV(19,"wmv"),
    VOB(20,"vob"),
    //图片
    JPG(21,"jpg"),
    BMP(22,"bmp"),
    GIF(23,"gif"),
    PSD(24,"psd"),
    JPEG(25,"jpeg"),
    PNG(26,"png");
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

    /**
     *
     * @param type
     * @return
     */
    public static FileType getFileType(int type)
    {
        for(FileType ft: FileType.values())
        {
            if(ft.getType()==type)
            {
                return ft;
            }
        }
        return null;
    }

    /**
     *
     * @param fileType
     * @return
     */
    public static FileType getFileType(String fileType)
    {
        if(StringUtils.isNotBlank(fileType))
        {
            for(FileType ft: FileType.values())
            {
                if(ft.getName().equalsIgnoreCase(fileType))
                {
                    return ft;
                }
            }
        }

        return null;
    }

    /**
     * 文档
     * @return
     */
    public static List<String> getDocument() {
        List<String> document = new ArrayList<String>();
        int i=1;
        for (FileType ft : FileType.values()) {
            if (ft.getType() == i && i<9) {
                document.add(ft.getName());
                i++;
            }
        }
        return document;
    }

    /**
     * 音频
     * @return
     */
    public static List<String> getAudio() {
        List<String> audio = new ArrayList<String>();
        audio.add("mp3");
        audio.add("amr");
        return audio;
    }

    public static List<String> getVideo() {
        List<String> video = new ArrayList<String>();
        int i=10;
        for (FileType ft : FileType.values()) {
            if (ft.getType() == i && i<21) {
                video.add(ft.getName());
                i++;
            }
        }
        return video;
    }

    public static List<String> getPic() {
        List<String> pics = new ArrayList<String>();
        int i=21;
        for (FileType ft : FileType.values()) {
            if (ft.getType() == i && i<27) {
                pics.add(ft.getName());
                i++;
            }
        }
        return pics;
    }



}
