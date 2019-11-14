package com.example.salecheck;

public class ItemList {
    public String itemName;//이름
    public String address;//주소
    public String money;//현재가격
    public  String chMOney;//설정가격
    public  int position ;//위치
    public String picture;//사진

    public String getItemName() {
        return itemName;
    }
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
    public String getMoney() {
        return money;
    }
    public void setMoney(String money) {
        this.money = money;
    }
    public String getChMOney() {
        return chMOney;
    }
    public void setChMOney(String chMOney) {
        this.chMOney = chMOney;
    }
    public int getPosition() {
        return position;
    }
    public void setPosition(int position) {
        this.position = position;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getPicture() {
        return picture;
    }
    public void setPicture(String picture) {
        this.picture = picture;
    }
}
