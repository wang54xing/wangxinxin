/**
 * Created by fulaan on 16-5-24.
 */

var type;
$(function() {
    if(type == '1'){
        $('#content-title').html('2014年安徽省高中化学微课评选');
    }else if(type == '0'){
        $('#content-title').html('2014年安徽省初中化学微课评选');
    }
    getWeikeList();
});
$('#weike-container').removeClass('hardloadingb');
var currentpage = 1;
function getWeikeList(){
    if(type == '1'){
        var id;/****视屏地址*****/
        var name;/*****视频名称******/
        var img;/*******图片地址*************/
        var did/*********视屏di****************/


        /*********************视屏地址数组start**********************/
        var vId = new Array();
        vId[0] = "http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/5739683163e79225a0ff46b7.mp4.m3u8"
        vId[1] = "http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/5739685763e79225a0ff46ba.mp4.m3u8"
        vId[2] = "http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/5739685c63e79225a0ff46bd.mp4.m3u8"
        vId[3] = "http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/5739685d63e79225a0ff46c0.mp4.m3u8"
        vId[4] = "http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/5739685d63e79225a0ff46c3.mp4.m3u8"
        vId[5] = "http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/5739685d63e79225a0ff46c6.mp4.m3u8"
        vId[6] = "http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/5739685e63e79225a0ff46c9.mp4.m3u8"
        vId[7] = "http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/5739685e63e79225a0ff46cc.mp4.m3u8"
        vId[8] = "http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/5739686063e79225a0ff46cf.mp4.m3u8"
        vId[9] = "http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/5739686063e79225a0ff46d2.mp4.m3u8"
        vId[10] = "http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/5739686263e79225a0ff46d5.mp4.m3u8"
        vId[11] = "http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/5739686263e79225a0ff46d8.mp4.m3u8"
        vId[12] = "http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/5739686463e79225a0ff46db.mp4.m3u8"
        vId[13] = "http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/5739686663e79225a0ff46de.mp4.m3u8"
        vId[14] = "http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/5739686763e79225a0ff46e1.mp4.m3u8"
        vId[15] = "http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/5739686963e79225a0ff46e4.mp4.m3u8"
        vId[16] = "http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/5739686c63e79225a0ff46e7.mp4.m3u8"
        vId[17] = "http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/5739686c63e79225a0ff46ea.mp4.m3u8"
        vId[18] = "http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/5739686d63e79225a0ff46ed.mp4.m3u8"
        vId[19] = "http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/5739686d63e79225a0ff46f0.mp4.m3u8"
        vId[20] = "http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/5739686e63e79225a0ff46f3.mp4.m3u8"
        vId[21] = "http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/5739686f63e79225a0ff46f6.mp4.m3u8"
        vId[22] = "http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/5739686f63e79225a0ff46f9.mp4.m3u8"
        vId[23] = "http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/5739687363e79225a0ff46fc.mp4.m3u8"
        vId[24] = "http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/5739687463e79225a0ff46ff.mp4.m3u8"
        vId[25] = "http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/5739687463e79225a0ff4702.mp4.m3u8"
        vId[26] = "http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/5739687863e79225a0ff4705.mp4.m3u8"
        vId[27] = "http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/5739687863e79225a0ff4708.mp4.m3u8"
        vId[28] = "http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/5739687963e79225a0ff470b.flv.m3u8"
        vId[29] = "http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/5739687963e79225a0ff470e.mp4.m3u8"
        vId[30] = "http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/5739687a63e79225a0ff4711.mp4.m3u8"
        vId[31] = "http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/5739687b63e79225a0ff4714.mp4.m3u8"
        vId[32] = "http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/5739687c63e79225a0ff4717.mp4.m3u8"
        vId[33] = "http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/5739687c63e79225a0ff471a.mp4.m3u8"
        vId[34] = "http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/5739687d63e79225a0ff471d.mp4.m3u8"
        vId[35] = "http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/5739687e63e79225a0ff4720.mp4.m3u8"
        vId[36] = "http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/5739687f63e79225a0ff4723.mp4.m3u8"
        vId[37] = "http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/5739688063e79225a0ff4726.mp4.m3u8"
        vId[38] = "http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/5739688263e79225a0ff4729.mp4.m3u8"
        vId[39] = "http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/5739688263e79225a0ff472c.mp4.m3u8"
        vId[40] = "http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/5739688363e79225a0ff472f.mp4.m3u8"
        vId[41] = "http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/5739688563e79225a0ff4732.mp4.m3u8"
        vId[42] = "http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/5739688663e79225a0ff4735.mp4.m3u8"
        vId[43] = "http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/5739688763e79225a0ff4738.mp4.m3u8"
        vId[44] = "http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/5739688a63e79225a0ff473b.mp4.m3u8"
        vId[45] = "http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/5739688b63e79225a0ff473e.mp4.m3u8"
        vId[46] = "http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/5739688c63e79225a0ff4741.mp4.m3u8"
        vId[47] = "http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/5739688d63e79225a0ff4744.mp4.m3u8"
        vId[48] = "http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/5739688f63e79225a0ff4747.mp4.m3u8"
        vId[49] = "http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/5739689163e79225a0ff474a.mp4.m3u8"
        vId[50] = "http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/5739689463e79225a0ff474d.mp4.m3u8"
        vId[51] = "http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/5739689463e79225a0ff4750.mp4.m3u8"
        vId[52] = "http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/5739689663e79225a0ff4753.mp4.m3u8"
        vId[53] = "http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/5739689863e79225a0ff4756.mp4.m3u8"
        vId[54] = "http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/5739689963e79225a0ff4759.flv.m3u8"
        vId[55] = "http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/5739689f63e79225a0ff475c.mp4.m3u8"
        vId[56] = "http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/573968a163e79225a0ff475f.mp4.m3u8"
        vId[57] = "http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/573968a263e79225a0ff4762.flv.m3u8"
        vId[58] = "http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/573968a463e79225a0ff4765.mp4.m3u8"
        vId[59] = "http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/573968a563e79225a0ff4768.mp4.m3u8"
        vId[60] = "http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/573968a763e79225a0ff476b.mp4.m3u8"
        vId[61] = "http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/573968a863e79225a0ff476e.mp4.m3u8"
        vId[62] = "http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/573968a963e79225a0ff4771.mp4.m3u8"
        /*********************视屏地址数组end**********************/


        /*********************视屏名称数组start**********************/
        var vName = new Array();
        vName[0] = "芜湖市-刘永浩-化学平衡"
        vName[1] = "芜湖市-马荣-氧化还原反应"
        vName[2] = "马鞍山-后勇军-碳酸钠和碳酸氢钠的差异1"
        vName[3] = "淮南-吕旭东-氯化钠和亚硝酸钠的鉴别"
        vName[4] = "安庆市-魏东-离子方程式"
        vName[5] = "合肥市-许克敏-苯的化学性质"
        vName[6] = "铜陵市-王盛-金属是怎样炼成的"
        vName[7] = "宣城市-俞陈丽-84消毒液的制备原理——电解原理"
        vName[8] = "宣城市-窦宁慧-物质的量的命名之旅"
        vName[9] = "芜湖市-王玉-原电池的构成条件"
        vName[10] = "淮南-张伟-气体摩尔体积"
        vName[11] = "宣城市-魏陈-浓度对化学反应速率的影响"
        vName[12] = "淮北-黄子超- 创新实验：二氧化硫的化学性质"
        vName[13] = "蚌埠市－郭广勇－金属的腐蚀"
        vName[14] = "宣城二中黄雪探究影响醋酸电离平衡的移动"
        vName[15] = "创新实验：二氧化硫的化学性质"
        vName[16] = "淮南-刘文-守恒关系式的书写"
        vName[17] = "宣城市-熊宗齐-燃料电池电极反应方程式的书写"
        vName[18] = "芜湖-许天宝-认识化学变化"
        vName[19] = "宿州-程波-胶体性质——丁达尔现象"
        vName[20] = "马鞍山-周美华-除食盐溶液中的可溶杂质"
        vName[21] = "淮北-朱陈银-蓄电池"
        vName[22] = "宣城市-罗莉萍-电解池"
        vName[23] = "合肥市-蒲绪凤-化学反应速率影响因素"
        vName[24] = "安庆市－潘丹丹－原电池原理"
        vName[25] = "宣城市-鲍世龙-盐类的水解"
        vName[26] = "滁州市-周健-金属的腐蚀"
        vName[27] = "铜陵市-段睿-高考图像分析题选择题变化趋势和解题方法"
        vName[28] = "阜阳市-王冰王海-酸和碱为什么具有相似的化学性质"
        vName[29] = "池州-刘静松-认识微观粒子的共性"
        vName[30] = "阜阳市-马金星-剖析“多功能瓶”"
        vName[31] = "淮南-吴杰-浓硫酸的特性"
        vName[32] = "芜湖市-冯正午-水的电离平衡影响因素"
        vName[33] = "宿州-刘刚-水的电离（复习课）"
        vName[34] = "淮北-万景民-物质的量的概念"
        vName[35] = "亳州市-程品、周倩-弱电解质的电离平衡"
        vName[36] = "宿州-胡长庚-弱电解质的电离"
        vName[37] = "铜陵市-周翔、胡孔国-氨的性质组合实验"
        vName[38] = "滁州市-何玉刚-向苯酚钠溶液通入CO2是生成Na2CO3 还是 NaHCO3"
        vName[39] = "滁州市-郭梓桃-化学实验方案的设计"
        vName[40] = "蚌埠市－王越－水果为什么能解酒"
        vName[41] = "安庆市－何志－复习萃取-分液与蒸馏"
        vName[42] = "滁州市-蔡长娟-探索之旅 钠和乙醇的反应"
        vName[43] = "淮北-尤伟-原电池基本工作原理"
        vName[44] = "宿州-孙长超-控制变量思想在实验设计中的应用"
        vName[45] = "池州-谢成龙-喷泉现象中的反应"
        vName[46] = "池州-方英-初识元素周期表"
        vName[47] = "马鞍山-李旭-乙醇与钠的反应"
        vName[48] = "蚌埠市－王心田－共价键模型"
        vName[49] = "马鞍山-李大庆-离子反应发生的条件"
        vName[50] = "宁国中学沈艳化学反应的限度"
        vName[51] = "蚌埠市－秦群－破解阿伏加德罗常数题中的“陷阱"
        vName[52] = "芜湖市-许敏-等效氢的判断与应用"
        vName[53] = "淮南-承长琴-硫酸根离子的检验"
        vName[54] = "合肥-黄也明 为有源头清水来--保护巢湖水资源"
        vName[55] = "马鞍山市-后勇军-碳酸钠和碳酸氢钠的差异"
        vName[56] = "合肥市-杨延光-烷烃的命名"
        vName[57] = "铜陵市-章诚、吴文健-氯气的化学性质"
        vName[58] = "阜阳市-王永金-再探水的组成"
        vName[59] = "淮北-李从山-《信息型方程式的书写"
        vName[60] = "亳州市-栗广平-原电池工作原理"
        vName[61] = "合肥市-王磊-二氧化硫的化学性质"
        vName[62] = "宣城市-谌祥波-甲烷的取代反应"

        /*********************视屏名称数组end**********************/


        /*********************图片地址start**********************/

        var vImg = new Array();
        vImg[0] = "http://7xiclj.com1.z0.glb.clouddn.com/5739683163e79225a0ff46b8.jpg"
        vImg[1] = "http://7xiclj.com1.z0.glb.clouddn.com/5739685963e79225a0ff46bb.jpg"
        vImg[2] = "http://7xiclj.com1.z0.glb.clouddn.com/5739685c63e79225a0ff46be.jpg"
        vImg[3] = "http://7xiclj.com1.z0.glb.clouddn.com/5739685d63e79225a0ff46c1.jpg"
        vImg[4] = "http://7xiclj.com1.z0.glb.clouddn.com/5739685d63e79225a0ff46c4.jpg"
        vImg[5] = "http://7xiclj.com1.z0.glb.clouddn.com/5739685d63e79225a0ff46c7.jpg"
        vImg[6] = "http://7xiclj.com1.z0.glb.clouddn.com/5739685e63e79225a0ff46ca.jpg"
        vImg[7] = "http://7xiclj.com1.z0.glb.clouddn.com/5739686063e79225a0ff46cd.jpg"
        vImg[8] = "http://7xiclj.com1.z0.glb.clouddn.com/5739686063e79225a0ff46d0.jpg"
        vImg[9] = "http://7xiclj.com1.z0.glb.clouddn.com/5739686263e79225a0ff46d3.jpg"
        vImg[10] = "http://7xiclj.com1.z0.glb.clouddn.com/5739686263e79225a0ff46d6.jpg"
        vImg[11] = "http://7xiclj.com1.z0.glb.clouddn.com/5739686463e79225a0ff46d9.jpg"
        vImg[12] = "http://7xiclj.com1.z0.glb.clouddn.com/5739686663e79225a0ff46dc.jpg"
        vImg[13] = "http://7xiclj.com1.z0.glb.clouddn.com/5739686763e79225a0ff46df.jpg"
        vImg[14] = "http://7xiclj.com1.z0.glb.clouddn.com/5739686963e79225a0ff46e2.jpg"
        vImg[15] = "http://7xiclj.com1.z0.glb.clouddn.com/5739686b63e79225a0ff46e5.jpg"
        vImg[16] = "http://7xiclj.com1.z0.glb.clouddn.com/5739686c63e79225a0ff46e8.jpg"
        vImg[17] = "http://7xiclj.com1.z0.glb.clouddn.com/5739686d63e79225a0ff46eb.jpg"
        vImg[18] = "http://7xiclj.com1.z0.glb.clouddn.com/5739686d63e79225a0ff46ee.jpg"
        vImg[19] = "http://7xiclj.com1.z0.glb.clouddn.com/5739686e63e79225a0ff46f1.jpg"
        vImg[20] = "http://7xiclj.com1.z0.glb.clouddn.com/5739686e63e79225a0ff46f4.jpg"
        vImg[21] = "http://7xiclj.com1.z0.glb.clouddn.com/5739686f63e79225a0ff46f7.jpg"
        vImg[22] = "http://7xiclj.com1.z0.glb.clouddn.com/5739687263e79225a0ff46fa.jpg"
        vImg[23] = "http://7xiclj.com1.z0.glb.clouddn.com/5739687363e79225a0ff46fd.jpg"
        vImg[24] = "http://7xiclj.com1.z0.glb.clouddn.com/5739687463e79225a0ff4700.jpg"
        vImg[25] = "http://7xiclj.com1.z0.glb.clouddn.com/5739687863e79225a0ff4703.jpg"
        vImg[26] = "http://7xiclj.com1.z0.glb.clouddn.com/5739687863e79225a0ff4706.jpg"
        vImg[27] = "http://7xiclj.com1.z0.glb.clouddn.com/5739687863e79225a0ff4709.jpg"
        vImg[28] = "http://7xiclj.com1.z0.glb.clouddn.com/5739687963e79225a0ff470c.jpg"
        vImg[29] = "http://7xiclj.com1.z0.glb.clouddn.com/5739687963e79225a0ff470f.jpg"
        vImg[30] = "http://7xiclj.com1.z0.glb.clouddn.com/5739687b63e79225a0ff4712.jpg"
        vImg[31] = "http://7xiclj.com1.z0.glb.clouddn.com/5739687b63e79225a0ff4715.jpg"
        vImg[32] = "http://7xiclj.com1.z0.glb.clouddn.com/5739687c63e79225a0ff4718.jpg"
        vImg[33] = "http://7xiclj.com1.z0.glb.clouddn.com/5739687d63e79225a0ff471b.jpg"
        vImg[34] = "http://7xiclj.com1.z0.glb.clouddn.com/5739687e63e79225a0ff471e.jpg"
        vImg[35] = "http://7xiclj.com1.z0.glb.clouddn.com/5739687f63e79225a0ff4721.jpg"
        vImg[36] = "http://7xiclj.com1.z0.glb.clouddn.com/5739688063e79225a0ff4724.jpg"
        vImg[37] = "http://7xiclj.com1.z0.glb.clouddn.com/5739688163e79225a0ff4727.jpg"
        vImg[38] = "http://7xiclj.com1.z0.glb.clouddn.com/5739688263e79225a0ff472a.jpg"
        vImg[39] = "http://7xiclj.com1.z0.glb.clouddn.com/5739688263e79225a0ff472d.jpg"
        vImg[40] = "http://7xiclj.com1.z0.glb.clouddn.com/5739688463e79225a0ff4730.jpg"
        vImg[41] = "http://7xiclj.com1.z0.glb.clouddn.com/5739688563e79225a0ff4733.jpg"
        vImg[42] = "http://7xiclj.com1.z0.glb.clouddn.com/5739688663e79225a0ff4736.jpg"
        vImg[43] = "http://7xiclj.com1.z0.glb.clouddn.com/5739688963e79225a0ff4739.jpg"
        vImg[44] = "http://7xiclj.com1.z0.glb.clouddn.com/5739688a63e79225a0ff473c.jpg"
        vImg[45] = "http://7xiclj.com1.z0.glb.clouddn.com/5739688b63e79225a0ff473f.jpg"
        vImg[46] = "http://7xiclj.com1.z0.glb.clouddn.com/5739688d63e79225a0ff4742.jpg"
        vImg[47] = "http://7xiclj.com1.z0.glb.clouddn.com/5739688d63e79225a0ff4745.jpg"
        vImg[48] = "http://7xiclj.com1.z0.glb.clouddn.com/5739689063e79225a0ff4748.jpg"
        vImg[49] = "http://7xiclj.com1.z0.glb.clouddn.com/5739689363e79225a0ff474b.jpg"
        vImg[50] = "http://7xiclj.com1.z0.glb.clouddn.com/5739689463e79225a0ff474e.jpg"
        vImg[51] = "http://7xiclj.com1.z0.glb.clouddn.com/5739689463e79225a0ff4751.jpg"
        vImg[52] = "http://7xiclj.com1.z0.glb.clouddn.com/5739689763e79225a0ff4754.jpg"
        vImg[53] = "http://7xiclj.com1.z0.glb.clouddn.com/5739689863e79225a0ff4757.jpg"
        vImg[54] = "http://7xiclj.com1.z0.glb.clouddn.com/5739689d63e79225a0ff475a.jpg"
        vImg[55] = "http://7xiclj.com1.z0.glb.clouddn.com/5739689f63e79225a0ff475d.jpg"
        vImg[56] = "http://7xiclj.com1.z0.glb.clouddn.com/573968a163e79225a0ff4760.jpg"
        vImg[57] = "http://7xiclj.com1.z0.glb.clouddn.com/573968a363e79225a0ff4763.jpg"
        vImg[58] = "http://7xiclj.com1.z0.glb.clouddn.com/573968a463e79225a0ff4766.jpg"
        vImg[59] = "http://7xiclj.com1.z0.glb.clouddn.com/573968a563e79225a0ff4769.jpg"
        vImg[60] = "http://7xiclj.com1.z0.glb.clouddn.com/573968a763e79225a0ff476c.jpg"
        vImg[61] = "http://7xiclj.com1.z0.glb.clouddn.com/573968a863e79225a0ff476f.jpg"
        vImg[62] = "http://7xiclj.com1.z0.glb.clouddn.com/573968aa63e79225a0ff4772.jpg"





        /*********************图片地址end**********************/


        /*********************视屏idstart**********************/

        var vDid = new Array();


        vDid[0] = "5739683363e79225a0ff46b9"
        vDid[1] = "5739685a63e79225a0ff46bc"
        vDid[2] = "5739685c63e79225a0ff46bf"
        vDid[3] = "5739685d63e79225a0ff46c2"
        vDid[4] = "5739685d63e79225a0ff46c5"
        vDid[5] = "5739685e63e79225a0ff46c8"
        vDid[6] = "5739685e63e79225a0ff46cb"
        vDid[7] = "5739686063e79225a0ff46ce"
        vDid[8] = "5739686063e79225a0ff46d1"
        vDid[9] = "5739686263e79225a0ff46d4"
        vDid[10] = "5739686263e79225a0ff46d7"
        vDid[11] = "5739686463e79225a0ff46da"
        vDid[12] = "5739686663e79225a0ff46dd"
        vDid[13] = "5739686763e79225a0ff46e0"
        vDid[14] = "5739686963e79225a0ff46e3"
        vDid[15] = "5739686c63e79225a0ff46e6"
        vDid[16] = "5739686c63e79225a0ff46e9"
        vDid[17] = "5739686d63e79225a0ff46ec"
        vDid[18] = "5739686d63e79225a0ff46ef"
        vDid[19] = "5739686e63e79225a0ff46f2"
        vDid[20] = "5739686f63e79225a0ff46f5"
        vDid[21] = "5739686f63e79225a0ff46f8"
        vDid[22] = "5739687363e79225a0ff46fb"
        vDid[23] = "5739687463e79225a0ff46fe"
        vDid[24] = "5739687463e79225a0ff4701"
        vDid[25] = "5739687863e79225a0ff4704"
        vDid[26] = "5739687863e79225a0ff4707"
        vDid[27] = "5739687963e79225a0ff470a"
        vDid[28] = "5739687963e79225a0ff470d"
        vDid[29] = "5739687a63e79225a0ff4710"
        vDid[30] = "5739687b63e79225a0ff4713"
        vDid[31] = "5739687c63e79225a0ff4716"
        vDid[32] = "5739687c63e79225a0ff4719"
        vDid[33] = "5739687d63e79225a0ff471c"
        vDid[34] = "5739687e63e79225a0ff471f"
        vDid[35] = "5739687f63e79225a0ff4722"
        vDid[36] = "5739688063e79225a0ff4725"
        vDid[37] = "5739688263e79225a0ff4728"
        vDid[38] = "5739688263e79225a0ff472b"
        vDid[39] = "5739688363e79225a0ff472e"
        vDid[40] = "5739688563e79225a0ff4731"
        vDid[41] = "5739688663e79225a0ff4734"
        vDid[42] = "5739688763e79225a0ff4737"
        vDid[43] = "5739688a63e79225a0ff473a"
        vDid[44] = "5739688b63e79225a0ff473d"
        vDid[45] = "5739688c63e79225a0ff4740"
        vDid[46] = "5739688d63e79225a0ff4743"
        vDid[47] = "5739688f63e79225a0ff4746"
        vDid[48] = "5739689163e79225a0ff4749"
        vDid[49] = "5739689463e79225a0ff474c"
        vDid[50] = "5739689463e79225a0ff474f"
        vDid[51] = "5739689663e79225a0ff4752"
        vDid[52] = "5739689863e79225a0ff4755"
        vDid[53] = "5739689963e79225a0ff4758"
        vDid[54] = "5739689f63e79225a0ff475b"
        vDid[55] = "573968a163e79225a0ff475e"
        vDid[56] = "573968a263e79225a0ff4761"
        vDid[57] = "573968a463e79225a0ff4764"
        vDid[58] = "573968a563e79225a0ff4767"
        vDid[59] = "573968a763e79225a0ff476a"
        vDid[60] = "573968a863e79225a0ff476d"
        vDid[61] = "573968a963e79225a0ff4770"
        vDid[62] = "573968ab63e79225a0ff4773"
        vDid[63] = "573968ac63e79225a0ff4776"
        vDid[64] = "d=573968b163e79225a0ff4779"

        var html = '';
        for (var i = 0; i < 63; i++) {
            id =vId[i];
            name = vName[i];
            img = vImg[i];
            did = vDid[i];
            html += '<dl class="weike-item" onclick="playTheMovie(\'' + id + '\')" style="background-color: white;">' +
                '<dt><img src="' + img + '"/></dt>' +
                '<dd class="weike-title" style="color: black;">' + name+ '</dd>' +
                '<dd class="play" ><img src="/customizedpage/ah/img/play_icon.png"/></dd>' +
                '</dl>';
        }
        $('#weike-container').html(html);
    }else if(type='0'){
        var html ='';
            html += '<dl class="weike-item" onclick="playTheMovie(\'' + 'http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/573968ab63e79225a0ff4774.mp4.m3u8' + '\')" style="background-color: white;">' +
                '<dt><img src="' + 'http://7xiclj.com1.z0.glb.clouddn.com/573968ab63e79225a0ff4775.jpg'+ '"/></dt>' +
                '<dd class="weike-title" style="color: black;">' + '淮南市-刘娟-中和反应'+ '</dd>' +
                '<dd class="play" ><img src="/customizedpage/ah/img/play_icon.png"/></dd>' +
                '</dl>'+
                '<dl class="weike-item" onclick="playTheMovie(\'' + 'http://7sbrbl.com1.z0.glb.clouddn.com/m3u8/573968ac63e79225a0ff4777.mp4.m3u8' + '\')" style="background-color: white;">' +
                '<dt><img src="' + 'http://7xiclj.com1.z0.glb.clouddn.com/573968ad63e79225a0ff4778.jpg'+ '"/></dt>' +
                '<dd class="weike-title" style="color: black;">' + '宣城市 魏明贵 空气中氧气含量的测定再探究'+ '</dd>' +
                '<dd class="play" ><img src="/customizedpage/ah/img/play_icon.png"/></dd>' +
                '</dl>';
        }
       $('#weike-container').html(html);

}



function closeCloudView() {
    $('#sewise-div').hide();
    $('.bg').hide();
}


function showplay(obj){
    $(obj).find('.play').show();
    $(obj).find('dt').addClass('translayer');
}
function hideplay(obj){
    $(obj).find('.play').hide();
    $(obj).find('dt').removeClass('translayer');
}
