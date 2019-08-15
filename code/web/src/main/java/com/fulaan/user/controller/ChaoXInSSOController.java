package com.fulaan.user.controller;

import com.db.user.UserDao;
import com.fulaan.annotation.SessionNeedless;
import com.fulaan.base.controller.BaseController;
import com.fulaan.cache.CacheHandler;
import com.fulaan.school.service.SchoolService;
import com.fulaan.user.service.UserService;
import com.pojo.app.SessionValue;
import com.pojo.school.SchoolEntry;
import com.pojo.user.UserEntry;
import com.pojo.utils.LoginLog;
import com.sys.constants.Constant;
import com.sys.exceptions.IllegalParamException;
import com.sys.exceptions.UnBindException;
import com.sys.exceptions.UnLoginException;
import com.sys.utils.DateTimeUtils;
import com.sys.utils.MD5Utils;
import com.sys.utils.RespObj;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/chaoxin")
public class ChaoXInSSOController extends BaseController {
	private static final Logger logger =Logger.getLogger(ChaoXInSSOController.class);
    private static final Logger loginLogger =Logger.getLogger("LOGIN");
    public static final String appkey_chaoxin = "4D18II5TQR5C15QJ";
    public static final String access_toke_url = "http://suzhi.jichu.chaoxing.com/front/appVerification";
    public static final String getUid_url = "http://suzhi.jichu.chaoxing.com/front/getUid";

    public UserDao userDao = new UserDao();

    private UserService userService = new UserService();
    @Autowired
	private SchoolService schoolService;


    public static final Map<Integer,String> type_url = new HashMap<Integer, String>();
    public static final Map<String,Integer> url_type = new HashMap<String, Integer>();


    static {
        type_url.put(1,"/elecGuid/evaSysMge/evaSysHome.do?type=chaoxin");
        type_url.put(2,"/zouban/studentXuanke.do?type=chaoxin");
        type_url.put(3,"/zouban/baseConfig.do?index=7&version=1&type=chaoxin");
        type_url.put(4,"/kw/index/indexPage.do?type=chaoxin");
        type_url.put(5,"/analyzePage/index.do?type=chaoxin");
        type_url.put(6,"/teaEvaMge/homePage.do?type=chaoxin");
        type_url.put(7,"/evalspecify/home.do?type=chaoxin");
        type_url.put(8,"/zouban/calculateTeacher/scoreEvaluation.do?type=chaoxin");
        type_url.put(9,"/lesson/teacher.do?version=1&index=3&type=chaoxin");
        type_url.put(10,"/dzmoredurank/home.do?domain=0&index=0&type=chaoxin");
        type_url.put(11,"/zouban/student.do?type=chaoxin");


        /*************************/
        url_type.put("/elecGuid/evaSysMge/evaSysHome.do?type=chaoxin",1);
        url_type.put("/zouban/studentXuanke.do?type=chaoxin",2);
        url_type.put("/zouban/baseConfig.do?index=7&version=1&type=chaoxin",3);
        url_type.put("/kw/index/indexPage.do?type=chaoxin",4);
        url_type.put("/analyzePage/index.do?type=chaoxin",5);
        url_type.put("/teaEvaMge/homePage.do?type=chaoxin",6);
        url_type.put("/evalspecify/home.do?type=chaoxin",7);
        url_type.put("/zouban/calculateTeacher/scoreEvaluation.do?type=chaoxin",8);
        url_type.put("/lesson/teacher.do?version=1&index=3&type=chaoxin",9);
        url_type.put("/dzmoredurank/home.do?domain=0&index=0&type=chaoxin",10);
        url_type.put("/zouban/student.do?type=chaoxin",11);
    }

