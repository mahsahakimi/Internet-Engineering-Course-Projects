package ir.ac.ut.ece.ie.miobook.model;

import lombok.Getter;

import java.util.List;

@Getter
public class Purchase {

    private List<String> bookTitles;
    private int totalCost;
    private String purchaseDateTime;

    public Purchase(List<String> bookTitles, int totalCost, String purchaseDateTime) {
        this.bookTitles = bookTitles;
        this.totalCost = totalCost;
        this.purchaseDateTime = purchaseDateTime;
    }

}