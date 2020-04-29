<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
<head>
    <title>User Meals</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
</head>
<body>

<div class="container">
    <h3><a href="index.html">Home</a></h3>
    <hr>
    <div class="d-flex justify-content-end">
    <form method="post" action="meals">
        <input name="action" value="save" hidden>
        <button type="submit" class="btn btn-success"><i class="fa fa-plus">  add meal</i></button>
    </form>
    </div>
    <h2 style="text-align: center">User meals</h2>
    <br>
    <table class="table table-striped">
        <thead>
        <tr>
            <th>Date</th>
            <th>Description</th>
            <th>Calories</th>
            <th>Edit</th>
            <th>Delete</th>
        </tr>
        </thead>
        <tbody>
        <jsp:useBean id="mealsTo" scope="request" type="java.util.List"/>
        <c:forEach items="${mealsTo}" var="meal">
            <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.MealTo"/>
            <tr class="${meal.excess ? "text-danger" : "text-success"}">
                <td>${fn:replace(meal.dateTime, "T", " ")}</td>
                <td>${meal.description}</td>
                <td>${meal.calories}</td>
                <td>
                    <form method="post" action="meals">
                        <input name="id" value="${meal.id}" hidden>
                        <input name="action" value="edit" hidden>
                        <button type="submit" class="btn btn-link"><i class="fa fa-pencil text-info"></i></button>
                    </form>
                </td>
                <td>
                    <form method="post" action="meals">
                        <input name="id" value="${meal.id}" hidden>
                        <input name="action" value="delete" hidden>
                        <button type="submit" class="btn btn-link"><i class="fa fa-close text-danger"></i></button>
                    </form>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
</body>
</html>
