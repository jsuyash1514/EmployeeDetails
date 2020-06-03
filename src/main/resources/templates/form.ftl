<#include "header.ftl">

<div class="row">
    <div class="col-md-12 mt-1">
        <h2>Hi colleague!</h2>
        <form class="form-inline" action="/register" method="post">
            <div class="form-group">
                <label for="employeeid">Employee ID:</label>
                <input type="number" class="form-control" id="employeeid" name="employeeid" required>
            </div><br>
            <div class="form-group">
                <label for="name">Name:</label>
                <input type="text" class="form-control" id="name" name="name" required>
            </div><br>
            <div class="form-group">
                <label for="age">Age:</label>
                <input type="number" class="form-control" id="age" name="age" required>
            </div><br>
            <div class="form-group">
                <label for="salary">Salary:</label>
                <input type="number" class="form-control" id="salary" name="salary" required>
            </div><br>
            <div class="form-group">
                <label for="address">Address:</label>
                <input type="text" class="form-control" id="address" name="address" required>
            </div><br><br>
            <button type="submit">Submit</button>
        </form>
    </div>
</div>

<#include "footer.ftl">