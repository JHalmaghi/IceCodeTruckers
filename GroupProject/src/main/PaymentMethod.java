package main;

import java.util.Date;

public class PaymentMethod {

    private String typeOfCard;
    private int cardNumber;
    private Date experationDate;

    public PaymentMethod(){}

    public String getTypeOfCard() {
        return typeOfCard;
    }

    public void setTypeOfCard(String typeOfCard) {
        this.typeOfCard = typeOfCard;
    }

    public int getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(int cardNumber) {
        this.cardNumber = cardNumber;
    }

    public Date getExperationDate() {
        return experationDate;
    }

    public void setExperationDate(Date experationDate) {
        this.experationDate = experationDate;
    }
}
