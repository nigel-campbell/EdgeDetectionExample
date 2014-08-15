import java.awt.image.*;
import java.io.*;
import javax.imageio.*;	

public class SobelFilter{
	
	static String fileName, outputName;
	static final String FILE_TYPE_PNG = "png";
	static BufferedImage inputFile, outputFile;
	static int width, height;
	static File example, result;

	/* APPROXIMATION START */
	public static double sobel(double[][] window){
		double sobelKernel[][] = {	{-1, 0, 1},
									{-2, 0, 2},
									{-1, 0, 1}	};


		return 0;
	}
	/* APPROXIMATION END */

	public static double[][] buildWindow(int x, int y, BufferedImage srcImg){
		double[][] retVal = new double[3][3];   
		for ( int ypos = -1; ypos <= 1; ypos++ ){
			for (int xpos = -1; xpos <= 1; xpos++ ){
				int currX = xpos + x; int currY = ypos + y;
				if ( (currX >= 0 && currX < width) && (currY >= 0 && currY < height) ){
					retVal[xpos + 1][ypos + 1] = srcImg.getRGB(currX, currY);
				}
				else
					retVal[xpos + 1][ypos + 1] = 0;
			}
		}
		return retVal; 

	}

	public static BufferedImage edgeDetection(BufferedImage srcImg){
		BufferedImage retVal = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		double[][] window = new double[3][3];
		for (int y = 0; y < height; y++){
			for (int x = 0; x < width; x++ ){
				window = buildWindow(x, y, srcImg);
				double newValue = 0;
				retVal.setRGB(x,y, (int) newValue);
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
		} 
		catch (IOException e){ /* DO NOTHING */ }

		try {
			result = new File(outputName);
			ImageIO.write(outputFile, FILE_TYPE_PNG, result);
		} 
		catch(IOException e){ /* DO NOTHING */ }

	}

}