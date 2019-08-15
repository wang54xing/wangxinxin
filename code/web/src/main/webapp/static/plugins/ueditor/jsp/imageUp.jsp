<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ page import="com.fulaan.utils.Uploader" %>
<%@ page import="java.io.File" %>
<%@ page import="java.io.FileInputStream" %>
<%@ page import="java.util.*" %>
<%--没见过这么烂的代码  在jsp里面写文件上传  写个servlet不好吗--%>
<%
    request.setCharacterEncoding(Uploader.ENCODEING);
    response.setCharacterEncoding(Uploader.ENCODEING);

    String currentPath = request.getRequestURI().replace(request.getContextPath(), "");

    File currentFile = new File(currentPath);

    currentPath = currentFile.getParent() + File.separator;

//                File propertyFile = new File(currentFile.getParent(), "config.properties");
    String imgUpload=request.getParameter("imgUpload");
    if(imgUpload!=null&&"/upload/".equals(imgUpload)){
        imgUpload+="ueditor/";
    }
//加载配置文件
    String propertiesPath = request.getSession().getServletContext().getRealPath("/static/plugins/ueditor/jsp/config.properties");
    Properties properties = new Properties();

    try {
        properties.load(new FileInputStream(propertiesPath));
    } catch (Exception e) {
        //加载失败的处理
        e.printStackTrace();
    }

    List<String> savePath = Arrays.asList(properties.getProperty("savePath").split(","));


    //获取存储目录结构
    if (request.getParameter("fetch") != null) {

        response.setHeader("Content-Type", "text/javascript");

        //构造json数据
        Iterator<String> iterator = savePath.iterator();

        String dirs = "[";
        while (iterator.hasNext()) {

            dirs += "'" + iterator.next() + "'";

            if (iterator.hasNext()) {
                dirs += ",";
            }

        }
        dirs += "]";
        response.getWriter().print("updateSavePath( " + dirs + " );");
        return;

    }

    Uploader up = new Uploader(request);

// 获取前端提交的path路径
    String dir = request.getParameter("dir");


//普通请求中拿不到参数， 则从上传表单中拿
    if (dir == null) {
        dir = up.getParameter("dir");
    }

    if (dir == null || "".equals(dir)) {

        //赋予默认值
        dir = savePath.get(0);

        //安全验证
    } else if (!savePath.contains(dir)) {

        response.getWriter().print("{'state':'\\u975e\\u6cd5\\u4e0a\\u4f20\\u76ee\\u5f55'}");
        return;

    }
    if(imgUpload!=null&&"/upload/ueditor/".equals(imgUpload)){
        up.setSavePath(imgUpload+dir);
    }else{
        up.setSavePath(dir);
    }

    String[] fileType = {".gif", ".png", ".jpg", ".jpeg", ".bmp"};
    up.setAllowFiles(fileType);
    up.setMaxSize(500 * 1024); //单位KB
    up.upload();
    response.getWriter().print("{'original':'" + up.getOriginalName() + "','url':'" + up.getUrl() + "','title':'" + up.getTitle() + "','state':'" + up.getState() + "'}");
%>