    @SessionNeedless
    @RequestMapping("/sso/{param}/login")
    public String ssoLogin(HttpServletRequest request, HttpServletResponse response, String code_id,@PathVariable Integer param) throws UnLoginException, ClientProtocolException, IOException, IllegalParamException, UnBindException, ServletException
    {
//        System.out.println(param);
        if(StringUtils.isBlank(code_id))
        {
            throw new UnLoginException("un bind k6kt user!!!");
        }
        String str1 = null;
        try {
            JSONObject json1 = generateJson1(code_id);
            str1 = sendPost(access_toke_url,json1.toString());
            JSONObject access_token = new JSONObject(str1);
            if(access_token.getJSONObject("data")!=null&&access_token.getJSONObject("data").getString("access_token")!=null){
                String token = access_token.getJSONObject("data").getString("access_token");
                JSONObject json2 = generateJson2(token);
                String str2 = sendPost(getUid_url,json2.toString());
                JSONObject uid = new JSONObject(str2);
                if(uid.getJSONObject("data")!=null&&uid.getJSONObject("data").getString("uid")!=null){
                    String uidStr = uid.getJSONObject("data").getString("uid");
                    UserEntry userEntry = userDao.searchUserByUserBind(9,uidStr);
                    if(userEntry!=null){
                        login(userEntry,request, response);
                        return "redirect:"+request.getSession().getAttribute("cxurl");
                    }
                }
            }
            else{
                throw new UnLoginException("un bind k6kt user!!!");
            }

        } catch (Exception e) {
            throw new UnLoginException("un bind k6kt user!!!");
        }

        return null;
    }
    @SessionNeedless
    @RequestMapping("/sso/logout")
    public RespObj ssoLogout(HttpServletRequest request, HttpServletResponse response) throws UnLoginException, ClientProtocolException, IOException, IllegalParamException, UnBindException, ServletException
    {
        SessionValue sv =getSessionValue();
        if (null != sv) {

            String yearMonth=DateTimeUtils.convert(System.currentTimeMillis(), DateTimeUtils.DATE_YYYY_MM);
            CacheHandler.deleteKey(CacheHandler.CACHE_USER_CALENDAR,sv.getId(),yearMonth);

            logger.info("try loginout;the ui=" + sv.getId());
            logger.info("delete session value for user:" + sv.getId());

            Cookie cookies[] = request.getCookies();
            Cookie c = null;
            for (int i = 0; i < cookies.length; i++) {
                c = cookies[i];
                c.setMaxAge(0);
                if(c.getName().equals(Constant.COOKIE_USER_KEY))
                {
                    CacheHandler.deleteKey(CacheHandler.CACHE_SESSION_KEY,
                            c.getValue());
                }
            }
        }

        return RespObj.SUCCESS;
    }
    public void login(UserEntry e, HttpServletRequest request, HttpServletResponse response) throws UnLoginException {
        String ip = getIP(request);
        try
        {

            if(0l==e.getLastActiveDate()){
                String params=e.getID().toString()+0l;
                String flkey= CacheHandler.getKeyString(CacheHandler.CACHE_USER_FIRST_LOGIN,params);
                CacheHandler.cache(flkey,Constant.USER_FIRST_LOGIN, Constant.SESSION_FIVE_MINUTE);
            }
            //更新最后登录日期
            userService.updateLastActiveDate(e.getID());
        }catch(Exception ex)
        {
            logger.error("",ex);
        }
        if(Constant.ONE==e.getUserType()) //有效时间用户
        {
            long validBeginTime =e.getValidBeginTime();
            long validTime=e.getValidTime();
            if(0L==validBeginTime) //第一次登陆
            {
                try {
                    userService.update(e.getID(), "vabt", System.currentTimeMillis());
                } catch (IllegalParamException e1) {

                };
            }
            else
            {
                if(System.currentTimeMillis()>validBeginTime+validTime*1000)
                {
                    throw new UnLoginException("该用户已经失效");
                }
            }
        }
        Map<String,String> schoolMap = new HashMap<String,String>();
		SchoolEntry schoolEntry = null;
        try {
			schoolEntry = schoolService.getSchoolEntryByUserId(e.getID());
            
           if(schoolEntry!=null) {
        	   schoolMap.put("id",schoolEntry.getID().toString());
               schoolMap.put("name",schoolEntry.getName());
               schoolMap.put("logo",schoolEntry.getLogo());
        	   
           }
            

        }
        catch (Exception ie){
            logger.error("Can not find school for user:"+e);
        }

        //处理SessionValue
        SessionValue value =new SessionValue();
        value.setId(e.getID().toString());
        value.setUserName(e.getUserName());
        value.setRealName(e.getNickName());
        value.setSex(e.getSex());
        value.setK6kt(e.getK6KT());
        if(e.getK6KT() == 1) {//k6kt用户
            value.setSchoolId(e.getSchoolID().toString());
            value.setSchoolLogo(schoolMap.get("logo"));
            value.setUserRole(e.getRole());
            value.setAvatar(e.getAvatar());
            value.setUserPermission(e.getPermission());
            value.setUserRemovePermission(e.getRemovePermission());
            value.setExperience(e.getExperiencevalue());
            value.setChatid(e.getChatId());
//			value.setSchoolNavs(schoolEntry.getSchoolNavs());
            value.setSchoolName(schoolMap.get("name"));
            value.setCoupon(e.getCoupon());
        }

        request.setAttribute(BaseController.SESSION_VALUE, value);

        //放入缓存
        ObjectId cacheUserKey =new ObjectId();
        //ck_key
        CacheHandler.cacheUserKey(e.getID().toString(), cacheUserKey.toString(), Constant.SECONDS_IN_DAY);
        //s_key
        CacheHandler.cacheSessionValue(cacheUserKey.toString(), value, Constant.SECONDS_IN_DAY);
        //处理cookie
        Cookie userKeycookie = new Cookie(Constant.COOKIE_USER_KEY,cacheUserKey.toString());
        userKeycookie.setMaxAge(Constant.SECONDS_IN_DAY);
        userKeycookie.setPath(Constant.BASE_PATH);
        response.addCookie(userKeycookie);
        //存储用户key和IP的对应关系,加强sso安全
        String ipKey=CacheHandler.getKeyString(CacheHandler.CACHE_USER_KEY_IP, cacheUserKey.toString());
        CacheHandler.cache(ipKey, ip, Constant.SECONDS_IN_DAY);
        Cookie couCookie = new Cookie("coupon","false");
        couCookie.setMaxAge(Constant.SECONDS_IN_MONTH);
        couCookie.setPath(Constant.BASE_PATH);
        response.addCookie(couCookie);

        try {
            Cookie nameCookie = new Cookie(Constant.COOKIE_USERNAME_KEY,URLEncoder.encode(e.getUserName(), Constant.UTF_8));
            nameCookie.setMaxAge(Constant.SECONDS_IN_MONTH);
            nameCookie.setPath(Constant.BASE_PATH);
            response.addCookie(nameCookie);
        } catch (UnsupportedEncodingException e1) {
            logger.error("", e1);
        }
        try
        {

//			RegionEntry region=schoolService.getRegionEntry(dto.getRegionId());
            //获取客户端信息
            LoginLog loginLog =new LoginLog();
            loginLog.setIpAddr(ip+e.getUserName());
//            loginLog.setPlatform(pf.getName());
            loginLog.setUserId(e.getID().toString());
            loginLog.setUserName(e.getUserName());
            if(e.getK6KT() == 1) {//k6kt用户
                loginLog.setRole(e.getRole());
                loginLog.setSchoolId(schoolMap.get("id"));
                loginLog.setSchoolName(schoolMap.get("name"));
//				loginLog.setCity(region.getName());
            }

        }catch(Exception ex)
        {
        }

    }

