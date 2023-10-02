package com.jw.study.settings.form;


import lombok.Data;

@Data
public class Notifications {

    private boolean studyCreatedByEmail;

    private boolean studyCreatedByWeb;

    private boolean studyEnrollmentResultByEmail;

    private boolean studyEnrollmentResultByWeb;

    private boolean studyUpdatedByEmail;

    private boolean studyUpdatedByWeb;

//    public Notifications(Account account) {
//        this.studyCreatedByEmail = account.isStudyCreatedByEmail();
//        this.studyCreatedByWeb = account.isStudyCreatedByWeb();
//        this.studyEnrollmentResultByEmail = account.isStudyEnrollmentResultByEmail();
//        this.studyEnrollmentResultByWeb = account.isStudyEnrollmentResultByWeb();
//        this.studyUpdatedByEmail = account.isStudyUpdatedByEmail();
//        this.studyUpdatedByWeb = account.isStudyUpdatedByWeb();
//    }
}
