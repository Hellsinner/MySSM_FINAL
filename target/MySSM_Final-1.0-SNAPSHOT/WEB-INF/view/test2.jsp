<%--
  Created by IntelliJ IDEA.
  User: yanghan
  Date: 2018/7/17
  Time: 14:13
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script type="text/javascript" src="jquery-1.10.1.min.js"></script>
<html>
<head>
    <title>Test2</title>
</head>
<body >
    编号 : ${user.emp_no}<br>
    密码 : ${user.emp_pass}<br>
    类型 : ${user.type}
</body>
<%--<script type="text/javascript">--%>
    <%--function loadInfo() {--%>
        <%--$.get({--%>
            <%--url:"/select",--%>
            <%--dataType:"json",--%>
            <%--success:function (data) {--%>
                <%--$("#name").val(data.name);--%>
                <%--$("#age").val(data.age);--%>
                <%--$("#sex").val(data.sex);--%>
            <%--}--%>
        <%--});--%>
    <%--}--%>
<%--</script>--%>
</html>
