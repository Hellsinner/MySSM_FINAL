<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Test1</title>
</head>
<body>

    <form action="${pageContext.request.contextPath}/add" method="post">
        编号:<input type="text" name="emp_no" />
        密码:<input type="text" name="emp_pass"/>
        类型:<input type="text" name="type"/>
        <input type="submit" value="提交"/>
    </form>
</body>
</html>
