# SP108E for Java

Get the source files, and copy them into your project.

Here below a sample API show up

    import sp108e.*;
    
    class demoSP108E {
    
	    public static void main(String argc[]) {
		    // create a controller on the expected IP 
		    // ⇒ my local DHCP provides fix IP address 
		    //    based on device MAC address
		    try {
		        Controller ctrl = new Controller("192.168.99.41"); // my SP108E
		        // switch Off 
		        ctrl.switchOff();
		        // switch On
		        ctrl.switchOn();
		        // Toggle between On and Off (and vice-versa)
		        ctrl.doToggleOnOff();
		        // Set animation by id
		        ctrl.setAnimation(0); // 0 == Rainbow
		        // set animation by name
		        ctrl.setAnimation("Rainbow"); // case insensitive
		        // set animation by object
		        ctrl.setAnimation(Animation.RAINBOW);
		        // Change color before to select a monochronmatic animation
		        ctrl.ChangeColor("#FF0000"); // red
		        ctrl.setAnimation(Animation.METEOR); // meteor monochromatic animation in red
		        // Change animation speed
		        ctrl.changeSpeed(200); // from 0 (very slow) top 255 (fastest)
		        ctrl.changeBrightness(150); // from 0 (like off) to 255 (brightest)
		        // Get device name
		        System.out.println("Device name :" + ctrl.getName());
		        String isReady = ctrl.isDeviceReady(); // is "1" (49 value byte) to indicate that device is ready to receive a command or data
		        // get device settings (Settings object ) 
			    Settings s = ctrl.getDeviceSettings();
			    System.out.println("Settings:\n" + s); // s.toString() provides a human readable multi lines string
			    
	        catch (Exception e) {
		        e.printStackTrace();
	        }
	    }
    }