package es.tatanca.logistics.entities.Order;

public enum OrderCompleted {
    NO("N"), YES("Y");

    private String code;

    private OrderCompleted(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public boolean completedEqual(String longName) {
        return code.equals(OrderCompleted.fromLongName(longName).getCode());
    }

    public static OrderCompleted fromShortName(String shortName) {
        switch (shortName) {
            case "N":
                return OrderCompleted.NO;
            case "Y":
                return OrderCompleted.YES;
            default:
                throw new IllegalArgumentException("ShortName [" + shortName + "] not supported.");
        }
    }

    public static OrderCompleted fromLongName(String longName) {
        switch (longName) {
            case "NO":
                return OrderCompleted.NO;
            case "YES":
                return OrderCompleted.YES;
            default:
                throw new IllegalArgumentException("LongName [" + longName + "] not supported.");
        }
    }
}
