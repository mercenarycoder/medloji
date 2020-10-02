package com.medlogi.medlogi;

public class cartBaseClass {
    String img,medicine,id,qty,price,price_old;

    public cartBaseClass(String img, String medicine, String id, String qty,
                         String price, String price_old) {
        this.img = img;
        this.medicine = medicine;
        this.id = id;
        this.qty = qty;
        this.price = price;
        this.price_old = price_old;
    }

    public String getImg() {
        return img;
    }

    public String getMedicine() {
        return medicine;
    }

    public String getId() {
        return id;
    }

    public String getQty() {
        return qty;
    }

    public String getPrice() {
        return price;
    }

    public String getPrice_old() {
        return price_old;
    }
}
