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
        <div class="form-group">
            <input type="text" name="id" value="${meal.id}" hidden><br>
            <label style="font-size: 20px;">date</label> <input class="form-control" type="datetime-local" name="date" value="${meal.dateTime}"
                                       required><br>
            <label style="font-size: 20px;">description</label> <input class="form-control" type="text" name="description"
                                              value="${meal.description}" required><br>
            <label style="font-size: 20px;">calories</label> <input class="form-control" type="text" name="calories" value="${meal.calories}"
                                           required><br>
            <button type="submit" class="btn btn-warning">Save</button>
            <button type="button" class="btn btn-danger"  onClick="history.go(-1); return false;">Cancel</button>
        </div>
    </form>
</div>
</body>
</html>
