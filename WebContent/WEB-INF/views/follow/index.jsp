<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:import url="/WEB-INF/views/layout/app.jsp">
    <c:param name="content">
        <c:if test="${flush != null}">
            <div id="flush_success">
                <c:choose>
                    <c:when test="${flush_name != null}">
                        <c:out value="${flush_name}"></c:out><span class="mgr-10">さんを</span><c:out value="${flush}"></c:out>
                    </c:when>
                    <c:otherwise>
                        <c:out value="${flush}"></c:out>
                    </c:otherwise>
                </c:choose>
            </div>
        </c:if>
        <h2>フォローした人一覧</h2>
        <table id="follow_list">
            <tbody>
                <tr>
                    <th class="follow_name">氏名</th>
                    <th class="follow_date">日付</th>
                    <th class="follow_action">操作</th>
                </tr>
                <c:forEach var="follow" items="${follows}" varStatus="status">
                    <tr class="row${status.count % 2}">
                        <td class="follow_name"><c:out value="${follow.employee_id.name}" /></td>
                        <td class="follow_date"><fmt:formatDate value='${follow.created_at}' pattern='yyyy-MM-dd' /></td>
                        <td class="follow_action"><a href="<c:url value='/unfollow?id=${follow.id}' />">フォロー削除</a></td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <div id="pagination">
            (全 ${follows_count} 件)<br />
            <c:forEach var="i" begin="1" end="${((follows_count - 1) / 15 + 1)}" step="1">
                <c:choose>
                    <c:when test="${i == page}">
                        <c:out value="${i}" />&nbsp;
                    </c:when>
                    <c:otherwise>
                        <a href="<c:url value='/follow/index?id=${login_employee.getId()}&page=${i}' />"><c:out value="${i}" /></a>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </div>
        <p><a href="<c:url value='/follow/index' />">一覧に戻る</a></p>

    </c:param>
</c:import>