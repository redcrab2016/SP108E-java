package sp108e;


import java.net.Socket;
import java.net.InetSocketAddress;

import kotlin.text.Charsets;

public class Controller {
    private final String ctrl_address;
    private final int ctrl_port;

    public Controller(String address, int port) {
        ctrl_address = address;
        ctrl_port = port;
    }

    public Controller(String address) {
        this(address, 8189);
    }

    private static byte[] a2bHex(String argbuf) throws Exception {
        int arglen = argbuf.length();

        /* XXX What should we do about strings with an odd length?  Should
         * we add an implicit leading zero, or a trailing zero?  For now,
         * raise an exception.
         */
        if (arglen % 2 != 0) throw new Exception("Odd-length string");
        StringBuilder retbuf = new StringBuilder(arglen / 2);
        int i = 0;
        while (i < arglen) {
            int top = Character.digit(argbuf.charAt(i), 16);
            int bot = Character.digit(argbuf.charAt(i + 1), 16);
            if (top == -1 || bot == -1) throw new Exception("Non-hexadecimal digit found");
            retbuf.append((char) ((top << 4) + bot));
            i += 2;
        }
        return retbuf.toString().getBytes(Charsets.ISO_8859_1);

    }

    private static String dec2evenHex(int decimal) {
        return dec2evenHex(decimal, -1);
    }

    private static String dec2evenHex(int decimal, int outputByte) {
        StringBuilder result = new StringBuilder(Integer.toHexString(decimal));
        if (result.length() % 2 == 1) result.insert(0, "0");
        if (result.length() < (outputByte * 2) && outputByte > 0)
            for (int i = 1; i <= ((outputByte * 2) - result.length()); i++) {
                result.insert(0, "00");
            }
        return result.toString();
    }

    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    private static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xff;
            hexChars[j * 2] = HEX_ARRAY[v >> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

    private void transmitData(String data) throws Exception {
        transmitData(data, false, 0);
    }

    private byte[] transmitData(
            String data,  // the command and it's value(if it has a value)
            boolean expect_response,  // set to true if you expect a response
            int response_length //# how long the response is
    ) throws Exception {
        Socket s = new Socket();
        s.connect(new InetSocketAddress(ctrl_address, ctrl_port), 1000);
        String cleanedData = data.replace(" ", "");

        s.getOutputStream().write(a2bHex(cleanedData));
        s.getOutputStream().flush();

        byte[] buffer;
        if (expect_response) {
            buffer = new byte[response_length];
            int i = s.getInputStream().read(buffer, 0, response_length);
            if (i != response_length) throw new Exception("Unexpected response data length");
        } else {
            buffer = new byte[0];
        }

        s.close();
        return buffer;
    }

    /**
     * Do toggle On and Off
     *
     * @throws Exception when transmission failed
     */
    public void doToggleOnOff() throws Exception {
        transmitData("38 000000 aa 83");
    }

    /**
     * Do swith on if the device settings indicate that it is off
     * @throws Exception if transmission failed
     */
    public void switchOn() throws Exception {
        Settings s = getDeviceSettings();
        if (s.isOff()) doToggleOnOff();
    }

    /**
     * Do switch off if the device settings indicate that it is on
     * @throws Exception if transmission failed
     */
    public void switchOff() throws Exception {
        Settings s = getDeviceSettings();
        if (s.isOn()) doToggleOnOff();
    }

    /**
     * Set animation by giving the code
     * @param anim integer SP108E animation number
     * @throws Exception failed communication or bad animation number
     */
    public void setAnimation(int anim) throws Exception {
        setAnimation(Animation.getByCode(anim));
    }

    /**
     * Set animation by giving the name
     * @param anim String SP108 animation name (can be official or custom name)
     * @throws Exception failed communication or bad animation name
     */
    public void setAnimation(String anim) throws Exception {
        setAnimation(Animation.getByName(anim));
    }

    /**
     * Set animation
     * @param animation Animation enumeration object
     * @throws Exception bad communication or Null value animation provided
     */
    public void setAnimation(Animation animation) throws Exception {
        transmitData("38 "+ animation.getHexCode() + " 0000 2c 83");
    }

    /**
     * change the color for the monochrome animation
     * to be set before to choose a monochrome animation
     * @param color in hexa string format RRGGBB or #RRGGBB ,
     *              example red is FF0000, green is 00FF00
     *              blue is 0000FF
     * @throws Exception when transmission failed
     */
    public void changeColor(String color) throws Exception {
       // color in hex format, be sure to have no # character
        color = color.replace("#", "");
        if (color.length() != 6 ) throw new Exception ("Bad color descriptor : " + color);
        transmitData("38 " + color + " 22 83");
    }

    /**
     * change the animation speed
     * @param speed  between 0 and 255
     * @throws Exception when transmission failed or wrong speed value
     */
    public void changeSpeed(int speed) throws Exception {
        if (speed < 0 || speed > 255) throw new Exception ("speed must be between 0 and 255");
        transmitData("38 " + dec2evenHex(speed) +" 0000 03 83 ");
    }

    /**
     * change the brightness
     * @param brightness between 0 and 255
     * @throws Exception when transmission failed or wrong brightness value
     */
    public void changeBrightness(int brightness) throws Exception {
        if (brightness < 0 || brightness > 255) throw new Exception ("brightness must be between 0 and 255");
        transmitData("38 " + dec2evenHex(brightness) + " 0000 2a 83");
    }

    /**
     * get SP108E device name
     * @return filtered device name
     * @throws Exception when translission failed
     */
    public String getName() throws Exception {
        byte[] result = transmitData("38 000000 77 83", true, 18);
        StringBuilder str = new StringBuilder();
        for (byte b :  result) {
            if (b != 0) {
                str.append((char)b);
            }
        }
        return str.toString();
    }

    /**
     * Test is the device is ready to receive command
     * @return "1" (byte value 49) if it is ready
     * @throws Exception when transmission failed
     */
   public String isDeviceReady() throws Exception {
        byte[] result = transmitData("38 000000 2f 83", true, 1);
        return  String.valueOf((char)result[0]);
   }

    private String getDeviceRawSettings() throws Exception {
        byte [] result = transmitData("38 000000 10 83", true, 17);
        return bytesToHex(result);
    }

    private Settings getDeviceSettings() throws Exception {
       return new Settings(getDeviceRawSettings());
    }

    public static void main(String[] args) {
        Controller c = new Controller("192.168.99.41");
        try {
            // c.doToggleOnOff();
            // c.changeSpeed(0);
            // c.changeBrightness(200);
            c.setAnimation(0);
            System.out.println(c.getDeviceSettings());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