    public JSONObject generateJson1(String code_id){
        JSONObject json1 = new JSONObject();
        JSONObject headjson = new JSONObject();
        JSONObject reqheadjson = new JSONObject();
        JSONObject datajson = new JSONObject();

        try {
            headjson.put("mfid",101);
            headjson.put("tokenid",101203);
            headjson.put("encp",0);
            headjson.put("encpdata","");

            reqheadjson.put("uid","12345678");
            reqheadjson.put("fid","87654321");
            reqheadjson.put("ukey","890489028&*(&#(djfljk");
            reqheadjson.put("msgid","666");
            reqheadjson.put("serial","12345678");
            reqheadjson.put("datestamp","20171103110101");

            datajson.put("client_secret",appkey_chaoxin);
            datajson.put("code_id",code_id);
            String enc = MD5Utils.md5(code_id+new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
            datajson.put("enc",enc);

            json1.put("head",headjson);
            json1.put("reqhead",reqheadjson);
            json1.put("data",datajson);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json1;
    }

    public JSONObject generateJson2(String access_token){
        JSONObject json1 = new JSONObject();
        JSONObject headjson = new JSONObject();
        JSONObject reqheadjson = new JSONObject();
        JSONObject datajson = new JSONObject();

        try {
            headjson.put("mfid",101);
            headjson.put("tokenid",101203);
            headjson.put("encp",0);
            headjson.put("encpdata","");

            reqheadjson.put("uid","12345678");
            reqheadjson.put("fid","87654321");
            reqheadjson.put("ukey","890489028&*(&#(djfljk");
            reqheadjson.put("msgid","666");
            reqheadjson.put("serial","12345678");
            reqheadjson.put("datestamp","20171103110101");

            datajson.put("client_secret",appkey_chaoxin);
            String enc = MD5Utils.md5(access_token+new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
            datajson.put("enc",enc);
            datajson.put("access_token",access_token);

            json1.put("head",headjson);
            json1.put("reqhead",reqheadjson);
            json1.put("data",datajson);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json1;
    }
    //post请求方法
    public  String sendPost(String url, String data) {
        String response = null;

        try {
            CloseableHttpClient httpclient = null;
            CloseableHttpResponse httpresponse = null;
            try {
                httpclient = HttpClients.createDefault();
                HttpPost httppost = new HttpPost(url);
                StringEntity stringentity = new StringEntity(data,
                        ContentType.create("text/json", "UTF-8"));
                httppost.setEntity(stringentity);
                httpresponse = httpclient.execute(httppost);
                response = EntityUtils
                        .toString(httpresponse.getEntity());
            } finally {
                if (httpclient != null) {
                    httpclient.close();
                }
                if (httpresponse != null) {
                    httpresponse.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    private String getIP(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
	

}
