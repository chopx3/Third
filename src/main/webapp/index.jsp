<%@ page contentType="text/html;charset=windows-1251" %>
<%@ page pageEncoding="CP1251" %>
<%@ page  import="webapp.Rows"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
        "http://www.w3.org/TR/html4/loose.dtd">

        <style>
           <%@include file='./css/form.css' %>
        </style>
<html>
<head>
<title>File Uploading Forms</title>
<script src="./js/form.js"></script>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css">
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
</head>
<body>


<div id="main-form" class="container-fluid">
    <form action="Uploader" method="post" enctype="multipart/form-data">
        <div class="input-group" >
        <span class="input-group-btn">
        <input type="button" id="leftButton" class="btn btn-info btn-md" value="Upload File" onclick="document.getElementById('file').click();" />
        </span>

        <input type="text" class="form-control" id ="dest" value="Destination of your file(xls or xlsx)" disabled />
        <span class="input-group-btn">
        <input type="submit" id="rightButton" class="btn btn-success btn-md" value="Check File" />
        </span>
        <input type="file" accept=".xls,.xlsx" style="display:none;" id="file" name="file" onchange="alertFilename()"/>
        </div>
    </form>
</div>
        <hr>
        <div class="container">

        <table class="table table-striped">
            <thead>
            <tr>
                <th width=10%>Row</th>
                <th width=80%>Error Text</th>
            </tr>
            </thead>
            <c:forEach items="${outputRows}" var="outputRows">
                <tr>
                    <td width=10%>${outputRows.row}</td>
                    <td width=80%>${outputRows.text}</td>
                </tr>
            </c:forEach>
        </table>

        </div>

</body>
</html>
