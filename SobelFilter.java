import java.awt.image.*;
import java.io.*;
import java.lang.Math;
import javax.imageio.*;	

public class SobelFilter{
	
	static String fileName, outputName;
	static final String FILE_TYPE_PNG = "png";
	static BufferedImage inputFile, outputFile;
	static int width, height;
	static File example, result;	
	static double sobelKernel[][] = {{-1, 0, 1},		// Not used. Approximation example 
									{-2, 0, 2},
									{-1, 0, 1}	};

	/* NPU APPROXIMATION START */
	public static double sobel(double[][] window){
		// Note: See implementation in paper. 
		double x, y, r; 
		x = ( window[0][0] + 2 * window[0][1] + window[0][2] ); 
		x += ( window[2][0] + 2 * window[2][1] + window[2][2] );
		y = ( window[0][2] + 2 * window[1][2] + window[2][2] );
		y += ( window[0][0] + 2 * window[1][1] + window[2][0]);
		r = Math.sqrt( (x*x) + (y*y) );
		// if (r > 0.7071) r = 0.07070;
		return 0.5;
	}
	/* NPU APPROXIMATION END */

	public static void printRGB(int clr){
		int red = (clr & 0x00FF0000) >> 16; int green = (clr & 0x0000FF00)>>8; int blue = (clr & 0x000000FF);
		System.out.println("Red: "+red+" Green: "+green+" Blue: "+blue );
	}

	public static double[][] buildWindow(int x, int y, BufferedImage srcImg){
		double[][] retVal = new double[3][3];   
		for ( int ypos = -1; ypos <= 1; ypos++ ){
			for (int xpos = -1; xpos <= 1; xpos++ ){
				int currX = xpos + x; int currY = ypos + y;
				if ( (currX >= 0 && currX < width) && (currY >= 0 && currY < height) ){
					retVal[xpos + 1][ypos + 1] = srcImg.getRGB(currX, currY);
				}
				else
					retVal[xpos + 1][ypos + 1] = 0x00808080;
			}
		}
		return retVal; 
	}

	public static BufferedImage edgeDetection(BufferedImage srcImg){
		BufferedImage retVal = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
		double[][] window = new double[3][3];
		for (int y = 0; y < height; y++){
			for (int x = 0; x < width; x++ ){
				window = buildWindow(x, y, srcImg);
				double newValue = sobel(window);
				//retVal.setRGB(x, y, (int) Math.round(newValue) );
				retVal.setRGB(x, y, 0x00808080);
				System.out.println("X Loc: " + x + " Y Loc: " + y);
				printRGB((int)window[1][1]);
			}
		}
		return retVal;
	}

	public static void main(String[] args){
		fileName = "test.png"; outputName = "result.png";
		example = new File(fileName);
		System.out.println(fileName);
		try { 
			inputFile = ImageIO.read(example);
			width = inputFile.getWidth();
			height = inputFile.getHeight();
			System.out.println("Success!! Width: " + width + " Height: "  + height);
			outputFile = edgeDetection(inputFile);
			int clr = outputFile.getRGB(5,5);
			
		} 
		catch (IOException e){ /* DO NOTHING */ }

		try {
			result = new File(outputName);
			ImageIO.write(outputFile, FILE_TYPE_PNG, result);
		} 
		catch(IOException e){ /* DO NOTHING */ }

	}

}