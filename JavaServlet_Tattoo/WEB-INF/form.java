


import javax.servlet.*;
import java.io.*;
import java.sql.*;

@WebServlet(name = "webform", urlPatterns = {"/webform"})
public class JoshGreenertFormPostServlet extends HttpServlet {
    private final String target = "localhost:7070/Bluestinger";

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        printHeader(out);
        printForms(out);
        printFooter(out);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        printHeader(out);
        printForms(out);

        String username = request.getParameter("USERS_TABLE");

        if (username != null && !username.isEmpty()) {
            try (Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost", "Hunter0118", "Bluestinger")) {
                String insertQuery = "INSERT INTO USERS_TABLE(username) VALUES (?)";
                try (PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {
                    pstmt.setString(1, username);
                    pstmt.executeUpdate();
                }

                String selectQuery = "SELECT * FROM USERS_TABLE ORDER BY userId ASC";
                try (Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery(selectQuery)) {
                    out.println("<div><h2>Query Data</h2><table><tr><th>ID</th><th>ITEM</th></tr>");
                    while (rs.next()) {
                        out.println("<tr><td>" + rs.getString("userId") + "</td>");
                        out.println("<td>" + rs.getString("username") + "</td></tr>");
                    }
                    out.println("</table></div>");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else {
            out.println("Nothing submitted");
        }

        printFooter(out);
    }

    public void printHeader(PrintWriter out) {
        out.println("<html><head><title>Servlet</title></head><body>");
    }

    public void printFooter(PrintWriter out) {
        out.println("</body></html>");
    }

    public void printForms(PrintWriter out) {
        out.println("<form method='post' action='http://" + target + "/database_data'><label>Enter A Value:</label>");
        out.println("<input type='text' name='USERS_TABLE' size='40' maxlength='40'/><input type='submit' /></form><br>");
    }
}