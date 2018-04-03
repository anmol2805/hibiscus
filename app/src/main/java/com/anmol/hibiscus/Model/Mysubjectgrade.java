package com.anmol.hibiscus.Model;

public class Mysubjectgrade {
    String name,professor,credits,id,quiz1,quiz2,midsem,endsem,faculty_assessment,grade_point,subtotal;
    Boolean hide;

    public Mysubjectgrade() {
    }

    public Mysubjectgrade(String name, String professor, String credits, String id, String quiz1, String quiz2, String midsem, String endsem, String faculty_assessment, String grade_point, String subtotal,Boolean hide) {
        this.name = name;
        this.professor = professor;
        this.credits = credits;
        this.id = id;
        this.quiz1 = quiz1;
        this.quiz2 = quiz2;
        this.midsem = midsem;
        this.endsem = endsem;
        this.faculty_assessment = faculty_assessment;
        this.grade_point = grade_point;
        this.subtotal = subtotal;
        this.hide = hide;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfessor() {
        return professor;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }

    public String getCredits() {
        return credits;
    }

    public void setCredits(String credits) {
        this.credits = credits;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuiz1() {
        return quiz1;
    }

    public void setQuiz1(String quiz1) {
        this.quiz1 = quiz1;
    }

    public String getQuiz2() {
        return quiz2;
    }

    public void setQuiz2(String quiz2) {
        this.quiz2 = quiz2;
    }

    public String getMidsem() {
        return midsem;
    }

    public void setMidsem(String midsem) {
        this.midsem = midsem;
    }

    public String getEndsem() {
        return endsem;
    }

    public void setEndsem(String endsem) {
        this.endsem = endsem;
    }

    public String getFaculty_assessment() {
        return faculty_assessment;
    }

    public void setFaculty_assessment(String faculty_assessment) {
        this.faculty_assessment = faculty_assessment;
    }

    public String getGrade_point() {
        return grade_point;
    }

    public void setGrade_point(String grade_point) {
        this.grade_point = grade_point;
    }

    public String getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
    }

    public Boolean getHide() {
        return hide;
    }

    public void setHide(Boolean hide) {
        this.hide = hide;
    }
}
