<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Database Tables</title>
</head>
<body>
    <h1>Database Tables</h1>
    <div>
        <%@ page import="java.util.List" %>
<%
    List<String> tableNames = (List<String>) request.getAttribute("tableNames");
%>
<ul>
    <%
        if (tableNames != null) {
            for (String tableName : tableNames) {
    %>
                <li><%= tableName %></li>
    <%
            }
        } else {
    %>
            <li>No tables found</li>
    <%
        }
    %>
</ul>
    </div>
</body>
</html>