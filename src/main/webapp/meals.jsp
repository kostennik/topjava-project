<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="mealsTo" value="${mealsTo}"/>
<html>
<head>
    <title>User Meals</title>
</head>
<body>
<table>
    <thead>
    <tr>Date</tr>
    <tr>Description</tr>
    <tr>Calories</tr>
    </thead>
    <tbody>
    <c:forEach items="${mealsTo}" var="meal">
        <jsp:useBean id="meal" class="ru.javawebinar.topjava.model.Meal"/>
        <p>${meal.dateTime}</p>
        <tr>${meal.dateTime}</tr>
        <tr>${meal.description}</tr>
        <tr>${meal.calories}</tr>
    </c:forEach>
    </tbody>
</table>
</body>
</html>
