package drinkshop.cp102.drinkshopclient.decoder;

public class OrderTypeDecoder {
    public static String valueOfOrderType(String orderType) {
        String value = null;
        switch (orderType) {
            case "0":
                value = "自取";
                break;
            case "1":
                value = "外送";
                break;
        }
        return value;
    }
}
