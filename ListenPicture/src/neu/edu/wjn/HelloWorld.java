package neu.edu.wjn;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.InputStream;


/**
 * Servlet implementation class HelloWorld
 */
@WebServlet("/HelloWorld")
public class HelloWorld extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public ArrayList<String> songName;
	public ArrayList<String> artistName;
	public ArrayList<String> youtubelink;

	private final String UPLOAD_DIRECTORY = "/Users/jiananwen/Desktop/image";

	private final String imagep = "/Users/jiananwen/Desktop/image/";
	
	private final String jarPath = "/Users/jiananwen/Desktop/v.xiaocase-0.0.1-SNAPSHOT.jar";
	

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public HelloWorld() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		songName = new ArrayList<>();
		artistName = new ArrayList<>();
		youtubelink = new ArrayList<>();

		String imageName = imagep;

		// upload image
		if (ServletFileUpload.isMultipartContent(request)) {
			try {
				List<FileItem> multiparts = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
				for (FileItem item : multiparts) {
					if (!item.isFormField()) {
						String name = new File(item.getName()).getName();
						imageName = imageName + name;
						System.out.println(imageName);

						item.write(new File(UPLOAD_DIRECTORY + File.separator + name));
					}

				}

			} catch (Exception ex) {
				request.setAttribute("message", "File Upload Failed due to " + ex);
			}
		} else {
			request.setAttribute("message", "Sorry this Servlet only handles file upload request");
		}

		String command = "java -jar " + jarPath;
		String finalCommand = command + " " + imageName;

		try {
			runProcess(finalCommand);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//set response artist
		request.setAttribute("artists0", artistName.get(0));
		request.setAttribute("artists1", artistName.get(1));
		request.setAttribute("artists2", artistName.get(2));
		
		request.setAttribute("songs0", songName.get(0));
		request.setAttribute("songs1", songName.get(1));
		request.setAttribute("songs2", songName.get(2));
		request.setAttribute("songs3", songName.get(3));
		request.setAttribute("songs4", songName.get(4));
		
		request.setAttribute("links0", youtubelink.get(0));
		request.setAttribute("links1", youtubelink.get(1));
		request.setAttribute("links2", youtubelink.get(2));
		request.setAttribute("links3", youtubelink.get(3));
		request.setAttribute("links4", youtubelink.get(4));

		
		
		
		if (songName.size() > 0) {
			System.out.println("haha");
			request.setAttribute("show", "yes");
		}

		request.getRequestDispatcher("/index.jsp").forward(request, response);
	}

	public void runProcess(String command) throws Exception {
		Process pro = Runtime.getRuntime().exec(command);
		printLines(command + " stdout:", pro.getInputStream());
		printLines(command + " stderr:", pro.getErrorStream());
		pro.waitFor();
		// System.out.println(command + " exitValue() " + pro.exitValue());
	}

	public void printLines(String cmd, InputStream ins) throws Exception {
		String line = null;
		BufferedReader in = new BufferedReader(new InputStreamReader(ins));
		while ((line = in.readLine()) != null) {
			if (line.contains("%")) {
				String bString = line.substring(1);
				System.out.println(bString);
				String[] cStrings = bString.split(",");
				for(String dString:cStrings){
					System.out.println(dString);
				}
				artistName.add(cStrings[0]);
				songName.add(cStrings[1]);
				youtubelink.add(cStrings[2]);
			}

		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
