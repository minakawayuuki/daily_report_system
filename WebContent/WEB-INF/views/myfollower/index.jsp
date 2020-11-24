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
        <h2>フォローしてくれた人一覧</h2>
        <table id="follower_list">
            <tbody>
                <tr>
                    <th class="follower_name">氏名</th>
                    <th class="follower_date">日付</th>
                    <th class="follower_action">操作</th>
                </tr>
                <c:forEach var="follower" items="${followers}" varStatus="status">
                    <tr class="row${status.count % 2}">
                        <td class="follower_name"><c:out value="${follower.my_id.name}" /></td>
                        <td class="follower_date"><fmt:formatDate value='${follower.created_at}' pattern='yyyy-MM-dd' /></td>
                        <c:choose>
                            <c:when test="${followChecks[status.index] == 0}">
                                <td class="follow_action"><a href="<c:url value='/tomyfollower?id=${follower.id}' />">フォローする</a></td>
                            </c:when>
                            <c:otherwise>
                                <td class="follow_action">フォローしています</td>
                            </c:otherwise>
                        </c:choose>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <div id="pagination">
            (全 ${followers_count} 件)<br />
            <c:forEach var="i" begin="1" end="${((followers_count - 1) / 15 + 1)}" step="1">
                <c:choose>
                    <c:when test="${i == page}">
                        <c:out value="${i}" />&nbsp;
                    </c:when>
                    <c:otherwise>
                        <a href="<c:url value='/myfollower/index?id=${login_employee.getId()}&page=${i}' />"><c:out value="${i}" /></a>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </div>
        <p><a href="<c:url value='/myfollower/index' />">一覧に戻る</a></p>
    </c:param>
</c:import>