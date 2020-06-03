<#include "header.ftl">

<div class="row">
    <div class="col-md-12 mt-1">
        <h3>Hi colleague!</h3>
        <form class="form-inline" action="/register" method="post">
            <div class="form-group">
                <label for="name">Name:</label>
                <input type="text" class="form-control" id="name" name="name">
            </div><br><br>
            <button type="submit">Submit</button>
        </form>
    </div>
</div>

<#include "footer.ftl">