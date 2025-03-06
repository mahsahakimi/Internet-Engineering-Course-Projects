package ir.ac.ut.ece.ie.miobook.model;

import lombok.Getter;

@Getter
public class CartElement {
    Book book;
    int days;
    int price;
    public CartElement(Book book, int days){
        this.book = book;
        this.days = days;
        this.price = (book.getPrice() * days) / 10;
        if(days == 0){
            this.price = book.getPrice();
        }
    }
}
