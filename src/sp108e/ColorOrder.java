package sp108e;

public enum ColorOrder {
    RGB     ("RGB",     "00"),
    RBG     ("RBG",     "01"),
    GRB     ("GRB",     "02"),
    GBR     ("GBR",     "03"),
    BRG     ("BRG",     "04"),
    BGR     ("BGR",     "05"),
;

    ColorOrder(String coName, String coHexCode) {
        this.colorOrderName = coName;
        this.colorOrderHexCode = coHexCode;
        int top = Character.digit(coHexCode.charAt(0), 16);
        int bot = Character.digit(coHexCode.charAt(1), 16);
        this.colorOrderCode= (top == -1 || bot == -1)?255: (top << 4) + bot;
    }

    private final String colorOrderName;
    private final String colorOrderHexCode;
    private final int colorOrderCode;

    public static ColorOrder getByHexCode(String hexCode) throws Exception {
        for (ColorOrder aType: ColorOrder.values()) {
            if (aType.colorOrderHexCode.equalsIgnoreCase(hexCode)) {
                return aType;
            }
        }
        throw new Exception("Unknown Color order hexadecimal code " + hexCode);
    }

    public static ColorOrder getByCode(int code) throws Exception {
        for (ColorOrder aType: ColorOrder.values()) {
            if (aType.colorOrderCode == code) {
                return aType;
            }
        }
        throw new Exception("Unknown Color order code " + code);
    }

    public static ColorOrder getByName(String name) throws Exception {
        for (ColorOrder aType: ColorOrder.values()) {
            if (aType.colorOrderName.equalsIgnoreCase(name)) {
                return aType;
            }
        }
        throw new Exception("Unknown Color order named '" + name +"'");
    }

    public String getName() { return colorOrderName;}
    public String getHexCode() { return colorOrderHexCode;}
    public int getCode() { return colorOrderCode;}

}
