package drinkshop.cp102.drinkshopclient.decoder;

public class ProductDetails {

    public String valueOfHotOrice(int hotOrice) {
        String value = null;
        switch (hotOrice) {
            case 0:
                value = "冷飲";
                break;
            case 1:
                value = "熱飲";
                break;
        }
        return value;
    }

    public String valueOfTemperature(int hotOrice, int temperature) {
        String value = null;
        if (hotOrice == 0) {
            switch (temperature) {
                case 0:
                    value = "正常冰";
                    break;
                case 1:
                    value = "少冰";
                    break;
                case 2:
                    value = "微冰";
                    break;
                case 3:
                    value = "去冰";
                    break;
            }
        } else if (hotOrice == 1) {
            switch (temperature) {
                case 0:
                    value = "正常熱";
                    break;
                case 1:
                    value = "熱一點";
                    break;
            }
        }
        return value;
    }

    public String valueOfSuger(int suger) {
        String value = null;
        switch (suger) {
            case 0:
                value = "正常糖";
                break;
            case 1:
                value = "少糖";
                break;
            case 2:
                value = "半糖";
                break;
            case 3:
                value = "微糖";
                break;
            case 4:
                value = "無糖";
                break;
        }
        return value;
    }

    public String valueOfSize(int size) {
        String value = null;
        switch (size) {
            case 0:
                value = "M";
                break;
            case 1:
                value = "L";
                break;
        }
        return value;
    }

}
