package com.pojo.school;

/**
 * Created by fl on 2015/11/30.
 */
public class TermTypeDTO implements Comparable{
    private int termType;
    private String classInfo;

    public TermTypeDTO(){}

    public TermTypeDTO(int termType, String classInfo){
        this.termType = termType;
        this.classInfo = classInfo;
    }

    public int getTermType() {
        return termType;
    }

    public void setTermType(int termType) {
        this.termType = termType;
    }

    public String getClassInfo() {
        return classInfo;
    }

    public void setClassInfo(String classInfo) {
        this.classInfo = classInfo;
    }

    @Override
    public int compareTo(Object o) {
        TermTypeDTO other = (TermTypeDTO)o;
        Integer thisTT = getTermType();
        Integer otherTT = other.getTermType();
        return thisTT.compareTo(otherTT);
    }
}
