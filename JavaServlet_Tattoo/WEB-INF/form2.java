
import javax.servlet.*;
import java.io.*;
import java.sql.*;

public class JoshGreenertFormPost2 extends HttpServlet {
    private String target = "localhost:7070/Bluestinger";

    String color = "";

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

        color = (request.getParameter("week5colors") != null) ? request.getParameter("week5colors") : "";

        out.println("<div>");
        out.println("<h2>QueryData</h2>");
        out.println("<table>");
        out.println("<tr>");
        out.println("<th>ID</th>");
        out.println("<th>ITEM</th>");
        out.println("</tr>");
        out.println("<tr>");

        try{

			int index = 1;

			DriverManager.registerDriver (new oracle.jdbc.OracleDriver());
			Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost", "Hunter0118","iamasilly1");

            if(color.isEmpty() && bike.isEmpty()){

                Statement stmt = con.createStatement();
                ResultSet carRs = stmt.executeQuery("SELECT * FROM WEEK5CARS");
                ResultSet carInsertRs = stmt.executeQuery("INSERT INTO week5cars(carid, car) VALUES ('" + car + "')");
                ResultSet finalRs = stmt.executeQuery("SELECT * FROM WEEK5CARS ORDER BY CARID ASC");

 
                while(finalRs.next()){
                    out.println("<tr><td>" + finalRs.getString(index) + "</td>");
                    index++;
                    out.println("<td>" + finalRs.getString(index) + "</td></tr>");
                    index--;
                }
                stmt.close();
		    	con.close();
                
            }
            else{
                out.println("Nothing submitted");
            }

		}
		catch (java.lang.Exception ex){

			ex.printStackTrace();
        }

        printFooter(out);
    }

    public void printHeader(PrintWriter out) {
        out.println("<html><head><title>Servlet City</title></head><body>");
    }

    public void printFooter(PrintWriter out) {

        out.println("</div>");
        out.println("</table>");
        out.println("</body>");
        out.println("</html>");
    }

    public void printForms1(PrintWriter out) {

        // First form for the day of the week.
        out.println("<form method='post' action='http://" + target + "/Database_Data'>");
        out.println("<label>Enter A New Car</label>   ");
        out.println("<input type='text' name='week5cars' size='40' maxlength='40'/>");
        out.println("<input type='submit' />");
        out.println("</form>");
        out.println("<br>");

        // Second form for the user's first thought.
        out.println("<form method='post' action='http://" + target + "/Database_Data'>");
        out.println("<label>Enter A New Bike</label>   ");
        out.println("<input type='text' name='week5bikes' size='40' maxlength='40'/>");
        out.println("<input type='submit' />");
        out.println("</form>");
        out.println("<br>");

        // Third form for the user's favorite word.
        out.println("<form method='post' action='http://" + target + "/Database_Data'>");
        out.println("<label>Enter A New Color</label>   ");
        out.println("<input type='text' name='week5colors' size='40' maxlength='40'/>");
        out.println("<input type='submit' />");
        out.println("</form>");
        out.println("<br>");
        
    }
}