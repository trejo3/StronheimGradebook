

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class InsertionServlet
 */
@WebServlet("/InsertionServlet")
public class InsertionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public InsertionServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("Entered doGet");
		String insertType = request.getParameter("insertType");
		System.out.println(insertType);
		PrintWriter writer = response.getWriter();
		if (insertType.equals("ass"))
		{
			System.out.println("Entered assignment condition");
			printBootstrapHTML(writer, insertAssignment(request.getParameter("studentID"), request.getParameter("a_name"), request.getParameter("type"), 
					request.getParameter("grade"), new Date(request.getDateHeader("date"))));
			
		}
		else if(insertType.equals("student"))
		{
			System.out.println("Entered student condition");
			printBootstrapHTML(writer, insertStudent(request.getParameter("s_name")));
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	private String insertStudent(String name)
	{
		System.out.println("Entered insertStudent");
		System.out.println("name: " + name);
		String sql = "insert into students (student_full_name) values (?)";
		String count = null;
		try(Connection conn = getConnection();
			PreparedStatement ps = conn.prepareStatement(sql))
		{
			ps.setString(1, name);
			count = Integer.toString(ps.executeUpdate());
		}
		catch(SQLException | ServletException | IOException | ClassNotFoundException e)
		{
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
		String retVal = "You have added " + count + "student(s)."
				+ "<div><a type=\"button\" class=\"btn btn-default\" href=\"http://localhost:8080/Strongheim_Gradebook/\">"
				+ "Add Another Assignment</a></div>";;
		return retVal;
	}

	private String insertAssignment(String studentID, String name, String grade, String type, Date date)
	{
		System.out.println("Entered insertAssignment");
		String sql = "insert into assignments (student_id, name, grade, type, assignment_date) values (?, ?, ?, ?, ?)";
		String count = null;
		try(Connection conn = getConnection();
			PreparedStatement ps = conn.prepareStatement(sql))
		{
			ps.setString(1, studentID);
			ps.setString(2, name);
			ps.setString(3, grade);
			ps.setString(4, type);
			ps.setDate(5, (java.sql.Date) date);
			
			count = Integer.toString(ps.executeUpdate());
		}
		catch(SQLException | ServletException | IOException | ClassNotFoundException e)
		{
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
		String retVal = "You have added " + count + "assignment(s)."
				+ "<div><a type=\"button\" class=\"btn btn-default\" href=\"http://localhost:8080/Strongheim_Gradebook/\">"
				+ "Add Another Assignment</a></div>";;
		return retVal;
	}
	
	private void printBootstrapHTML(PrintWriter writer, String body)
	{
		String titleString = "Gradebook";
		
		// Pre-body
		writer.println("<!DOCTYPE html><html><head><title>" + titleString + "</title>"
				 + "<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css\" "
				 + "integrity=\"sha512-dTfge/zgoMYpP7QbHy4gWMEGsbsdZeCXz7irItjcC3sPUFtf0kuFbDz/ixG7ArTxmDjLXDmezHubeNikyKGVyQ==\" crossorigin=\"anonymous\">"
				 + "</head>");
		
		// Body-nav
		writer.println("<nav class=\"navbar navbar-default\"><div class=\"container-fluid\"><!-- Brand and toggle get grouped for better mobile display -->"
			    + "<div class=\"navbar-header\"><button type=\"button\" class=\"navbar-toggle collapsed\" data-toggle=\"collapse\" data-target="
			    + "#bs-example-navbar-collapse-1\" aria-expanded=\"false\"><span class=\"sr-only\">Toggle navigation</span>"
			    + "<span class=\"icon-bar\"></span><span class=\"icon-bar\"></span><span class=\"icon-bar\"></span>"
			    + "</button><a class=\"navbar-brand\" href=\"http://localhost:8080/Gradebook_Web_App/\">Ms. Mackleberry's Gradebook</a>"
			    + "</div><!-- Collect the nav links, forms, and other content for toggling --><div class=\"collapse navbar-collapse\""
			    + " id=\"bs-example-navbar-collapse-1\"><ul class=\"nav navbar-nav\"><!-- <li class=\"active\"><a href=\""
			    + "http://localhost:8080/Gradebook_Web_App/GradeEntryServlet\">Grades <span class=\"sr-only\">(current)</span></a></li>"
			    + "--></ul><ul class=\"nav navbar-nav navbar-right\"><li class=\"active\"><a href=\"http://localhost:8080/Gradebook_Web_App/GradeEntryServlet\">"
			    + "Grades <span class=\"sr-only\">(current)</span></a></li></ul></div><!-- /.navbar-collapse --></div><!-- /.container-fluid -->"
			    + "</nav><br><br>");
		
		// Body-content
		writer.println(generatePanel(body));
		
		// Body-jquery/Bootstrap js
		writer.println("<!-- jQuery (necessary for Bootstrap's JavaScript plugins) --> <script src=\"https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js\">"
		+ "</script><!-- Include all compiled plugins (below), or include individual files as needed --><script src=\"js/bootstrap.min.js\"></script></body>");
		
		// Post-body
		writer.println("</html>");
		writer.close();
	}
	
	private String generatePanel(String content)
	{
		String retVal = "<div class=\"container\"><div class=\"panel panel-default\"><div class=\"panel-body\">";
		retVal += content;
		retVal += "</div></div></div>";
		return retVal;
	}
	
	private Connection getConnection() throws ClassNotFoundException, ServletException, IOException
	{
		Connection connection = null;
		try
		{
			String url = "jdbc:oracle:thin:system/password@localhost";
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Properties prop = new Properties();
			prop.setProperty("user", "testuserdb");
			prop.setProperty("password", "password");
			connection = DriverManager.getConnection(url, prop);
			return connection;
		}
		catch(ClassNotFoundException e)
		{
			System.err.println(e.getMessage());
			e.printStackTrace();
			return null;
		}
		catch(SQLException e)
		{
			System.err.println(e.getMessage());
			e.printStackTrace();
			return null;
		}
	}
}
