package com.example.shaimaalzahrani.inventoryapp_ufp;

public class Item {

    private String prodName;
    private String price;
    private int quantity;
    private String supplierName;
    private String supplierEmail;
    private String supplierPhone;
    private String image;

    public Item(String prod, String prc, int Q, String Name, String Phone, String Email, String img) {
        prodName = prod;
        price = prc;
        quantity = Q;
        supplierName = Name;
        supplierEmail = Email;
        supplierPhone = Phone;
        image = img;
    }

    public String getProductName() {
        return prodName;
    }

    public String getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public String getSupplierPhone() {
        return supplierPhone;
    }

    public String getSupplierEmail() {
        return supplierEmail;
    }

    public String getImage() {
        return image;
    }
    @Override
    public String toString() {
        return "Item{ prodName='" + prodName + '\'' + ", price='" + price + '\'' +
                ", quantity=" + quantity + ", supplierName='" + supplierName + '\'' +
                ", supplierPhone='" + supplierPhone + '\'' + ", supplierEmail='" + supplierEmail + '\'' + '}';
    }

}
