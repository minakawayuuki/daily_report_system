<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:import url="/WEB-INF/views/layout/app.jsp">
    <c:param name="content">
        <c:choose>
            <c:when test="${report != null}">
                <h2>日報　詳細ページ</h2>

                <table>
                    <tbody>
                        <tr>
                            <th>氏名</th>
                            <td><c:out value="${report.employee.name}" /></td>
                        </tr>
                        <tr>
                            <th>日付</th>
                            <td><fmt:formatDate value="${report.report_date}" pattern="yyyy-MM-dd" /></td>
                        </tr>
                        <tr>
                            <th>内容</th>
                            <td>
                                <pre><c:out value="${report.content}" /></pre>
                            </td>
                        </tr>
                        <tr>
                            <th>登録日時</th>
                            <td>
                                <fmt:formatDate value="${report.created_at}" pattern="yyyy-MM-dd HH:mm:ss" />
                            </td>
                        </tr>
                        <tr>
                            <th>更新日時</th>
                            <td>
                                <fmt:formatDate value="${report.updated_at}" pattern="yyyy-MM-dd HH:mm:ss" />
                            </td>
                        </tr>
                        <tr>
                            <th>いいね数</th>
                            <c:choose>
                                <%-- いいね数が0より大きい時はいいね数をURLにする --%>
                                <c:when test="${report.reaction_nice_cnt > 0}">
                                    <td class="report_reaction_nice_cnt"><a href="<c:url value='/reactionnicetime/index?id=${report.id}' />">${report.reaction_nice_cnt}</a></td>
                                </c:when>
                                <%-- いいね数が0より大きくない時はURLにしない --%>
                                <c:otherwise>
                                    <td class="report_reaction_nice_cnt">${report.reaction_nice_cnt}</td>
                                </c:otherwise>
                            </c:choose>
                        </tr>
                    </tbody>
                </table>

                <c:choose>
                    <c:when test="${sessionScope.login_employee.id == report.employee.id}">
                        <p><a href="<c:url value="/reports/edit?id=${report.id}" />">この日報を編集する</a></p>
                    </c:when>
                    <c:otherwise>
                       <p><a href="<c:url value="/reports/count/likes?id=${report.id}" />">いいねする</a></p>
                    </c:otherwise>
                </c:choose>
            </c:when>
            <c:otherwise>
                <h2>お探しのデータは見つかりませんでした。</h2>
            </c:otherwise>
        </c:choose>

        <p><a href="<c:url value="/reports/index" />">一覧に戻る</a></p>
    </c:param>
</c:import>