package com.wcreation.sprinklesbakery.Model;

public class CartItems {
    private int ct_id;
    private String itemName;
    private String item_img;
    private int Qty;
    private double itemPrice;

    public CartItems(int ct_id, String itemName, String item_img, int qty, double itemPrice) {
        this.ct_id = ct_id;
        this.itemName = itemName;
        this.item_img = item_img;
        Qty = qty;
        this.itemPrice = itemPrice;
    }

    public int getCt_id() {
        return ct_id;
    }

    public void setCt_id(int ct_id) {
        this.ct_id = ct_id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItem_img() {
        return item_img;
    }

    public void setItem_img(String item_img) {
        this.item_img = item_img;
    }

    public int getQty() {
        return Qty;
    }

    public void setQty(int qty) {
        Qty = qty;
    }

    public double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(double itemPrice) {
        this.itemPrice = itemPrice;
    }
}
