<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%-- errorsリストに何か入っていれば表示する --%>
<c:if test="${errors != null }">
    <div id="flush_error">
        入力内容にエラーがあります。<br />
        <%-- errorsのリストをerrorとして使えるようにしてerrorsに入っているリストを(全て)表示する --%>
        <c:forEach var="error" items="${errors}">
            ・<c:out value="${error}" /><br />
        </c:forEach>
    </div>
</c:if>
<label for="code">社員番号</label><br />
<input type="text" name="code" value="${employee.code}" />
<br /><br />

<label for="name">氏名</label><br />
<input type="text" name="name" value="${employee.name}" />
<br /><br />

<label for="password">パスワード</label><br />
<input type="password" name="password" />
<br /><br />

<div class="job_title_placement">
    <div class="job_title_placement1">
        <label for="admin_flag">権限</label>
        <select name="admin_flag">
            <option value="0"<c:if test="${employee.admin_flag == 0}">selected</c:if>>一般</option>
            <option value="1"<c:if test="${employee.admin_flag == 1}">selected</c:if>>管理者</option>
        </select>
    </div>

    <div class="job_title_placement2">
        <label for="position_flag">役職</label>
        <select name="position_flag">
            <option value="0"<c:if test="${employee.position_flag == 0}">selected</c:if>>一般</option>
            <option value="1"<c:if test="${employee.position_flag == 1}">selected</c:if>>課長</option>
            <option value="2"<c:if test="${employee.position_flag == 2}">selected</c:if>>部長</option>
        </select>
    </div>
</div>
<br /><br />

<input type="hidden" name="_token" value="${_token}" />
<button type="submit">投稿</button>