<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:import url="/WEB-INF/views/layout/app.jsp">
    <c:param name="content">
        <c:if test="${flush != null}">
            <div id="flush_success">
                <c:out value="${flush}"></c:out>
            </div>
        </c:if>
        <c:choose>
            <c:when test="${reactionnicetime_count > 0}">
                <h2>いいねした人一覧</h2>
                <table id="reactionnicetime_list">
                    <tbody>
                        <tr>
                            <th class="reactionnicetime_name">氏名</th>
                            <th class="reactionnicetime_created_at">日付</th>
                        </tr>
                        <c:forEach var="reactionnicetime" items="${reactionnicetimes}" varStatus="status">
                            <tr class="row${status.count % 2}">
                                <td class="reactionnicetime_name"><c:out value="${reactionnicetime.employee.name}" /></td>
                                <td class="reactionnicetime_created_at"><fmt:formatDate value='${reactionnicetime.created_at}' pattern='yyyy-MM-dd' /></td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>

                <div id="pagination">
                    （全 ${reactionnicetime_count} 件）<br />
                    <c:forEach var="i" begin="1" end="${((reactionnicetime_count - 1) / 15) + 1}" step="1">
                        <c:choose>
                            <c:when test="${i == page}">
                                <c:out value="${i}" />&nbsp;
                            </c:when>
                            <c:otherwise>
                                <a href="<c:url value='/reactionnicetime/index?id=${id}&page=${i}&gamenn=${gamenn}' />"><c:out value="${i}" /></a>&nbsp;
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
               </div>
            </c:when>
            <c:otherwise>
               <h2>まだいいねした人はいません</h2>
            </c:otherwise>
        </c:choose>
        <c:choose>
            <c:when test="${gamenn == 2}">
                <p><a href="<c:url value="/unapproved/index" />">未承認日報一覧に戻る</a></p>
            </c:when>
            <c:otherwise>
                <p><a href="<c:url value="/reports/index" />">日報一覧に戻る</a></p>
            </c:otherwise>
        </c:choose>
    </c:param>
</c:import>