
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;

import javax.imageio.ImageIO;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;

import gui.ava.html.image.generator.HtmlImageGenerator;

/**
 * Servlet implementation class ConvertServlet
 */
@WebServlet("/htmltopdf")
public class ConvertServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 */
	public ConvertServlet() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		String path = "/index.html";
		ServletContext servletContext = getServletContext();
		RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher(path);
		requestDispatcher.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String siteurl = request.getParameter("siteurl");
		//URL url = new URL(siteurl);
		//URLConnection conn = url.openConnection();
		//InputStream inHtml = conn.getInputStream();
		OutputStream out = response.getOutputStream();	

		try {		
			
			HtmlImageGenerator imageGenerator = new HtmlImageGenerator(); 
			imageGenerator.loadUrl(siteurl);
			imageGenerator.setSize(new Dimension(2000, 3000));
			//imageGenerator.saveAsImage("hello-world.png"); 
			//imageGenerator.saveAsHtmlWithMap("hello-world.html", "hello-world.png");
			BufferedImage image = imageGenerator.getBufferedImage();
			

			/*Pdf converter*/
			/*ByteArrayOutputStream outPdf = new ByteArrayOutputStream();
			Document document = new Document(PageSize.A4, 15, 15, 45, 30);
			PdfWriter writer = PdfWriter.getInstance(document, outPdf);
			document.open();
			
			Font mainFont = FontFactory.getFont("Aria", 10, BaseColor.BLACK);
			XMLWorkerHelper.getInstance().parseXHtml(writer, document, inHtml);
			document.close();
			
			outPdf.close();*/
			
			response.setContentType("image/png");
			response.setHeader("Content-disposition", "attachment; filename=result.png");
			ImageIO.write(image, "PNG", out);
			image.flush();
			
		} catch (Exception ex) {
			response.setContentType("text/html");
			PrintWriter writer = new PrintWriter(out);
			writer.println("<h2>Failed to convert xhtml to pdf. Probably some error in syntaxis: </h2>");
			writer.println("<p>" + ex.getMessage() + "</p>");
			System.out.println(ex.getMessage());
			writer.close();
		} finally {
			//inHtml.close();
			out.flush();
		}
	}
}
