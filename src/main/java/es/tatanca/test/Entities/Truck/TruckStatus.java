package es.tatanca.test.Entities.Truck;

public enum TruckStatus {
    OK("O"), NOK("N");

    private String code;

    private TruckStatus(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static TruckStatus fromShortName(String shortName) {
        switch (shortName) {
            case "O":
                return TruckStatus.OK;
            case "N":
                return TruckStatus.NOK;
            default:
                throw new IllegalArgumentException("ShortName [" + shortName + "] not supported.");
        }
    }

    public static TruckStatus fromLongName(String longName) {
        switch (longName) {
            case "OK":
                return TruckStatus.OK;
            case "NOK":
                return TruckStatus.NOK;
            default:
                throw new IllegalArgumentException("LongName [" + longName + "] not supported.");
        }
    }
}
