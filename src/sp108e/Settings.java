package sp108e;

public class Settings {

    private boolean     isOn;
    private Animation   animation;
    private int         animationSpeed;
    private int         brightness;
    private ColorOrder  colorOrder;
    private int         ledsPerSegment;
    private int         segments;
    private String      color;
    private ChipType    chiptype;
    private int         recorderPatterns;
    private int         whiteChannelBrightness;

    public boolean isOn() { return isOn;}
    public boolean isOff() { return !isOn;}
    public Animation getAnimation() { return animation; }
    public int getAnimationSpeed() { return animationSpeed;}
    public int getBrightness() { return brightness;}
    public ColorOrder getColorOrder() { return colorOrder;}
    public int getLEDsPerSegment() { return ledsPerSegment;}
    public int getSegments() { return segments;}
    public String getColor() { return color;}
    public ChipType getChiptype() { return chiptype;}
    public int getRecorderPatterns() { return recorderPatterns;}
    public int getWhiteChannelBrightness() { return whiteChannelBrightness;}

    public Settings(String hexSettings) throws Exception {
        init(hexSettings);
    }

    private int hexbytestr2int(String hexabyte) throws Exception {
        int top = Character.digit(hexabyte.charAt(0), 16);
        int bot = Character.digit(hexabyte.charAt(1), 16);
        if (top == -1 || bot == -1) throw new Exception("Non-hexadecimal digit found");
        return (top << 4) + bot;
    }

    private void init(String hexSettings) throws Exception {
        if (hexSettings == null || hexSettings.length() != 34) throw new Exception("Bad hexadecimal string controller settings");
        animation = Animation.getByHexCode(hexSettings.substring(4,6)); //       current_animation = get_animation(raw_settings[4:6])
        chiptype = ChipType.getByHexCode(hexSettings.substring(26,28)); //       chip_type = get_chip_type(raw_settings[26:28])
        colorOrder = ColorOrder.getByHexCode(hexSettings.substring(10,12));  //   color_order = get_color_order(raw_settings[10:12])
        isOn = hexSettings.startsWith("01",2) ;  //        turned_on = int(raw_settings[2:4], 16)
        color = hexSettings.substring(20,26); //       current_color = raw_settings[20:26].upper()
        animationSpeed =hexbytestr2int(hexSettings.substring(6,8)); //            "animation_speed": int(raw_settings[6:8], 16),
        brightness = hexbytestr2int(hexSettings.substring(8,10)); //           "current_brightness": int(raw_settings[8:10], 16),
        ledsPerSegment = hexbytestr2int(hexSettings.substring(12,14)) * 256 + hexbytestr2int(hexSettings.substring(14,16)); //           "leds_per_segment": int(raw_settings[12:16], 16),
        segments = hexbytestr2int(hexSettings.substring(16,18)) * 256 + hexbytestr2int(hexSettings.substring(18,20)); //           "segments": int(raw_settings[16:20], 16),
        recorderPatterns = hexbytestr2int(hexSettings.substring(28,30)); // "recorded_patterns": int(raw_settings[28:30], 16),
        whiteChannelBrightness =hexbytestr2int(hexSettings.substring(30,32)); //    "white_channel_brightness": int(raw_settings[30:32], 16)
    }

    public String toString() {
        return "Animation          : " + animation.getName() + "\n" +
                "Chip Type          : " + chiptype.getName() + "\n" +
                "Color Order        : " + colorOrder.getName() + "\n" +
                "Is On              : " + isOn + "\n" +
                "Color              : #" + color + "\n" +
                "Animation Speed    : " + (animationSpeed * 100 / 255) + "%\n" +
                "Brighness          : " + (brightness * 100 / 255) + "%\n" +
                "Nb LED per segment : " + ledsPerSegment + "\n" +
                "Nb segment         : " + segments + "\n" +
                "Nb record pattern  : " + recorderPatterns + "\n" +
                "White brightness   : " + (whiteChannelBrightness * 100 / 255) + "%";
    }

}
