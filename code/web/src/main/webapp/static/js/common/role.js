/**
 * Created by Hao on 2015/4/13.
 */

var STUDENT=1//"学生")
var TEACHER=1<<1// "老师"),
var PARENT=1<<2//,"家长"),
var HEADMASTER=1<<3//"校长"),
var LEADER_CLASS=1<<4//,"班主任"),
var K6KT_HELPER=1<<5//,"K6KT小助手"),
var ADMIN=1<<6//,"管理员"),
var LEADER_OF_GRADE=1<<7//"年级组长"),
var LEADER_OF_SUBJECT=1<<8//,"学科组长"),
var EDUCATION=1<<9//,"教育局"),
var SYSMANAGE=1<<10//,"系统管理员"//1024
var DOORKEEPER=1<<11//,"门卫"),//2048
var DORMMANAGER=1<<12//,"宿舍管理员"),//4096
var FUNCTION_ROOM_MANAGER=1<<13//,"功能教室管理员"),//

function getRole(role){
    var roles="";
    if((role & STUDENT)==STUDENT){
        roles="学生";
    }else if((role & TEACHER)==TEACHER){
        roles="老师";
    }else if((role & HEADMASTER)==HEADMASTER){
        roles="校领导";
    }else if((role & EDUCATION)==EDUCATION){
        roles="教育局";
    }else{
        roles="家长";
    }
    return roles;
}

function isStudent(role){
    return (role & STUDENT) ==STUDENT;
}
function isTeacher(role){
    return (role & TEACHER) ==TEACHER;
}
function isK6ktHelper (role){
    return (role & K6KT_HELPER)==K6KT_HELPER;
}
function isLeaderOfSubject(role){
    return (role & LEADER_OF_SUBJECT) == LEADER_OF_SUBJECT;
}
function isHeadMaster(role){
    return (role & HEADMASTER) ==HEADMASTER;
}
function isHeadMasterAndTeacher(role){
    return isTeacher(role) && isHeadMaster(role);
}
function isParent(role){
    return (role & PARENT) == PARENT;
}
function isAdmin(role){
    return (role &  ADMIN) == ADMIN;
}
function isLeaderOfGrade(role){
    return (role & LEADER_OF_GRADE) ==LEADER_OF_GRADE;
}
function isLeaderOfClass(role){
    return (role & LEADER_CLASS) ==LEADER_CLASS;
}
function isEducation(role){
    return (role & EDUCATION) ==EDUCATION;
}
function isSubjectLeader(role){
    return (role & LEADER_OF_SUBJECT)==LEADER_OF_SUBJECT;
}

function isDoorKeeper(role) {
    return (role & DOORKEEPER)==DOORKEEPER;
}

function isDormManager(role) {
    return (role & DORMMANAGER)==DORMMANAGER;
}