package com.medlogi.medlogi;

public class base_dashboard {
String cat_id1,cat_id2,cat_name1,cat_name2,cat_img1,cat_img2;

    public base_dashboard(String cat_id1, String cat_id2, String cat_name1,
                          String cat_name2, String cat_img1, String cat_img2) {
        this.cat_id1 = cat_id1;
        this.cat_id2 = cat_id2;
        this.cat_name1 = cat_name1;
        this.cat_name2 = cat_name2;
        this.cat_img1 = cat_img1;
        this.cat_img2 = cat_img2;
    }

    public String getCat_id1() {
        return cat_id1;
    }

    public String getCat_id2() {
        return cat_id2;
    }

    public String getCat_name1() {
        return cat_name1;
    }

    public String getCat_name2() {
        return cat_name2;
    }

    public String getCat_img1() {
        return cat_img1;
    }

    public String getCat_img2() {
        return cat_img2;
    }
}
