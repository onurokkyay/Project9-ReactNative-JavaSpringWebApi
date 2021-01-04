package com.melih.image;

import java.awt.BasicStroke;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import javax.swing.Box.Filler;

import org.omg.CORBA.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.api.Page;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.storage.Acl;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.BatchAnnotateImagesResponse;
import com.google.cloud.vision.v1.EntityAnnotation;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Image;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.cloud.vision.v1.ImageSource;
import com.google.cloud.vision.v1.LocalizedObjectAnnotation;
import com.google.common.collect.Lists;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.StorageClient;
import com.google.cloud.vision.v1.Feature.Type;
import com.google.protobuf.ByteString;

@CrossOrigin
@RestController
public class imageprocess {
	static int i = 0;
	static float[][] coordinate = new float[400][2];
	static person Global_p=new person();
	static int Total_Object_Count=0;
	


	@RequestMapping(value = "ImageProcess", method = RequestMethod.POST)
	public static person detectLocalizedObjectsGcs(@RequestBody person p) throws IOException, ClassNotFoundException, IllegalAccessException, NoSuchFieldException {
		resetGlobalPersonandCoodinates();		
	System.out.println("GELEN PERSON VERİSİ"+"imageName :"+p.getImageName());	
	System.out.println("GELEN PERSON VERİSİ"+"downloadURL :"+p.getDownloadURL());	

	if(p.getDownloadURL()==null || p.getImageName()==null ) {
		System.out.println("HATALI BİLGİ GELDİ");
	}
		
		Global_p=p;		
		
		String URL_Image="gs://nesnealgilama.appspot.com/avatars/"+Global_p.getImageName();
		List<AnnotateImageRequest> requests = new ArrayList<>();

		ImageSource imgSource = ImageSource.newBuilder().setGcsImageUri(URL_Image).build();
		Image img = Image.newBuilder().setSource(imgSource).build();

		AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
				.addFeatures(Feature.newBuilder().setType(Type.OBJECT_LOCALIZATION)).setImage(img).build();
		requests.add(request);

	
		try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {

			BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
			List<AnnotateImageResponse> responses = response.getResponsesList();
			client.close();
		
			for (AnnotateImageResponse res : responses) {
				for (LocalizedObjectAnnotation entity : res.getLocalizedObjectAnnotationsList()) {
					System.out.format("Object name: %s%n", entity.getName());				
					System.out.format("Confidence: %s%n", entity.getScore());
					System.out.format("Normalized Vertices:%n");
					Global_p.setAllObjects(entity.getName());
					
					entity.getBoundingPoly().getNormalizedVerticesList()
							.forEach(vertex -> fillingCoordinates(vertex.getX(), vertex.getY()));
				}
			}
		}
		drawThePictureRemote(Global_p.getDownloadURL());
		return Global_p;
	}
	


	public static void fillingCoordinates(float x, float y) {

		coordinate[i][0] = x;
		coordinate[i][1] = y;
		System.out.println("NOKTA " + i + " " + coordinate[i][0] + "" + coordinate[i][1]);
		i++;
		Total_Object_Count++;

	}


	public static void drawThePictureRemote(String download_URL) throws IOException {
		 BufferedImage myPicture = ImageIO.read(new URL(download_URL));

		Graphics2D g = (Graphics2D) myPicture.getGraphics();
		g.setStroke(new BasicStroke(3));
		g.setColor(Color.BLUE);

		for (i = 0; i < Total_Object_Count; i++) {
			g.draw(new Line2D.Double(coordinate[i][0] * myPicture.getWidth(), coordinate[i][1] * myPicture.getHeight(),
					coordinate[i + 1][0] * myPicture.getWidth(), coordinate[i + 1][1] * myPicture.getHeight()));
			g.draw(new Line2D.Double(coordinate[i + 1][0] * myPicture.getWidth(),
					coordinate[i + 1][1] * myPicture.getHeight(), coordinate[i + 2][0] * myPicture.getWidth(),
					coordinate[i + 2][1] * myPicture.getHeight()));
			g.draw(new Line2D.Double(coordinate[i + 2][0] * myPicture.getWidth(),
					coordinate[i + 2][1] * myPicture.getHeight(), coordinate[i + 3][0] * myPicture.getWidth(),
					coordinate[i + 3][1] * myPicture.getHeight()));
			g.draw(new Line2D.Double(coordinate[i + 3][0] * myPicture.getWidth(),
					coordinate[i + 3][1] * myPicture.getHeight(), coordinate[i][0] * myPicture.getWidth(),
					coordinate[i][1] * myPicture.getHeight()));
			i += 3;

		}
		/*
		 * Local test icin
		File outputfile = new File("C:\\Users\\melih\\Desktop\\islenmis_cikti.jpg");
		ImageIO.write(myPicture, "jpg", outputfile);
		*/
		
		uploadObject(myPicture);
	}
	
	
	public static void uploadObject(BufferedImage image ) throws IOException {


		ByteArrayOutputStream os = new ByteArrayOutputStream();
		ImageIO.write(image,"jpg", os); 
		    Storage storage = StorageOptions.newBuilder().setProjectId("nesnealgilama").build().getService();
		    BlobId blobId = BlobId.of("nesnealgilama.appspot.com", "avatars/Islenmis"+Global_p.getImageName()+".jpg");
		    
		    Map<String, String> map = new HashMap<>();
	        map.put("firebaseStorageDownloadTokens", "x");
		    
		    BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setMetadata(map).setContentType("image/jpg").build();
		    
		    storage.create(blobInfo,os.toByteArray() );
		    Global_p.setTotalObjects(Total_Object_Count);
		    Total_Object_Count=0;
		   
		  }
	

	public static void resetGlobalPersonandCoodinates() {
		i=0;
		Global_p.resetAllObjects();
		Global_p.setTotalObjects(0);
	}
}
