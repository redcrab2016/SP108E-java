package sp108e;

public enum ChipType {
    SM16703     ("SM16703",     "00"),
    TM1804      ("TM1804",      "01"),
    UCS1903     ("UCS1903",     "02"),
    WS2811      ("WS2811",      "03"),
    WS2801      ("WS2801",      "04"),
    SK6812      ("SK6812",      "05"),
    LPD6803     ("LPD6803",     "06"),
    LPD8806     ("LPD8806",     "07"),
    APA102      ("APA102",      "08"),
    APA105      ("APA105",      "09"),
    DMX512      ("DMX512",      "0a"),
    TM1914      ("TM1914",      "0b"),
    TM1913      ("TM1913",      "0c"),
    P9813       ("P9813",       "0d"),
    INK1003     ("INK1003",     "0e"),
    P943S       ("P943S",       "0f"),
    P9411       ("P9411",       "10"),
    P9413       ("P9413",       "11"),
    TX1812      ("TX1812",      "12"),
    TX1813      ("TX1813",      "13"),
    GS8206      ("GS8206",      "14"),
    GS8208      ("GS8208",      "15"),
    SK9822      ("SK9822",      "16"),
    TM1814      ("TM1814",      "17"),
    SK6812_RGBW ("SK6812_RGBW", "18"),
    P9414       ("P9414",       "19"),
    P9412       ("P9412",       "1a"),
    ;

    ChipType(String chipName, String chipHexCode) {
        this.chipName = chipName;
        this.chipHexCode = chipHexCode;
        int top = Character.digit(chipHexCode.charAt(0), 16);
        int bot = Character.digit(chipHexCode.charAt(1), 16);
        this.chipCode= (top == -1 || bot == -1)?255: (top << 4) + bot;

    }

    private final String chipName;
    private final String chipHexCode;
    private final int chipCode;

    public static ChipType getByHexCode(String hexCode) throws Exception {
        for (ChipType aType: ChipType.values()) {
            if (aType.chipHexCode.equalsIgnoreCase(hexCode)) {
                return aType;
            }
        }
       throw new Exception("Unknown Chip type hexadecimal code '"+hexCode+"'");
    }

    public static ChipType getByCode(int code) throws Exception {
        for (ChipType aType: ChipType.values()) {
            if (aType.chipCode == code) {
                return aType;
            }
        }
        throw new Exception("Unknown Chip type code " + code);
    }

    public static ChipType getByName(String name) throws Exception {
        for (ChipType aType: ChipType.values()) {
            if (aType.chipName.equalsIgnoreCase(name)) {
                return aType;
            }
        }
        throw new Exception("Unknown Chip type named '" +name +"'");
    }

    public String getName() { return chipName;}
    public String getHexCode() { return chipHexCode;}
    public int getCode() { return chipCode;}

}
