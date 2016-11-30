<%@ page     pageEncoding="windows-1251"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
        "http://www.w3.org/TR/html4/loose.dtd">
        <style>
           <%@include file='./css/form.css' %>
        </style>
<html>
<head>
<title>File Uploading Forms</title>
</head>
<body>

<script src="./js/form.js">
</script>

<div id="main-form">
    <form action="Uploader" method="post" enctype="multipart/form-data">
        <input type="button" id="leftButton" value="Upload File" onclick="document.getElementById('file').click();" />
        <input type="file" style="display:none;" id="file" name="file" onchange="alertFilename()"/>
        <input type="text" id="dest" value="Destination of your file" disabled />
        <input type="submit" id="rightButton" value="Check File" />
        <div id="inner-form">
        <textarea rows="10" id="inner-text" readonly>
        ${requestScope.message}

        </textarea>
        </div>
    </form>
</div>
</body>
</html>
