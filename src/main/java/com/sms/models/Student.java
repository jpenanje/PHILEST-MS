package com.sms.models;

import com.sms.interfaces.TableRowable;
import com.sms.tools.Config;
import com.sms.tools.Tools;

// Student class
public class Student implements TableRowable {
    private String id = "";
    private String pupilName = "Ade Divine Precious";
    private String studentClass = "Class 1";
    private String classFee = "70000";
    private int classId = 1;
    private String parentName = "Ade Fru Maeva";
    private String phoneNumber = "654370303";
    private String currentYear = "hoho";
    private String registered = "false";
    private String installment1 = "0";
    private String installment2 = "0";
    private String installment3 = "0";
    private String installment4 = "0";
    private String installment5 = "0";
    private String feesOwed = "500";
    private String totalPaid = "100";

    // no arg constructor
    public Student() {
        super();
        initTotalPaid();
        initFeesOwed();
        concatPaymentFields();
    }
    // public Student(String classs) {
    // super();
    // this.studentClass = classs;
    // initTotalPaid();
    // initFeesOwed();
    // concatPaymentFields();
    // }

    // constructor with an initial value for all fields
    public Student(String id, String pupilName, String studentClass, String classFee, int classId, String parentName,
            String phoneNumber,String currentYear, String registered, String installment1, String installment2, String installment3,
            String installment4, String installment5) {
        super();

        this.id = id;
        this.pupilName = pupilName;
        this.studentClass = studentClass;
        this.classFee = classFee;
        this.classId = classId;
        this.phoneNumber = phoneNumber;
        this.parentName = parentName;
        this.currentYear = currentYear;
        this.registered = registered;
        this.installment1 = installment1;
        this.installment2 = installment2;
        this.installment3 = installment3;
        this.installment4 = installment4;
        this.installment5 = installment5;

        initTotalPaid();
        initFeesOwed();
        concatPaymentFields();
    }


    // getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPupilName() {
        return pupilName;
    }

    public void setPupilName(String pupilName) {
        this.pupilName = pupilName;
    }

    public String getStudentClass() {
        return studentClass;
    }

    public void setStudentClass(String studentClass) {
        this.studentClass = studentClass;
    }

    public String getClassFee() {
        return classFee;
    }

    public void setClassFee(String classFee) {
        this.classFee = classFee;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String isRegistered() {
        return registered;
    }

    public void setRegistered(String registered) {
        this.registered = registered;
    }

    public String getInstallment1() {
        return installment1;
    }

    public void setInstallment1(String installment1) {
        this.installment1 = installment1;
    }

    public String getInstallment2() {
        return installment2;
    }

    public void setInstallment2(String installment2) {
        this.installment2 = installment2;
    }

    public String getInstallment3() {
        return installment3;
    }

    public void setInstallment3(String installment3) {
        this.installment3 = installment3;
    }

    public String getInstallment4() {
        return installment4;
    }

    public void setInstallment4(String installment4) {
        this.installment4 = installment4;
    }

    public String getInstallment5() {
        return installment5;
    }

    public void setInstallment5(String installment5) {
        this.installment5 = installment5;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getRegistered() {
        return registered;
    }

    public String getFeesOwed() {
        return feesOwed;
    }

    public void setFeesOwed(String feesOwed) {
        this.feesOwed = feesOwed;
    }

    public String getTotalPaid() {
        return totalPaid;
    }

    public void setTotalPaid(String totalPaid) {
        this.totalPaid = totalPaid;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public String getCurrentYear() {
        return currentYear;
    }

    public void setCurrentYear(String currentYear) {
        this.currentYear = currentYear;
    }

    // initializes the value of total paid
    void initTotalPaid() {
        try {
            totalPaid = "" + (Float.valueOf(installment1) + Float.valueOf(installment2) + Float.valueOf(installment3)
                    + Float.valueOf(installment4) + Float.valueOf(installment5));
        } catch (Exception e) {
            totalPaid = "0";
        }
    }

    // initializes the value of fees owed
    void initFeesOwed() {
        try {
            feesOwed = "" + (Float.valueOf(classFee) - Float.valueOf(totalPaid));
            feesOwed = Tools.addCommasToStringValue(feesOwed);
        } catch (Exception e) {
            feesOwed = "0";
        }
    }

    // concats the payment values with class fee. These are to be separated
    // for comparism
    void concatPaymentFields() {
        installment1 = installment1 + Config.valueDelimiter + classFee;
        installment2 = installment2 + Config.valueDelimiter + classFee;
        installment3 = installment3 + Config.valueDelimiter + classFee;
        installment4 = installment4 + Config.valueDelimiter + classFee;
        installment5 = installment5 + Config.valueDelimiter + classFee;
        totalPaid = totalPaid + Config.valueDelimiter + classFee;
    }

    // returns the json representation of an object of this class
    @Override
    public String toJson() {
        return "{\"full_name\": \"" + pupilName + "\", \"parent_name\": \"" + parentName + "\", \"parent_phone\": \""
                + phoneNumber + "\", \"student_class\": " + classId + ", \"current_year\": \""+currentYear+"\"}";
    }

    // converts a json string to an object of this class
    @Override
    public TableRowable fromJson(String json) {
        return null;
    }

    
}