

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class QueryServlet
 */
@WebServlet("/QueryServlet")
public class QueryServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public QueryServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		PrintWriter writer = response.getWriter();
		
		String searchType = request.getParameter("searchType");
		
		switch (searchType){
		
		case "1":
			//Query all assignments by student
		case "2":
			//Query all assignments by type
		case "3":
			//Query all assignments by type and student
		case "4":
			//Query average for student
		case "5":
			//Query average for student by type
		case "6":
			//Query highest and lowest grade by type
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request,response);
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
	
	private String assignmentByS(String name)
	{
		String retVal = "<div style=\"width:40%;margin:auto;\"><table class=\"table table-striped table-hover table-bordered\"><tr><th>Student Name</th><th>Assignment Name</th><th>Type</th>"
				+ "<th>Grade</th><th>Date</th></tr>";
		String sql = "SELECT * FROM STUDENTS, ASSIGNMENTS WHERE STUDENTS.STUDENT_ID = ASSIGNMENTS.STUDENT_ID AND STUDENTS.STUDENT_FULL_NAME = ?";
		try(Connection connection = getConnection();
			PreparedStatement ps = connection.prepareStatement(sql))
		{
			ps.setString(1, name);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next())
			{
				String sName = rs.getString("STUDENTS.STUDENT_FULL_NAME");
				String aName = rs.getString("ASSIGNMENTS.NAME");
				String aType = rs.getString("TYPE");
				String aGrade = rs.getString("GRADE");
				Date aDate = rs.getDate("DATE");

				retVal += ("<tr><td>"+sName+"</td><td>"+aName+"</td><td>"+aType+"</td><td>"+aGrade+"</td><td>"+aDate+"</td></tr>");
			}
			retVal += "</table>";
			return retVal;
		}
		catch(SQLException | ServletException | IOException | ClassNotFoundException e)
		{
			System.err.println(e.getMessage());
			e.printStackTrace();
			return retVal;
		}
	}
	
	private String allAssignmentsByT(String type)
	{
		String retVal = "<div style=\"width:40%;margin:auto;\"><table class=\"table table-striped table-hover table-bordered\"><tr><th>Name</th><th>Type</th>"
				+ "<th>Grade</th><th>Date</th></tr>";
		String sql = "SELECT * FROM STUDENTS, ASSIGNMENTS WHERE STUDENTS.STUDENT_ID = ASSIGNMENTS.STUDENT_ID AND ASSIGNMENTS.TYPE = ?";
		try(Connection connection = getConnection();
			PreparedStatement ps = connection.prepareStatement(sql))
		{
			ps.setString(1, type);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next())
			{
				String sName = rs.getString("STUDENTS.STUDENT_FULL_NAME");
				String aName = rs.getString("ASSIGNMENTS.NAME");
				String aType = rs.getString("TYPE");
				String aGrade = rs.getString("GRADE");
				Date aDate = rs.getDate("DATE");

				retVal += ("<tr><td>"+sName+"</td><td>"+aName+"</td><td>"+aType+"</td><td>"+aGrade+"</td><td>"+aDate+"</td></tr>");
			}
			retVal += "</table>";
			return retVal;
		}
		catch(SQLException | ServletException | IOException | ClassNotFoundException e)
		{
			System.err.println(e.getMessage());
			e.printStackTrace();
			return retVal;
		}
	}
	private String assignmentsByTnS(String type, String name)
	{
		String retVal = "<div style=\"width:40%;margin:auto;\"><table class=\"table table-striped table-hover table-bordered\"><tr><th>Name</th><th>Type</th>"
				+ "<th>Grade</th><th>Date</th></tr>";
		String sql = "SELECT * FROM STUDENTS, ASSIGNMENTS WHERE STUDENTS.STUDENT_ID = ASSIGNMENTS.STUDENT_ID AND ASSIGNMENTS.TYPE = ? AND STUDENTS.STUDENT_FULL_NAME = ?";
		try(Connection connection = getConnection();
			PreparedStatement ps = connection.prepareStatement(sql))
		{
			ps.setString(1, type);
			ps.setString(2, name);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next())
			{
				String sName = rs.getString("STUDENTS.STUDENT_FULL_NAME");
				String aName = rs.getString("ASSIGNMENTS.NAME");
				String aType = rs.getString("TYPE");
				String aGrade = rs.getString("GRADE");
				Date aDate = rs.getDate("DATE");

				retVal += ("<tr><td>"+sName+"</td><td>"+aName+"</td><td>"+aType+"</td><td>"+aGrade+"</td><td>"+aDate+"</td></tr>");
			}
			retVal += "</table>";
			return retVal;
		}
		catch(SQLException | ServletException | IOException | ClassNotFoundException e)
		{
			System.err.println(e.getMessage());
			e.printStackTrace();
			return retVal;
		}
	}
	private String avgforStudent(String name)
	{
		String retVal = null;
		String avg = null;
		String sql = "SELECT AVG(ASSIGNMENT.GRADE) AS AVRG FROM STUDENTS, ASSIGNMENTS WHERE STUDENTS.STUDENT_ID = ASSIGNMENTS.STUDENT_ID  AND STUDENTS.STUDENT_FULL_NAME = ?";
		try(Connection connection = getConnection();
			PreparedStatement ps = connection.prepareStatement(sql))
		{
			ps.setString(1, name);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next())
			{
				avg = rs.getString("AVRG");
			}
			retVal = "<h3 style=\"text-align:center;\">Average<br> <small>" + avg + "</small></h3><br>"
					+ "<div><a type=\"button\" class=\"btn btn-default\" href=\"http://localhost:8080/Gradebook_Web_App/\">"
					+ "Add Another Assignment</a></div>";
			return retVal;
		}
		catch(SQLException | ServletException | IOException | ClassNotFoundException e)
		{
			System.err.println(e.getMessage());
			e.printStackTrace();
			return retVal;
		}
	}
	private String avgforStudentByType(String name, String type)
	{
		String retVal = null;
		String avg = null;
		String sql = "SELECT AVG(ASSIGNMENT.GRADE) AS AVRG FROM STUDENTS, ASSIGNMENTS WHERE STUDENTS.STUDENT_ID = ASSIGNMENTS.STUDENT_ID  AND STUDENTS.STUDENT_FULL_NAME = ? AND ASSIGNMENTS.TYPE = ?";
		try(Connection connection = getConnection();
			PreparedStatement ps = connection.prepareStatement(sql))
		{
			ps.setString(1, name);
			ps.setString(2, type);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next())
			{
				avg = rs.getString("AVRG");
			}
			retVal = "<h3 style=\"text-align:center;\">Average<br> <small>" + avg + "</small></h3><br>"
					+ "<div><a type=\"button\" class=\"btn btn-default\" href=\"http://localhost:8080/Gradebook_Web_App/\">"
					+ "Add Another Assignment</a></div>";
			return retVal;
		}
		catch(SQLException | ServletException | IOException | ClassNotFoundException e)
		{
			System.err.println(e.getMessage());
			e.printStackTrace();
			return retVal;
		}
	}
	private String highestLowestByT(String type)
	{
		String retVal = null;
		String max = null, min = null;
		String sql = "SELECT MAX(ASSIGNMENT.GRADE) AS MAXI, MIN(ASSIGNMENT.GRADE) AS MINI FROM STUDENTS, ASSIGNMENTS WHERE STUDENTS.STUDENT_ID = ASSIGNMENTS.STUDENT_ID AND ASSIGNMENTS.TYPE = ?";
		try(Connection connection = getConnection();
			PreparedStatement ps = connection.prepareStatement(sql))
		{
			ps.setString(1, type);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next())
			{
				max = rs.getString("MAXI");
				min = rs.getString("MINI");
			}
			retVal = "<h3 style=\"text-align:center;\">Highest Grade<br> <small>" + max + "</small></h3><br>"
					+ "<h3 style=\"text-align:center;\">Lowest Grade<br> <small>" + min + "</small></h3><br>"
					+ "<div><a type=\"button\" class=\"btn btn-default\" href=\"http://localhost:8080/Gradebook_Web_App/\">"
					+ "Add Another Assignment</a></div>";
			return retVal;
		}
		catch(SQLException | ServletException | IOException | ClassNotFoundException e)
		{
			System.err.println(e.getMessage());
			e.printStackTrace();
			return retVal;
		}
	}
	
	

}
