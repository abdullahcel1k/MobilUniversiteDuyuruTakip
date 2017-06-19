package com.example.abdullah.socialuniversity;

import java.io.Serializable;

/**
 * Created by abdullah on 19.05.2017.
 */

public class Notices implements Serializable {

    private String noticeLogo;

    private String noticeTitle;

    private String noticeUrl;

    public Notices(){

    }

    public Notices(String noticeLogo, String noticeTitle, String noticeUrl){
        super();
        this.noticeLogo = noticeLogo;
        this.noticeTitle = noticeTitle;
        this.noticeUrl = noticeUrl;
    }

    public String getNoticeLogo() {
        return noticeLogo;
    }

    public void setNoticeLogo(String noticeLogo) {
        this.noticeLogo = noticeLogo;
    }

    public String getNoticeTitle() {
        return noticeTitle;
    }

    public void setNoticeTitle(String noticeTitle) {
        this.noticeTitle = noticeTitle;
    }

    public String getNoticeUrl() {
        return noticeUrl;
    }

    public void setNoticeUrl(String noticeUrl) {
        this.noticeUrl = noticeUrl;
    }
}
