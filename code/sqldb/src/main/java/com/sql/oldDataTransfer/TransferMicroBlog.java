package com.sql.oldDataTransfer;

import com.db.microblog.MicroBlogDao;
import com.pojo.app.IdValuePair;
import com.pojo.app.Platform;
import com.pojo.microblog.MicroBlogEntry;
import com.pojo.microblog.MicroBlogImage;
import com.sql.dao.RefactorMapper;
import com.sql.oldDataPojo.BlogPicInfo;
import com.sql.oldDataPojo.MicroBlogInfo;
import com.sql.oldDataPojo.MicroBlogZanLogsInfo;
import com.sql.oldDataPojo.ReplyCommentInfo;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by guojing on 2015/4/1.
 */
public class TransferMicroBlog {
    private SqlSessionFactory getSessionFactory() {
        SqlSessionFactory sessionFactory = null;
        String resource = "configuration.xml";
        try {
            sessionFactory = new SqlSessionFactoryBuilder().build(Resources.getResourceAsReader(resource));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sessionFactory;
    }

    private RefactorMapper getRefactorMapper(){
        SqlSession sqlSession = getSessionFactory().openSession();
        RefactorMapper refactorMapper = sqlSession.getMapper(RefactorMapper.class);
        return refactorMapper;
    }

    MicroBlogDao microBlogDao=new MicroBlogDao();

    public static Map<Integer,ObjectId> blogIdMap=new HashMap<Integer, ObjectId>();

    public static Map<Integer,ObjectId> blogImageMap=new HashMap<Integer, ObjectId>();

    private List<MicroBlogInfo> blogInfos = null;
    private List<ReplyCommentInfo> replyInfos = null;
    private List<MicroBlogZanLogsInfo> zanLogInfos = null;
    private List<BlogPicInfo> blogPicInfos = null;

    public void transferMicroBlogInfo(Map<Integer,ObjectId> userMap,Map<Integer,ObjectId> schoolMap){
        blogInfos =  getRefactorMapper().getMicroBlogInfo();
        replyInfos =  getRefactorMapper().getReplyCommentInfo();
        zanLogInfos =  getRefactorMapper().getMicroBlogZanLogsInfo();
        blogPicInfos =  getRefactorMapper().getBlogPicInfo();
        for(MicroBlogInfo microBlogInfo  :blogInfos) {
            int id=microBlogInfo.getId();
            List<ReplyCommentInfo> childReplys=new ArrayList<ReplyCommentInfo>();
            for(ReplyCommentInfo replyCommentInfo  :replyInfos) {
                if(id==replyCommentInfo.getBlogid()){
                    childReplys.add(replyCommentInfo);
                }
            }


            List<MicroBlogZanLogsInfo> childZans=new ArrayList<MicroBlogZanLogsInfo>();
            for(MicroBlogZanLogsInfo microBlogZanLogsInfo  :zanLogInfos) {
                if(id==microBlogZanLogsInfo.getBlogid()){
                    childZans.add(microBlogZanLogsInfo);
                }
            }

            List<BlogPicInfo> childPics=new ArrayList<BlogPicInfo>();
            for(BlogPicInfo blogPicInfo  :blogPicInfos) {
                if(id==blogPicInfo.getBlogid()){
                    childPics.add(blogPicInfo);
                }
            }

            dealZonghe(microBlogInfo,childReplys,childZans,childPics,userMap,schoolMap);

        }

        System.out.println("UserPetInf size:"+blogInfos.size());

    }

    private void dealZonghe(MicroBlogInfo microBlogInfo, List<ReplyCommentInfo> childReplys,
                            List<MicroBlogZanLogsInfo> childZans, List<BlogPicInfo> blogPics,
                            Map<Integer, ObjectId> userMap,Map<Integer,ObjectId> schoolMap) {

        List<MicroBlogImage> imageList=new ArrayList<MicroBlogImage>();
        for(BlogPicInfo blogPic:blogPics){
            MicroBlogImage microBlogImage=new MicroBlogImage(blogPic.getPathname());


            imageList.add(microBlogImage);
        }
        ObjectId userid=userMap.get(microBlogInfo.getUserid())==null?null:userMap.get(microBlogInfo.getUserid());
        if(userid!=null){
            String clienttype=microBlogInfo.getClienttype()==null?"":microBlogInfo.getClienttype();
            ObjectId blogId=new ObjectId(microBlogInfo.getCreatetime());
            List<MicroBlogEntry> list=new ArrayList<MicroBlogEntry>();
            Map<Integer,ObjectId> replyIdMap=new HashMap<Integer, ObjectId>();
            List<ObjectId> replyIds=new ArrayList<ObjectId>();
            for(ReplyCommentInfo replyCommentInfo:childReplys){
                    ObjectId replyId=new ObjectId(replyCommentInfo.getCreatetime());
                replyIds.add(replyId);
                replyIdMap.put(replyCommentInfo.getId(),replyId);
            }
            Map<Integer,List<ObjectId>> commidMap=new HashMap<Integer, List<ObjectId>>();
            for(Integer key : replyIdMap.keySet())
            {
                List<ObjectId> tempId=new ArrayList<ObjectId>();
                for(ReplyCommentInfo replyCommentInfo:childReplys){
                    if(replyCommentInfo.getCommentid()==key){
                        tempId.add(replyIdMap.get(replyCommentInfo.getId()));
                    }
                }
                commidMap.put(key,tempId);
            }


            for(ReplyCommentInfo replyCommentInfo:childReplys){

                ObjectId ruserid=userMap.get(replyCommentInfo.getUserid())==null?null:userMap.get(replyCommentInfo.getUserid());
                ObjectId buserid=userMap.get(replyCommentInfo.getBuserid())==null?null:userMap.get(replyCommentInfo.getBuserid());
                ObjectId schoolId=schoolMap.get(replyCommentInfo.getSchoolId());
                List<ObjectId> rzanUserIds=new ArrayList<ObjectId>();
                for(MicroBlogZanLogsInfo zanLogsInfo:childZans){
                    if(zanLogsInfo.getCommentid()==replyCommentInfo.getId()){
                        ObjectId rzanUserId=userMap.get(zanLogsInfo.getUserid())==null?null:userMap.get(zanLogsInfo.getUserid());
                        rzanUserIds.add(rzanUserId);
                    }
                }
                
                IdValuePair atInfo=new IdValuePair(buserid,replyCommentInfo.getNickname());
//
//                MicroBlogEntry microBlogEntry=new MicroBlogEntry(
//                        ruserid,
//                        2,
//                        replyCommentInfo.getDelflg(),
//                        replyCommentInfo.getCommentcontent(),
//                        Platform.PC,
//                        replyCommentInfo.getCreatetime().getTime(),
//                        replyCommentInfo.getCreatetime().getTime(),
//                        0,
//                        0,
//                        0,
//                        replyCommentInfo.getZancount(),
//                        blogId,
//                        replyCommentInfo.getIsread(),
//                        schoolId,
//                        commidMap.get(replyCommentInfo.getId()),
//                        new ArrayList<MicroBlogImage>(),
//                        rzanUserIds,
//                        atInfo,
//                        new ArrayList<ObjectId>(),
//                        1
//                        );
              //  microBlogEntry.setID(replyIdMap.get(replyCommentInfo.getId()));
              //  list.add(microBlogEntry);
            }

            List<ObjectId> zanUserIds=new ArrayList<ObjectId>();
            for(MicroBlogZanLogsInfo zanLogsInfo:childZans){
                if(zanLogsInfo.getCommentid()==0){
                    ObjectId zanUserId=userMap.get(zanLogsInfo.getUserid())==null?null:userMap.get(zanLogsInfo.getUserid());
                    zanUserIds.add(zanUserId);
                }
            }

            MicroBlogEntry microBlogEntry2=null;
            if("".equals(clienttype)||"PC".equals(clienttype)){
//                microBlogEntry2=new MicroBlogEntry(
//                        userid,
//                        1,
//                        microBlogInfo.getDelflg(),
//                        microBlogInfo.getBlogcontent(),
//                        Platform.PC,
//                        microBlogInfo.getCreatetime().getTime(),
//                        microBlogInfo.getIstop(),
//                        microBlogInfo.getBlogtype(),
//                        microBlogInfo.getMreply(),
//                        microBlogInfo.getZancount(),
//                        null,
//                        microBlogInfo.getIsread(),
//                        schoolMap.get(microBlogInfo.getSchoolId()),
//                        replyIds,
//                        imageList,
//                        zanUserIds,
//                        null,
//                        new ArrayList<ObjectId>()
//                );
            }
            if("Android".equals(clienttype)){
//                microBlogEntry2=new MicroBlogEntry(
//                        userid,
//                        1,
//                        microBlogInfo.getDelflg(),
//                        microBlogInfo.getBlogcontent(),
//                        Platform.Android,
//                        microBlogInfo.getCreatetime().getTime(),
//                        microBlogInfo.getIstop(),
//                        microBlogInfo.getBlogtype(),
//                        microBlogInfo.getMreply(),
//                        microBlogInfo.getZancount(),
//                        null,
//                        microBlogInfo.getIsread(),
//                        schoolMap.get(microBlogInfo.getSchoolId()),
//                        replyIds,
//                        imageList,
//                        zanUserIds,
//                        null,
//                        new ArrayList<ObjectId>()
//                );
            }
            if("iOS".equals(clienttype)){
//                microBlogEntry2=new MicroBlogEntry(
//                        userid,
//                        1,
//                        microBlogInfo.getDelflg(),
//                        microBlogInfo.getBlogcontent(),
//                        Platform.IOS,
//                        microBlogInfo.getCreatetime().getTime(),
//                        microBlogInfo.getIstop(),
//                        microBlogInfo.getBlogtype(),
//                        microBlogInfo.getMreply(),
//                        microBlogInfo.getZancount(),
//                        null,
//                        microBlogInfo.getIsread(),
//                        schoolMap.get(microBlogInfo.getSchoolId()),
//                        replyIds,
//                        imageList,
//                        zanUserIds,
//                        null,
//                        new ArrayList<ObjectId>()
//                );
            }

            microBlogEntry2.setID(blogId);
            list.add(microBlogEntry2);

            for(MicroBlogEntry microBlogEntry:list){
                dealMicroBlogInfo(microBlogEntry);
            }
        }
    }

    private void dealMicroBlogInfo(MicroBlogEntry microBlogEntry) {
        microBlogDao.addMicroBlog(microBlogEntry);

    }

    /*public static void main(String[] args) throws Exception{
        TransferMicroBlog transfer = new TransferMicroBlog();
        transfer.transferMicroBlogInfo(new HashMap<Integer, ObjectId>());
    }*/
}
