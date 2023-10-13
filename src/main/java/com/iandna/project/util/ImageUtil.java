package com.iandna.project.util;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.jpeg.JpegDirectory;
import org.apache.commons.lang.StringUtils;
import org.imgscalr.Scalr;
import org.krysalis.barcode4j.impl.int2of5.Interleaved2Of5Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component(value = "imgUtil")
public class ImageUtil {
	
	@Value("${s3.server.file.path}")
	private String savePath;
	@Value("${s3.server.file.url}")
	private String serverFileUrl;
	@Value("${s3.upload.bucket}")
	private String s3UploadBucket;
	
	private static final Logger logger = LoggerFactory.getLogger(ImageUtil.class);
	
	/**
	* 바코드 생성
	* @param barcodeType
	* @param barcodeData
	* @param dpi
	 * @throws Exception 
	*/
	public static void createBarcode(String barcodeData, String path) throws Exception {
		String outputFile = path + "/" + barcodeData + ".png";
		
		try {
			Interleaved2Of5Bean bean = new Interleaved2Of5Bean();

		    bean.setHeight(10d);

		    bean.doQuietZone(false);

		    OutputStream out = new FileOutputStream(new File(outputFile));

		    BitmapCanvasProvider provider = new BitmapCanvasProvider(out, "image/x-png", 110, BufferedImage.TYPE_BYTE_GRAY, false, 0);
		    bean.generateBarcode(provider, barcodeData);

		    provider.finish();
		} catch (Exception e) {
			logger.error("바코드 생성 중 오류가 발생하였습니다.", e);
			throw e;
		}
	}

	/**
	 * Rotates an image. Actually rotates a new copy of the image.
	 * 
	 * @param img The image to be rotated
	 * @param angle The angle in degrees
	 * @return The rotated image
	 */
	public static BufferedImage rotate(Image img, double angle) {
		double sin = Math.abs(Math.sin(Math.toRadians(angle))),
			   cos = Math.abs(Math.cos(Math.toRadians(angle)));

		int w = img.getWidth(null), h = img.getHeight(null);

		int neww = (int) Math.floor(w*cos + h*sin),
			newh = (int) Math.floor(h*cos + w*sin);

		BufferedImage bimg = toBufferedImage(getEmptyImage(neww, newh));
		Graphics2D g = bimg.createGraphics();

		g.translate((neww-w)/2, (newh-h)/2);
		g.rotate(Math.toRadians(angle), w/2, h/2);
		g.drawRenderedImage(toBufferedImage(img), null);
		g.dispose();

		return bimg;
	}

	/**
	 * Converts a given Image into a BufferedImage
	 * 
	 * @param img The Image to be converted
	 * @return The converted BufferedImage
	 */
	public static BufferedImage toBufferedImage(Image img){
		if (img instanceof BufferedImage) {
			return (BufferedImage) img;
		}
		// Create a buffered image with transparency
		BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		// Draw the image on to the buffered image
		Graphics2D bGr = bimage.createGraphics();
		bGr.drawImage(img, 0, 0, null);
		bGr.dispose();
		// Return the buffered image
		return bimage;
	}
	
	/**
	 * Creates an empty image with transparency
	 * 
	 * @param width The width of required image
	 * @param height The height of required image
	 * @return The created image
	 */
	public static Image getEmptyImage(int width, int height){
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		return toImage(img);
	}
	
	/**
	 * Converts a given BufferedImage into an Image
	 * 
	 * @param bimage The BufferedImage to be converted
	 * @return The converted Image
	 */
	public static Image toImage(BufferedImage bimage){
		// Casting is enough to convert from BufferedImage to Image
		Image img = (Image) bimage;
		return img;
	}
	
	/**
	 * 2019.07.19 dhkim : imageResize
	 * @param img
	 * @param height
	 * @param width
	 * @return
	 */
	public static BufferedImage resize(BufferedImage img, int height, int width) {
        Image tmp = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resized.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return resized;
    }
	
