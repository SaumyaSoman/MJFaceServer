package edu.rutgers.winlab.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;


import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

import edu.rutgers.winlab.EntityDetect;
import edu.rutgers.winlab.ImageSearch;
import edu.rutgers.winlab.javafaces.FaceDetect;
import edu.rutgers.winlab.javafaces.FaceRecognition;
import edu.rutgers.winlab.response.Annotations;
import edu.rutgers.winlab.response.SearchResponse;
import edu.rutgers.winlab.response.WSResponse;


@Path("/image")

/**
 * REST web service which receives image, does face/object detection and recognition, color extraction and google search.
 * The response is text(object/person) and search results.
 * @author Saumya
 *
 */
public class ImageService {

	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces("application/json")
	public WSResponse uploadFile(@FormDataParam("image") InputStream uploadedInputStream,
			@FormDataParam("image") FormDataContentDisposition fileDetail) {

		//save the received image with this name
		String uploadedFileLocation = "C://Users/Saumya/Pictures/pics/" + fileDetail.getFileName();

		WSResponse response=new WSResponse();
		try{
			long startTime=System.currentTimeMillis();
			saveToFile(uploadedInputStream, uploadedFileLocation);
			FaceDetect faceDetect=new FaceDetect();
			faceDetect.run(uploadedFileLocation);
			FaceRecognition recognition= new FaceRecognition();
			Set<String> names=recognition.recognize();
			for (String name : names) {
				System.out.println(name);
			}
			
			String output="";
			if(names==null){
				output= "Cannot be recognized";
			}else{
				//if object/face is identified, do google search
				ArrayList<Annotations> annotations=new ArrayList<Annotations>();
				for (String name : names) {
					Annotations annotation=new Annotations();
					//ImageSearch search=new ImageSearch();
					//ArrayList<SearchResponse> searchResp=search.getSearchResults(uploadedFileLocation,output);
					annotation.setText(name);
					annotations.add(annotation);
				}
				response.setAnnotations(annotations);
			}
			response.setResult(output);
			long endTime=System.currentTimeMillis();
			System.out.println("\ntotal time taken="+(endTime-startTime)+" millisecs");
		}catch(Exception e){
			e.printStackTrace();
		}
		return response;

	}
public static void main(String[] args) {
	try{
		String uploadedFileLocation = "C://Users/Saumya/Pictures/pics/sara1.jpg";
		long startTime=System.currentTimeMillis();
		FaceDetect faceDetect=new FaceDetect();
		faceDetect.run(uploadedFileLocation);
		FaceRecognition recognition= new FaceRecognition();
		Set<String> names=recognition.recognize();
		for (String name : names) {
			System.out.println(name);
		}
		long endTime=System.currentTimeMillis();
		System.out.println("\ntotal time taken="+(endTime-startTime)+" millisecs");
	}catch(Exception e){
		e.printStackTrace();
	}
}
	/**
	 * Method to save uploaded file to new location
	 * @param uploadedInputStream InputStream
	 * @param uploadedFileLocation string
	 */
	private void saveToFile(InputStream uploadedInputStream,String uploadedFileLocation) {

		try {
			OutputStream out = null;
			int read = 0;
			byte[] bytes = new byte[1024]; 
			out = new FileOutputStream(new File(uploadedFileLocation));
			while ((read = uploadedInputStream.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}
			out.flush();
			out.close();
		} catch (IOException e) {

			e.printStackTrace();
		}

	}

}

