package es.tatanca.test.Entities.Cargo;

public enum CargoStatus {
    READY("R"), ASSIGNED("A"), SHIPPED("S"), DELIVERED("D");

    private String code;

    private CargoStatus(String code) { this.code = code; }

    public String getCode() { return code; }

    public static CargoStatus fromShortName(String shortName) {
        switch (shortName) {
            case "R":
                return CargoStatus.READY;
            case "A":
                return CargoStatus.ASSIGNED;
            case "S":
                return CargoStatus.SHIPPED;
            case "D":
                return CargoStatus.DELIVERED;
            default:
                throw new IllegalArgumentException("ShortName [" + shortName + "] not supported.");
        }
    }

    public static CargoStatus fromLongName(String longName) {
        switch (longName) {
            case "READY":
                return CargoStatus.READY;
            case "ASSIGNED":
                return CargoStatus.ASSIGNED;
            case "SHIPPED":
                return CargoStatus.SHIPPED;
            case "DELIVERED":
                return CargoStatus.DELIVERED;
            default:
                throw new IllegalArgumentException("LongName [" + longName + "] not supported.");
        }
    }
}
