<%@ page import="javax.servlet.*,javax.servlet.http.*,java.io.*,java.sql.*" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Java Servlet</title>
</head>
<body>
<%
    public void printForms(PrintWriter out) {
        out.println("<form method='post' action='/Bluestinger/form'>");
        out.println("<label>Enter Data:</label>");
        out.println("<input type='text' name='fieldName' size='40' maxlength='40'/>");
        out.println("<input type='submit' />");
        out.println("</form>");
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        printForms(out);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        printForms(out);
        String userValue = request.getParameter("fieldName");
        out.println("<div><h2>Data From Query</h2><table><tr><th>ID</th><th>ITEM</th></tr>");
        try {
            DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
            Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost", "Hunter0118", "Bluestinger");
            if (userValue != null && !userValue.isEmpty()) {
                PreparedStatement pstmt = conn.prepareStatement("INSERT INTO USERS_TABLE(username) VALUES (?)");
                pstmt.setString(1, userValue);
                pstmt.executeUpdate();
            }
            Statement stmt = conn.createStatement();
            ResultSet finalRs = stmt.executeQuery("SELECT * FROM USERS_TABLE ORDER BY userId ASC");
            while (finalRs.next()) {
                out.println("<tr><td>" + finalRs.getString(1) + "</td>");
                out.println("<td>" + finalRs.getString(2) + "</td></tr>");
            }
            stmt.close();
            conn.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        out.println("</table></div>");
    }

    // Call doGet or doPost based on request method
    if (request.getMethod().equals("GET")) {
        doGet(request, response);
    }
    if (request.getMethod().equals("POST")) {
        doPost(request, response);
    }
%>
</body>
</html>
