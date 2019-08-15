package com.pojo.user;

import com.sys.constants.Constant;
import org.apache.commons.lang.StringUtils;

import java.util.*;


/**
 * 用户角色
 *
 * @author fourer
 */
public enum UserRole {

    STUDENT(1, "学生"), //1
    TEACHER(1 << 1, "老师"),//2
    PARENT(1 << 2, "家长"),//4
    HEADMASTER(1 << 3, "校长"),//8
    LEADER_CLASS(1 << 4, "班主任"),//16
    K6KT_HELPER(1 << 5, "K6KT小助手"),//32
    ADMIN(1 << 6, "管理员"),//64
    LEADER_OF_GRADE(1 << 7, "年级组长"),//128
    LEADER_OF_SUBJECT(1 << 8, "学科组长"),//256
    EDUCATION(1 << 9, "教育局"),//512
    SYSMANAGE(1 << 10, "系统管理员"),//1024
    DOORKEEPER(1 << 11, "门卫"),//2048
    DORMMANAGER(1 << 12, "宿舍管理员"),//4096
    FUNCTION_ROOM_MANAGER(1 << 13, "功能教室管理员"),//8192
    DISCUSS_MANAGER(1 << 14, "论坛管理员"),//16384
    DISCUSS_SECTION_MANAGER(1 << 15, "版主"),//32768
    CLASSTEACHER(1 << 16, "班主任"),//65536
    CHECKMANAGE(1 << 17, "新闻检查员"),//131072
    YUNMANAGE(1 << 18, "云管理员"),//262144
    STAFF(1<<19,"职工"),//524288
    OTHER(1<<29,"其他")
    ;

    private UserRole(int role, String des) {
        this.role = role;
        this.des = des;
    }

    private int role;
    private String des;