	public MultipartFile imgResizeByWidth(MultipartFile file, int width) throws IOException {
		// file resize
		MultipartFile result = null;
		File ioFile = convertMfileToFile(file);
//		FileInputStream fis = new FileInputStream(ioFile);  
		
		InputStream in = file.getInputStream(); 
//		BufferedImage img = ImageIO.read(file.getInputStream());
//		BufferedImage img = ImageIO.read(fis);
		BufferedImage img = ImageIO.read(in);
		if(img == null) {
			logger.error("################# imgResizeByWidth");
			logger.error(ioFile.getName());
			logger.error(file.getOriginalFilename());
			logger.error(ioFile.toString());
			logger.error(""+file.getSize());
			return file;
		}
		int ow = img.getWidth();
		int oh = img.getHeight();
		int orientation = 1; // 1=0 3=180 6=270 8=90
		
		Metadata metadata; // 이미지 메타 데이터 객체
		Directory directory; // 이미지의 Exif 데이터를 읽기 위한 객체
		JpegDirectory jpegDirectory; // JPG 이미지 정보를 읽기 위한 객체
		
		try {
			metadata = ImageMetadataReader.readMetadata(file.getInputStream());
			directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
			jpegDirectory = metadata.getFirstDirectoryOfType(JpegDirectory.class);
			if(directory != null){
				orientation = directory.getInt(ExifIFD0Directory.TAG_ORIENTATION); // 회전정보
			}
	 
		}catch (Exception e) {
			orientation=1;
		}
		
		switch (orientation) {
		case 6:
			img = Scalr.rotate(img, Scalr.Rotation.CW_90, null); 
			break;
		case 1:
	 
			break;
		case 3:
			img = Scalr.rotate(img, Scalr.Rotation.CW_180, null);
			break;
		case 8:
			img = Scalr.rotate(img, Scalr.Rotation.CW_270, null);
			break;
	 
		default:
			orientation=1;
			break;
		}
		
		BufferedImage destImg = null;
		
		// 이미지 잘라내기
//		BufferedImage cropImg = Scalr.crop(img, (ow-nw)/2, (oh-nh)/2, nw, nh);
//		BufferedImage cropImg = Scalr.crop(img, 0, 0, nw, nh);
		// 원하는 크기로 리사이즈
//		BufferedImage destImg = Scalr.resize(img, width, height);
		if(ow > width) {
			// 원본 비율 유지 - 가로기준
			destImg = Scalr.resize(img, Scalr.Method.AUTOMATIC, Scalr.Mode.FIT_TO_WIDTH, width);
		}else {
			destImg = img;
		}

		// 원본 비율 유지 - 세로기준
//		BufferedImage destImg = Scalr.resize(img, Scalr.Method.AUTOMATIC, Scalr.Mode.FIT_TO_HEIGHT, height);

		/*
        Image tmp = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resized.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        // resize end
        */
		
        // make file name & extend name
        String fileName = makeFileName(file.getOriginalFilename(), "review");
        String exName = StringUtils.substring(fileName, StringUtils.lastIndexOf(fileName, ".")+1);
        // file name & extend name end
        // jpg, png 모두 png로 저장 (jpg 색상, 투명도 변질)
        /*
        if(exName.equals("png") || exName.equals("jpg") || exName.equals("JPG") || exName.equals("jpeg") || exName.equals("JPEG") || exName.equals("PNG")) {
        	exName = "png";	
        }
        */
        File rszFile = imgToFile(destImg, this.savePath, fileName, exName);
        FileInputStream input = new FileInputStream(rszFile);
        result = new MockMultipartFile(fileName, input);
        return result;
    }

	/*
	public MultipartFile resize_jpeg(MultipartFile file, int height, int width) throws IOException {
		BufferedImage img = null;
		com.sun.image.codec.jpeg.JPEGImageDecoder jpegDecoder = null;
		try {
			jpegDecoder = JPEGCodec.createJPEGDecoder(new FileInputStream(filePath));
		} catch (FileNotFoundException e2) {
			e2.printStackTrace();
		}
		
		// file resize
		MultipartFile result = null; 
		BufferedImage img = ImageIO.read(file.getInputStream());
        Image tmp = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resized.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        // resize end
        
        // make file name & extend name
        String fileName = makeFileName(file.getOriginalFilename(), "review");
        logger.debug("in resize fileName : " + fileName);
        String exName = StringUtils.substring(fileName, StringUtils.lastIndexOf(fileName, ".")+1);
        // file name & extend name end
        
        File rszFile = imgToFile(resized, this.prop.getProperty("server.file.path"), fileName, exName);
        FileInputStream input = new FileInputStream(rszFile);
        result = new MockMultipartFile(fileName, input);
        return result;
    }
	*/
	
	/**
	 * 2019.07.19 dhkim : MultipartFile to File
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static File multipartFileToFile(MultipartFile file) throws IOException {
		 
	    File convFile = new File(file.getOriginalFilename());
	    convFile.createNewFile();
	    FileOutputStream fos = new FileOutputStream(convFile);
	    fos.write(file.getBytes());
	    fos.close();
	 
	    return convFile;
	  }
	
	public static File imgToFile(BufferedImage img, String filePath, String fileName, String ext) throws IOException {
		File outputFile = null;
		try {
			outputFile = new File(filePath+fileName);
			/*
			if(ext.equals("jpg")) {
				oufputFile = new File(outputFile)
			}else {
				ImageIO.write(img, ext, outputFile);
			}
			*/
			ImageIO.write(img, ext, outputFile);
//			ImageIO.write(img, "png", outputFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return outputFile;
	}
	
	public static String makeFileName(String originFileName, String headStr) {
		long time = System.currentTimeMillis();
        SimpleDateFormat day = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat dayTime = new SimpleDateFormat("HHmmss");
        String str = day.format(new Date(time));
		String str2 = dayTime.format(new Date(time));
        String exName = StringUtils.substring(originFileName,
				StringUtils.lastIndexOf(originFileName, "."));
		String fileName = headStr + "_" + str + str2 + "_img" + exName;
		return fileName;
	}
	
	public File convertMfileToFile(MultipartFile mFile) throws IOException {
		File file = new File(mFile.getOriginalFilename());
		file.createNewFile();
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(mFile.getBytes());
		fos.close();
		return file;
	}
	
	public static void main(String[] args) {
		String originFileName = "abcd.jpg";
		logger.debug(makeFileName(originFileName, "test"));
		
		logger.debug(StringUtils.substring(makeFileName(originFileName, "test"),
				StringUtils.lastIndexOf(makeFileName(originFileName, "test"), ".")));
	}
}
