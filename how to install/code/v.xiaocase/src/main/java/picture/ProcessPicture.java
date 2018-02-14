//Use google API to process picture---by XiaoCase
package picture;
import com.google.cloud.vision.spi.v1.ImageAnnotatorClient;
import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.BatchAnnotateImagesResponse;
import com.google.cloud.vision.v1.ColorInfo;
import com.google.cloud.vision.v1.DominantColorsAnnotation;
import com.google.cloud.vision.v1.EntityAnnotation;
import com.google.cloud.vision.v1.FaceAnnotation;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Feature.Type;
import com.google.cloud.vision.v1.Image;
import com.google.protobuf.ByteString;
import com.google.protobuf.Descriptors.FieldDescriptor;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class ProcessPicture {
	int anger = -1;		//-1 means this picture is not a face
	int joy = -1;
	int surprise = -1;
	int sorrow = -1;
	int PositiveorNegative = -1;
	ArrayList<String> keyword = new ArrayList<String>();
	Boolean hasLocation=false;		//if have location information
	public Picture picture;
	List<AnnotateImageResponse> responses;
	
	//main processing
	public ProcessPicture(String file) throws IOException, InterruptedException{
		System.out.println("Dectecting emotion...");
		detectEmotion(file);
		System.out.println("Dectecting location...");
		detectLocation(file);
		System.out.println("Dectecting keyword...");
		detectKeyWord(file);
		System.out.println("Dectecting color...");
		//if it is not a face
		if(anger==-1) detectColor(file);
		picture = new Picture(anger, joy, surprise, sorrow, PositiveorNegative, keyword, hasLocation);
		System.out.println(picture.toString());
	}
	
	//detect location
	public void detectLocation(String file) throws FileNotFoundException, IOException{
		//connect to Google API
		List<AnnotateImageRequest> requests = new ArrayList<>();
		ByteString imgBytes = ByteString.readFrom(new FileInputStream(file));
		Image img = Image.newBuilder().setContent(imgBytes).build();
		Feature feat = Feature.newBuilder().setType(Type.LANDMARK_DETECTION).build();
		AnnotateImageRequest request = AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
		requests.add(request);
		BatchAnnotateImagesResponse response = ImageAnnotatorClient.create().batchAnnotateImages(requests);			List<AnnotateImageResponse> responses = response.getResponsesList();
			
		for (AnnotateImageResponse res : responses) {
			if (res.hasError()) {
				System.out.printf("Error: %s\n", res.getError().getMessage());
				return;
		    }
			//get the location
		    for (EntityAnnotation annotation : res.getLandmarkAnnotationsList()) {
		    	if(annotation.getDescription()!=null) {
		    		keyword.add(annotation.getDescription().toLowerCase());
		    		hasLocation=true;
		    	}
			}
		}		
	}	
	
	//detect emotion
	public void detectEmotion(String file) throws IOException {
		//connect to Google API
		List<AnnotateImageRequest> requests = new ArrayList<AnnotateImageRequest>();
		ByteString imgBytes = ByteString.readFrom(new FileInputStream(file));
		Image img = Image.newBuilder().setContent(imgBytes).build();
		Feature feat = Feature.newBuilder().setType(Type.FACE_DETECTION).build();
		AnnotateImageRequest request = AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
		requests.add(request);
		BatchAnnotateImagesResponse response =ImageAnnotatorClient.create().batchAnnotateImages(requests);
		List<AnnotateImageResponse> responses = response.getResponsesList();

		for (AnnotateImageResponse res : responses) {
			if (res.hasError()) {
				System.out.printf("Error: %s\n", res.getError().getMessage());
				return;
		    }
		    for (FaceAnnotation annotation : res.getFaceAnnotationsList()) {
		    	//grade anger
		    	if(annotation.getAngerLikelihood().toString()=="VERY_LIKELY") anger=10;
		    	else if(annotation.getAngerLikelihood().toString()=="LIKELY") anger=8;
		    	else if(annotation.getAngerLikelihood().toString()=="POSSIBLE") anger=6;
		    	else if(annotation.getAngerLikelihood().toString()=="UNLIKELY") anger=4;
		    	else if(annotation.getAngerLikelihood().toString()=="VERY_UNLIKELY") anger=2;
		    	//grade joy
		    	if(annotation.getJoyLikelihood().toString()=="VERY_LIKELY") joy=10;
		    	else if(annotation.getJoyLikelihood().toString()=="LIKELY") joy=8;
		    	else if(annotation.getJoyLikelihood().toString()=="POSSIBLE") joy=6;
		    	else if(annotation.getJoyLikelihood().toString()=="UNLIKELY") joy=4;
		    	else if(annotation.getJoyLikelihood().toString()=="VERY_UNLIKELY") joy=2;
		    	//grade surprise
		    	if(annotation.getSurpriseLikelihood().toString()=="VERY_LIKELY") surprise=10;
		    	else if(annotation.getSurpriseLikelihood().toString()=="LIKELY") surprise=8;
		    	else if(annotation.getSurpriseLikelihood().toString()=="POSSIBLE") surprise=6;
		    	else if(annotation.getSurpriseLikelihood().toString()=="UNLIKELY") surprise=4;
		    	else if(annotation.getSurpriseLikelihood().toString()=="VERY_UNLIKELY") surprise=2;
		    	//grade sorrow
		    	if(annotation.getSorrowLikelihood().toString()=="VERY_LIKELY") sorrow=10;
		    	else if(annotation.getSorrowLikelihood().toString()=="LIKELY") sorrow=8;
		    	else if(annotation.getSorrowLikelihood().toString()=="POSSIBLE") sorrow=6;
		    	else if(annotation.getSorrowLikelihood().toString()=="UNLIKELY") sorrow=4;
		    	else if(annotation.getSorrowLikelihood().toString()=="VERY_UNLIKELY") sorrow=2;
		    	//grade positive and negative 
		    	PositiveorNegative=(int)(annotation.getDetectionConfidence()*10);
		          /*
		          VERY_LIKELY    	10
		          LIKELY			8	
		          POSSIBLE			6
		          UNLIKELY			4
		          VERY_UNLIKELY 	2
		          */
		    }  
		}
	}
	
	//detect keyword
	public void detectKeyWord(String file) throws IOException {
		//connect to Google API
		List<AnnotateImageRequest> requests = new ArrayList<AnnotateImageRequest>();
		ByteString imgBytes = ByteString.readFrom(new FileInputStream(file));
		Image img = Image.newBuilder().setContent(imgBytes).build();
		Feature feat = Feature.newBuilder().setType(Type.LABEL_DETECTION).build();
		AnnotateImageRequest request =AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
		requests.add(request);
		BatchAnnotateImagesResponse response =ImageAnnotatorClient.create().batchAnnotateImages(requests);
		List<AnnotateImageResponse> responses = response.getResponsesList();
		for (AnnotateImageResponse res : responses) {
			if (res.hasError()) {
				System.out.printf("Error: %s\n", res.getError().getMessage());
				return;
			}
			//get key word
			for (EntityAnnotation annotation : res.getLabelAnnotationsList()) {
				int count=1;
				Map<FieldDescriptor, Object> map = annotation.getAllFields();
				for(Entry<FieldDescriptor, Object> entry : map.entrySet()){
					if(count%3==2 && entry!=null) keyword.add(entry.getValue().toString());
					count++;
				}
			}
		}
	}	

	//detect color
	public void detectColor(String file) throws FileNotFoundException, IOException{
		List<AnnotateImageRequest> requests = new ArrayList<>();
		ByteString imgBytes = ByteString.readFrom(new FileInputStream(file));
		Image img = Image.newBuilder().setContent(imgBytes).build();
		Feature feat = Feature.newBuilder().setType(Type.IMAGE_PROPERTIES).build();
		AnnotateImageRequest request = AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
		requests.add(request);
		BatchAnnotateImagesResponse response =ImageAnnotatorClient.create().batchAnnotateImages(requests);
		List<AnnotateImageResponse> responses = response.getResponsesList();
		for (AnnotateImageResponse res : responses) {
			if (res.hasError()) {
				System.out.printf("Error: %s\n", res.getError().getMessage());
				return;
		    }

		    DominantColorsAnnotation colors = res.getImagePropertiesAnnotation().getDominantColors();
		    //RGB -> Luma conversion formula: sqrt(0.299 * R^2 + 0.587 * G^2 + 0.114 * B^2)
		    double sum=0;
		    double Lsum=0;
		    for (ColorInfo color : colors.getColorsList()) {
		    	sum+=color.getScore();
		    	Lsum+=color.getScore()*Math.sqrt(0.299*Math.pow(color.getColor().getRed(),2)
		    									+0.587*Math.pow(color.getColor().getGreen(),2)
		    									+0.114*Math.pow(color.getColor().getBlue(),2));	
		    }
		    //sqrt(0.299 * 255^2 + 0.587 * 255^2 + 0.114 * 255^2)
		    double light=255;
		    PositiveorNegative= (int)((Lsum/(sum*light))*10)+1;
		}
	}
	
	//test
//	public static void main(String[] args) throws IOException, InterruptedException {
//		@SuppressWarnings("unused")
//		ProcessPicture pp = new ProcessPicture("C:/Users/XiaoCase/Desktop/image/sailboats-in-boston-0a5755ca.jpg");
//	}
}