    public static boolean isOther(int role){
        return (role & OTHER.getRole()) == OTHER.getRole();
    }
    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }


    public static boolean isTeacher(int role) {
        return (role & TEACHER.getRole()) == TEACHER.getRole();
    }

    public static boolean isTeacherOnly(int role) {
        return role == TEACHER.getRole();
    }

    public static boolean isStudent(int role) {
        return (role & STUDENT.getRole()) == STUDENT.getRole();
    }

    public static boolean isParent(int role) {
        return (role & PARENT.getRole()) == PARENT.getRole();
    }

    public static boolean isStudentOrParent(int role) {
        return isStudent(role) || isParent(role);
    }


    /**
     * 是否是班主任
     *
     * @param role
     * @return
     */
    public static boolean isClassTeacher(int role) {
        return (role & CLASSTEACHER.getRole()) == CLASSTEACHER.getRole();
    }

    /**
     * 不是学生也不是家长
     *
     * @param role
     * @return
     */
    public static boolean isNotStudentAndParent(int role) {
        return !isStudent(role) && !isParent(role);
    }


    public static boolean isK6KT(int role) {
        return (role & K6KT_HELPER.getRole()) == K6KT_HELPER.getRole();
    }

    public static boolean isHeadmaster(int role) {
        return (role & HEADMASTER.getRole()) == HEADMASTER.getRole();
    }

    public static boolean isLeaderClass(int role) {
        return (role & LEADER_CLASS.getRole()) == LEADER_CLASS.getRole();
    }

    public static boolean isManager(int role) {
        return (role & ADMIN.getRole()) == ADMIN.getRole();
    }

    public static boolean isSysManager(int role) {
        return (role & SYSMANAGE.getRole()) == SYSMANAGE.getRole();
    }

    public static boolean roleIsRole(int singleRole, int moreRole) {
        return (moreRole & singleRole) == singleRole;
    }

    public static boolean isK6ktHelper(int role) {
        return (role & K6KT_HELPER.getRole()) == K6KT_HELPER.getRole();
    }

    public static boolean isStaff(int role){
        return (role & STAFF.getRole()) == STAFF.getRole();
    }

    public static boolean isManagerOnly(int role) {
        if (role == ADMIN.getRole()) {
            return true;
        }
        return false;
    }

    public static boolean isLeaderOfGrade(int role) {
        return (role & LEADER_OF_GRADE.getRole()) == LEADER_OF_GRADE.getRole();
    }

    public static boolean isLeaderOfSubject(int role) {
        return (role & LEADER_OF_SUBJECT.getRole()) == LEADER_OF_SUBJECT.getRole();
    }

    public static boolean isEducation(int role) {
        return (role & EDUCATION.getRole()) == EDUCATION.getRole();
    }

    public static boolean isDoorKeeper(int role) {
        return (role & DOORKEEPER.getRole()) == DOORKEEPER.getRole();
    }

    public static boolean isDormManager(int role) {
        return (role & DORMMANAGER.getRole()) == DORMMANAGER.getRole();
    }

    public static boolean isFunctionRoomManager(int role) {
        return (role & FUNCTION_ROOM_MANAGER.getRole()) == FUNCTION_ROOM_MANAGER.getRole();
    }

    public static boolean isCheckManager(int role) {
        return (role & CHECKMANAGE.getRole()) == CHECKMANAGE.getRole();
    }

    public static boolean isYunManager(int role) {
        return (role & YUNMANAGE.getRole()) == YUNMANAGE.getRole();
    }

    /**
     * 得到混合角色的role
     *
     * @param roles
     * @return
     */
    public static int getRole(Set<UserRole> roles) {
        int r = Constant.DEFAULT_VALUE_INT;
        for (UserRole role : roles) {
            r = (r | role.getRole());
        }
        return r;
    }

    /**
     * 获取角色描述
     *
     * @param role
     * @return
     */
    public static String getRoleDescription(int role) {
        String roleDesc = StringUtils.EMPTY;
        if (isStudent(role)) {
            roleDesc = UserRole.STUDENT.getDes();
        } else if (isParent(role)) {
            roleDesc = UserRole.PARENT.getDes();
        } else if (isHeadmaster(role)) {
            roleDesc = UserRole.HEADMASTER.getDes();
        } else if (isK6ktHelper(role)) {
            roleDesc = UserRole.K6KT_HELPER.getDes();
        } else if (isManager(role)) {
            roleDesc = UserRole.ADMIN.getDes();
        } else if (isLeaderOfSubject(role)) {
            roleDesc = UserRole.LEADER_OF_SUBJECT.getDes();
        } else if (isLeaderOfGrade(role)) {
            roleDesc = UserRole.LEADER_OF_GRADE.getDes();
        } else if (isClassTeacher(role)) {
            roleDesc = UserRole.CLASSTEACHER.getDes();
        } else if (isStaff(role)) {
            roleDesc = UserRole.STAFF.getDes();
        }else if (isOther(role)){
            roleDesc = UserRole.OTHER.getDes();
        }   else {
            roleDesc = "任课老师";
        }
        return roleDesc;
    }


    public static void main(String[] args) {

        System.out.println(1 << 30);
        System.out.println(1 << 31);

//    	
//    	List<UserRole> list =getUserRoleList(40);
//    	
//    	
//    	for(UserRole r:list)
//    	{
//    		System.out.println(r.getRole());;
//    	}

    }


    /**
     * @param alreadyRole 已经具有的角色
     * @param appendRole  新添加的角色
     * @return
     */
    public static int getRole(int alreadyRole, UserRole appendRole) {
        return alreadyRole | appendRole.getRole();
    }


    /**
     * 是不是属于其中的某个角色
     *
     * @param role
     * @param roles
     * @return
     */
    public static boolean isInRoles(int role, UserRole[] roles) {
        for (UserRole userRole : roles) {
            if ((userRole.getRole() & role) == userRole.getRole()) {
                return true;
            }
        }
        return false;
    }

    public static boolean isInRoles(int role, List<UserRole> roles) {
        for (UserRole userRole : roles) {
            if ((userRole.getRole() & role) == userRole.getRole()) {
                return true;
            }
        }
        return false;
    }


    public static List<UserRole> getUserRoleList(int role) {
        List<UserRole> retList = new ArrayList<UserRole>();
        for (UserRole ur : UserRole.values()) {
            if ((role & ur.getRole()) == ur.getRole()) {
                retList.add(ur);
            }
        }
        return retList;
    }


    public static List<Integer> getUserRoleIntList(int role) {
        List<Integer> retList = new ArrayList<Integer>();
        for (UserRole ur : UserRole.values()) {
            if ((role & ur.getRole()) == ur.getRole()) {
                retList.add(ur.getRole());
            }
        }
        return retList;
    }


    public static UserRole getUserRole(int role) {
        for (UserRole ur : UserRole.values()) {
            if (ur.getRole() == role)
                return ur;
        }
        return null;
    }

    public static Map<Integer, String> getManageCountRoleMap() {
        Map<Integer, String> map = new LinkedHashMap<Integer, String>();
        map.put(UserRole.TEACHER.getRole(), UserRole.TEACHER.getDes());
        map.put(UserRole.STUDENT.getRole(), UserRole.STUDENT.getDes());
        map.put(UserRole.PARENT.getRole(), UserRole.PARENT.getDes());
        return map;
    }

    public static Map<Integer, String> getFieryActivityRoleMap() {
        Map<Integer, String> map = new LinkedHashMap<Integer, String>();
        map.put(UserRole.HEADMASTER.getRole(), UserRole.HEADMASTER.getDes());
        map.put(UserRole.TEACHER.getRole(), UserRole.TEACHER.getDes());
        map.put(UserRole.STUDENT.getRole(), UserRole.STUDENT.getDes());
        return map;
    }


    public static Map<Integer, String> getTeacherManageCountRoleMap() {
        Map<Integer, String> map = new LinkedHashMap<Integer, String>();
        map.put(UserRole.STUDENT.getRole(), UserRole.STUDENT.getDes());
        map.put(UserRole.PARENT.getRole(), UserRole.PARENT.getDes());
        return map;
    }
    public static String getRoleDescriptions(int role) {
        String roleDesc = StringUtils.EMPTY;
        if (isStudent(role)) {
            roleDesc ="学生";
        } else if (isParent(role)) {
            roleDesc = "家长";
        } else if (isHeadmaster(role)) {
            roleDesc =  "领导";
        } else if (isK6ktHelper(role)) {
            roleDesc =  "领导";
        } else if (isManager(role)) {
            roleDesc =  "领导";
        } else if (isLeaderOfSubject(role)) {
            roleDesc =  "领导";
        } else if (isLeaderOfGrade(role)) {
            roleDesc =  "领导";
        }else {
            roleDesc = "老师";
        }
        return roleDesc;
    }

}
