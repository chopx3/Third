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
</head>
<body>


<div id="main-form">
    <form action="Uploader" method="post" enctype="multipart/form-data">
        <input type="button" id="leftButton" value="Upload File" onclick="document.getElementById('file').click();" />
        <input type="file" accept=".xls,.xlsx" style="display:none;" id="file" name="file" onchange="alertFilename()"/>
        <input type="text" id="dest" value="Destination of your file(xls or xlsx)" disabled />
        <input type="submit" id="rightButton" value="Check File" />
        <p> Results </p>

        <hr>
        <br>
        <div id="inner-form">
        <table>
            <c:forEach items="${outputRows}" var="outputRows">
                <tr>
                    <td width=10%>${outputRows.row}</td>
                    <td width=80%>${outputRows.text}</td>
                </tr>
            </c:forEach>
        </table>
        </div>
    </form>
</div>
</body>
</html>
