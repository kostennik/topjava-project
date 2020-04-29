<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Create/update meal</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
</head>
<body>
<div class="container">
    <form method="post" action="meals">
        <input name="action" value="create" hidden>
        <div class="form-group">
            <jsp:useBean id="meal" scope="request" type="ru.javawebinar.topjava.model.Meal"/>
            <input type="text"
                   name="id"
                   value="${meal.id}" hidden><br>
            <label style="font-size: 20px;">
                date
                <div class="form-inline">
                    <input class="form-control"
                           type="date"
                           name="date"
                           value="${meal.dateTime.toLocalDate()}"
                           required>
                    <input class="form-control"
                           type="time"
                           name="time"
                           value="${meal.dateTime.toLocalTime()}"
                           required>
                </div>
            </label><br>
            <label style="font-size: 20px;">
                description
                <input class="form-control"
                       type="text"
                       name="description"
                       value="${meal.description}"
                       required>
            </label><br>
            <label style="font-size: 20px;">
                calories
                <input class="form-control"
                       type="number"
                       name="calories"
                       value="${meal.calories == "0" ? "" : meal.calories}"
                       min="1"
                       required>
            </label><br>
            <button type="submit" class="btn btn-warning">Save</button>
            <button type="button" class="btn btn-danger" onClick="history.go(-1); return false;">Cancel</button>
        </div>
    </form>
</div>
</body>
</html>
