package es.tatanca.logistics.entities.Driver;

public enum DriverStatus {
    WORK("W"), REST("R");

    private final String code;

    DriverStatus(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static DriverStatus fromShortName(String shortName) {
        return switch (shortName) {
            case "W" -> DriverStatus.WORK;
            case "R" -> DriverStatus.REST;
            default -> throw new IllegalArgumentException("ShortName [" + shortName + "] not supported.");
        };
    }

    public static DriverStatus fromLongName(String longName) {
        return switch (longName) {
            case "WORK" -> DriverStatus.WORK;
            case "REST" -> DriverStatus.REST;
            default -> throw new IllegalArgumentException("LongName [" + longName + "] not supported.");
        };
    }

}