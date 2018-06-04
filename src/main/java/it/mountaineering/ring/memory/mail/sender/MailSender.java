package it.mountaineering.ring.memory.mail.sender;

import java.awt.Image;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import javax.imageio.ImageIO;


public class MailSender {

	public void sendPropertiesExceptionEmail(String prefixException, String exception, String suffixString) {
		// TODO Auto-generated method stub
		
	}
	
	public static void main( String[] args )
    {
		
		try {
			MailSender.saveImage("http://111.0.0.1/record/current.jpg", "C:\\Users\\Lele\\Documents\\LavoroWebCamMobotix\\AVI_Exports\\test.jpg");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	Image image = null;
        try {
            URL url = new URL("http://www.mkyong.com/image/mypic.jpg");
            image = ImageIO.read(url);
        } catch (IOException e) {
        	e.printStackTrace();
        }

        //image.
    }
	
	
	public static void saveImage(String imageUrl, String destinationFile) throws IOException {
		URL url = new URL(imageUrl);
		InputStream is = url.openStream();
		OutputStream os = new FileOutputStream(destinationFile);

		byte[] b = new byte[2048];
		int length;

		while ((length = is.read(b)) != -1) {
			os.write(b, 0, length);
		}

		is.close();
		os.close();
	}
}
