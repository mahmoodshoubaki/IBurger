package com.example.iburger;

public class OrderDetails {

    private String choice;
    private String type;
    private int price = 0;


    public OrderDetails(String choice, String type) {
        this.choice = choice;
        this.type = type;
        setPrice();
    }

    public void setPrice() {
        if (choice.contains("burger")) {
            switch (choice.toLowerCase()) {
                case "cheese burger":
                    if (type.equalsIgnoreCase("meal")) {
                        price = 6;
                    } else {
                        price = 5;
                    }
                    break;
                case "bfr burger":
                    if (type.equalsIgnoreCase("meal")) {
                        price = 6;
                    } else {
                        price = 4;
                    }
                    break;
                case "beef burger":
                    if (type.equalsIgnoreCase("meal")) {
                        price = 7;
                    } else {
                        price = 6;
                    }
                    break;
                case "mash burger":
                    if (type.equalsIgnoreCase("meal")) {
                        price = 4;
                    } else {
                        price = 3;
                    }
                    break;
            }
        }else {
            switch (choice.toLowerCase()) {
                case "shawarma":
                    if (type.equalsIgnoreCase("meal")) {
                        price = 6;
                    } else {
                        price = 5;
                    }
                    break;
                case "hotdog":
                    if (type.equalsIgnoreCase("meal")) {
                        price = 6;
                    } else {
                        price = 4;
                    }
                    break;
                case "chrispy":
                    if (type.equalsIgnoreCase("meal")) {
                        price = 7;
                    } else {
                        price = 6;
                    }
                    break;
                case "faheta":
                    if (type.equalsIgnoreCase("meal")) {
                        price = 4;
                    } else {
                        price =  3;
                    }
                    break;
            }
        }
    }

    public int getPrice() {
        return price;
    }

    public String getChoice() {
        return choice;
    }

    public String getType() {
        return type;
    }

}
