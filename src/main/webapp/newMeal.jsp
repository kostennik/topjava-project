<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Create/update meal</title>
</head>
<body>
<form method="post" action="meals">
    <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.Meal" scope="request"/>
    <input type="text" name="id" value="${meal.id}" hidden>
    <label>date</label> <input type="datetime-local" name="date" value="${meal.dateTime}">
    <label>description</label> <input type="text" name="description" value="${meal.description}">
    <label>calories</label> <input type="text" name="calories" value="${meal.calories}">
    <input type="submit" name="save">
</form>
</body>
</html>
