package com.example.abdullah.socialuniversity;

import java.io.Serializable;

/**
 * Created by abdullah on 14.04.2017.
 */

public class Universities implements Serializable {

    private String universityName;

    private String universityWeb;

    private String universityLogo;

    public Universities(){

    }

    public Universities(String universityName, String universityWeb, String universityLogo){
        super();
        this.universityName = universityName;
        this.universityWeb = universityWeb;
        this.universityLogo = universityLogo;
    }

    public String getUniversityName() {
        return universityName;
    }

    public void setUniversityName(String universityName) {
        this.universityName = universityName;
    }

    public String getUniversityWeb() {
        return universityWeb;
    }

    public void setUniversityWeb(String universityWeb) {
        this.universityWeb = universityWeb;
    }

    public String getUniversityLogo() {
        return universityLogo;
    }

    public void setUniversityLogo(String universityLogo) {
        this.universityLogo = universityLogo;
    }
}
