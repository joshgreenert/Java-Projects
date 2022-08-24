


import javax.servlet.*;
import java.io.*;
import java.sql.*;

public class JoshGreenertFormPost2 extends HttpServlet {
    private String target = "localhost:7070/Bluestinger";
    String userValue = "";
    public void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {

        PrintWriter out = response.getWriter();

        printHeader(out);
        printForms(out);
        printFooter(out);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        printHeader(out);
        printForms(out);
        userValue = (request.getParameter("week5colors") != null) ? request.getParameter("week5colors") : "";

        out.println("<div><h2>QueryData</h2><table><tr><th>ID</th><th>ITEM</th></tr><tr>");
        try{
			DriverManager.registerDriver (new oracle.jdbc.OracleDriver());
			Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost", "Hunter0118","P@sSwOr0");

            if(color.isEmpty() && bike.isEmpty()){
                Statement stmt = conn.createStatement();
                ResultSet carRs = stmt.executeQuery("SELECT * FROM WEEK5CARS");
                ResultSet carInsertRs = stmt.executeQuery("INSERT INTO week5cars(carid, car) VALUES ('" + car + "')");
                ResultSet finalRs = stmt.executeQuery("SELECT * FROM WEEK5CARS ORDER BY CARID ASC");
                while(finalRs.next()){
                    out.println("<tr><td>" + finalRs.getString(1) + "</td>");
                    out.println("<td>" + finalRs.getString(2) + "</td></tr>");
                }
                stmt.close();
		    	conn.close();
            } else{ out.println("Nothing submitted"); }
		}
		catch (java.lang.Exception ex){ ex.printStackTrace(); }
        printFooter(out);
    }

    public void printHeader(PrintWriter out) { out.println("<html><head><title>Servlet City</title></head><body>"); }

    public void printFooter(PrintWriter out) { out.println("</div></table></body></html>"); }
    public void printForms(PrintWriter out) {
        out.println("<form method='post' action='http://" + target + "/database_data'><label>Enter A Value:</label>");
        out.println("<input type='text' name='week5cars' size='40' maxlength='40'/><input type='submit' /></form><br>");
    }
}