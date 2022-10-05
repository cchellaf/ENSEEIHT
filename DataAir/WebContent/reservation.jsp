<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import= "java.util.Collection" import="entities.Vol" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>DataAit - Reservations</title>
</head>
<body>

<% Collection<Vol> listeVols = (Collection<Vol>) request.getAttribute("listeVols_selection");
for (Vol v : listeVols) {
	
	out.println(v.getId()  + " " + v.getNum_vol() + "<br>"); 
	out.println( "&nbsp;&nbsp;" + v.getDate() + " " + v.getHeure() + "<br>");
}
%>

</body>
</html>