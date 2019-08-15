package com.pojo.utils;

import java.util.ArrayList;
import java.util.List;

/**className 类名
 * data 中三列数据表示     参数名  参数类型  对应数据库字段名
 * Created by fl on 2016/8/23.
 */
public class Auto {

    private static String className;
    private static List<String[]> data = new ArrayList<String[]>();

    public static void main(String[] args){
        className = "EvaluationTeacherEntry";
        data.add(new String[]{"schoolId", "ObjectId", "sid"});
        data.add(new String[]{"teacherId", "ObjectId", "tid"});
        data.add(new String[]{"statement", "String", "stat"});
        data.add(new String[]{"evidence", "String", "evi"});
        new Auto().generateCode();
    }

    private void generateCode(){
        System.out.println("import com.mongodb.BasicDBObject;");
        System.out.println("import com.pojo.base.BaseDBObject;");
        System.out.println("import org.bson.types.ObjectId;");
        System.out.println("");
        System.out.println("public class " + className + " extends BaseDBObject {");
        System.out.println("");
        System.out.println("\tpublic " + className + "(){}");
        System.out.println("");
        System.out.println("\tpublic " + className + "(BasicDBObject baseEntry){");
        System.out.println("\t\tsetBaseEntry(baseEntry);");
        System.out.println("\t}");
        System.out.println("");
        System.out.println("\tpublic " + className + "(" + methodArgs() + "){");
        System.out.println("\t\tBasicDBObject baseEntry = new BasicDBObject()");
        for(String[] arg : data){
            System.out.println("\t\t\t\t.append(\"" + arg[2] + "\", " + arg[0] + ")");
        }
        System.out.println("\t\t\t;");
        System.out.println("\t\tsetBaseEntry(baseEntry);");
        System.out.println("\t}");
        System.out.println("");
        System.out.println("");
        for(String[] arg : data){
            System.out.println("\tpublic " + arg[1] + " get" + firstCharUpperCase(arg[0]) + "() {");
            System.out.println("\t\treturn "+convert(arg[1])+"(\""+arg[2]+"\");");
            System.out.println("\t}");
            System.out.println("");
            System.out.println("\tpublic void set" + firstCharUpperCase(arg[0]) + "(" + arg[1] + " " + arg[0] + ") {");
            System.out.println("\t\tsetSimpleValue(\"" + arg[2] + "\", " + arg[0] + ");");
            System.out.println("\t}");
            System.out.println("");
        }
        System.out.println("}");
        System.out.println("");
    }

    private String methodArgs(){
        StringBuffer stringBuffer = new StringBuffer();
        for(String[] arg : data){
            stringBuffer.append(arg[1] + " " + arg[0] + ", ");
        }
        stringBuffer.delete(stringBuffer.length()-2, stringBuffer.length()-1);
        String args = stringBuffer.toString();
        return args;
    }

    private String firstCharUpperCase(String str){
        char[] array = str.toCharArray();
        array[0] -= 32;
        return String.valueOf(array);
    }

    private String convert(String str){
        if("int".equals(str) || "Integer".equals(str)){
            return "getSimpleIntegerValue";
        }
        if("double".equals(str) || "Double".equals(str)){
            return "getSimpleDoubleValue";
        }
        if("long".equals(str) || "Long".equals(str)){
            return "getSimpleLongValue";
        }
        if("String".equals(str)){
            return "getSimpleStringValue";
        }
        if("ObjectId".equals(str)){
            return "getSimpleObjecIDValue";
        }
        if("boolean".equals(str) || "Boolean".equals(str)){
            return "getSimpleBoolean";
        }

        return "getSimpleObjectValue";
    }
}

