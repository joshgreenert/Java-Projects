<%@ page import="javax.servlet.*,javax.servlet.http.*,java.io.*,java.sql.*" %>
<!DOCTYPE html><html><head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Java Servlet</title>
</head>
<body>
<%!
public void printForms(PrintWriter outObject) {
    try{
        outObject.println("<form method=\'post' action=\'http://localhost:7070/Bluestinger/form\'>");
        outObject.println("<label>Enter Data:</label>   ");
        outObject.println("<input type='text' name='fieldName' size='40' maxlength='40'/>");
        outObject.println("<input type='submit' />");
        outObject.println("</form>");
    }
    catch(Exception e){ e.printStackTrace(); }
    
}
public void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        PrintWriter outObject = response.getWriter();
        printForms(outObject);
}
public void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        PrintWriter outObject = response.getWriter();
        printForms( outObject);
        String userValue = "";
        userValue = (request.getParameter("week5colors") != null) ? request.getParameter("week5colors") : "";
        outObject.println("<div><h2>Data From Query</h2><table><tr><th>ID</th><th>ITEM</th></tr><tr>");
        try{
			DriverManager.registerDriver (new oracle.jdbc.OracleDriver());
			Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost", "Hunter0118","iamsilly1");
            if(color.isEmpty() && bike.isEmpty()){
                Statement stmt = conn.createStatement();
                ResultSet carRs = stmt.executeQuery("SELECT * FROM WEEK5CARS");
                ResultSet carInsertRs = stmt.executeQuery("INSERT INTO week5cars(carid, car) VALUES ('" + car + "')");
                ResultSet finalRs = stmt.executeQuery("SELECT * FROM WEEK5CARS ORDER BY CARID ASC");
                while(finalRs.next()){
                    outObject.println("<tr><td>" + finalRs.getString(1) + "</td>");
                    outObject.println("<td>" + finalRs.getString(2) + "</td></tr>");
                }
                stmt.close();
		    	conn.close(); 
            }
            else{ outObject.println("Nothing Submitted"); }
		}
		catch (java.lang.Exception ex){ ex.printStackTrace(); }   
}
%>
<% if(request.getMethod().equals("GET")){ doGet( request, response); %>
<% }
if(request.getMethod().equals("POST")){ doPost( request, response); }%>
</body></html>