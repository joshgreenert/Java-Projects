<%@ page import="javax.servlet.*,javax.servlet.http.*,java.io.*,java.sql.*" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>JSP TEST</title>
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
    catch(Exception e){
        e.printStackTrace();
    }
    
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

        String car = "";
        String bike = "";
        String color = "";

        // Get the data from the form button by using ternary statements to account for null values.
        car = (request.getParameter("week5cars") != null) ? request.getParameter("week5cars") : "";
        bike = (request.getParameter("week5bikes") != null) ? request.getParameter("week5bikes") : "";
        color = (request.getParameter("week5colors") != null) ? request.getParameter("week5colors") : "";

        // Start the table where the data will be filled into.

        outObject.println("<div class='tablecss'>");
        outObject.println("<h2 style='text-align:center;'>Data From Query</h2>");
        outObject.println("<table style='border: 1px solid black; margin-left: auto; margin-right: auto; background:white;'");
        outObject.println("<tr>");
        outObject.println("<th style='text-align:center; border: 1px solid #000000;'>ID</th>");
        outObject.println("<th style='text-align:center; border: 1px solid #000000;'>ITEM</th>");
        outObject.println("</tr>");
        outObject.println("<tr>");

        try{
			int index = 1;
            
            // Create a connection to use for statements.
			DriverManager.registerDriver (new oracle.jdbc.OracleDriver());
			Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost", "joshgreenert1","iamsilly1");

            // Check which variable the user entered for which form was posted.
            if(color.isEmpty() && bike.isEmpty()){
                
                // Get the data from the table and to find the final row number; for primary key.
                Statement stmt = con.createStatement();
                ResultSet carRs = stmt.executeQuery("SELECT * FROM WEEK5CARS");
                int count = 1;

                // Set count by counting all records fetched.
                while(carRs.next()){
                    count++;
                }

                // Use the count to add the new record from the user.
                ResultSet carInsertRs = stmt.executeQuery("INSERT INTO week5cars(carid, car) VALUES ('" + 
                        count + "','" + car + "')");

                // Get the data set again that now includes the users entry.
                ResultSet finalRs = stmt.executeQuery("SELECT * FROM WEEK5CARS ORDER BY CARID ASC");

                // Loop through the list to insert items into the table. Make sure to open the new
                // row and close within the query as long as a new item from table exists.
                while(finalRs.next()){

                    outObject.println("<tr>");
                    outObject.println("<td style='text-align:center; border: 1px solid #000000;'>");
                    outObject.println(finalRs.getString(index));
                    outObject.println("</td>");

                    // Increase the index to grab the next piece.
                    index++;

                    outObject.println("<td style='text-align:center; border: 1px solid #000000;'>");
                    outObject.println(finalRs.getString(index));
                    outObject.println("</td>");
                    outObject.println("</tr>");

                    // Descrease the index to support the next row.
                    index--;
                }
                stmt.close();
		    	con.close();
                
            }
            else if(color.isEmpty() && car.isEmpty()){
                // Get the data from the table and to find the final row number; for primary key.
                Statement stmt = con.createStatement();
                ResultSet bikeRs = stmt.executeQuery("SELECT * FROM WEEK5BIKES");
                int count = 1;

                // Set count by counting all records fetched.
                while(bikeRs.next()){
                    count++;
                }

                // Use the count to add the new record from the user.
                ResultSet bikeInsertRs = stmt.executeQuery("INSERT INTO WEEK5BIKES(bikeid, bike) VALUES ('" + 
                        count + "','" + bike + "')");

                // Get the data set again that now includes the users entry.
                ResultSet finalRs = stmt.executeQuery("SELECT * FROM WEEK5BIKES ORDER BY BIKEID ASC");

                // Loop through the list to insert items into the table. Make sure to open the new
                // row and close within the query as long as a new item from table exists.
                while(finalRs.next()){

                    outObject.println("<tr>");
                    outObject.println("<td style='text-align:center; border: 1px solid #000000;'>");
                    outObject.println(finalRs.getString(index));
                    outObject.println("</td>");

                    // Increase the index to grab the next piece.
                    index++;

                    outObject.println("<td style='text-align:center; border: 1px solid #000000;'>");
                    outObject.println(finalRs.getString(index));
                    outObject.println("</td>");
                    outObject.println("</tr>");

                    // Descrease the index to support the next row.
                    index--;
                }
                stmt.close();
		    	con.close();
            }
            else if(car.isEmpty() && bike.isEmpty()){
                // Get the data from the table and to find the final row number; for primary key.
                Statement stmt = con.createStatement();
                ResultSet colorRs = stmt.executeQuery("SELECT * FROM WEEK5COLORS");
                int count = 1;

                // Set count by counting all records fetched.
                while(colorRs.next()){
                    count++;
                }

                // Use the count to add the new record from the user.
                ResultSet colorInsertRs = stmt.executeQuery("INSERT INTO WEEK5COLORS(colorid, color) VALUES ('" + 
                        count + "','" + color + "')");

                // Get the data set again that now includes the users entry.
                ResultSet finalRs = stmt.executeQuery("SELECT * FROM WEEK5COLORS ORDER BY COLORID ASC");

                // Loop through the list to insert items into the table. Make sure to open the new
                // row and close within the query as long as a new item from table exists.
                while(finalRs.next()){

                    outObject.println("<tr>");
                    outObject.println("<td style='text-align:center; border: 1px solid #000000;'>");
                    outObject.println(finalRs.getString(index));
                    outObject.println("</td>");

                    // Increase the index to grab the next piece.
                    index++;

                    outObject.println("<td style='text-align:center; border: 1px solid #000000;'>");
                    outObject.println(finalRs.getString(index));
                    outObject.println("</td>");
                    outObject.println("</tr>");

                    // Descrease the index to support the next row.
                    index--;

                }
                stmt.close();
		    	con.close();
            }
            else{
            
                outObject.println("Nothing Submitted");
                
            }

		}
		catch (java.lang.Exception ex){

			ex.printStackTrace();
        }   
}
%>

<%
if(request.getMethod().equals("GET")){
    
        doGet( request, response);
    
%>

<%
}
if(request.getMethod().equals("POST")){

    doPost( request, response);

}
%>


</body>

</html>