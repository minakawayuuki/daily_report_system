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
        <h2>日報　一覧</h2>
        <table id="report_list">
            <tbody>
                <tr>
                    <th class="report_name">氏名</th>
                    <th class="report_date">日付</th>
                    <th class="report_title">タイトル</th>
                    <th class="report_reaction_nice_cnt">いいね数</th>
                    <th class="report_action">操作</th>
                    <th class="report_action2">操作</th>
                    <th class="report_approval">承認確認</th>
                </tr>
                <c:forEach var="report" items="${reports}" varStatus="status">
                    <tr class="row${status.count % 2}">
                                                              <!-- Employeeクラスのnameが入る -->
                        <td class="report_name"><c:out value="${report.employee.name}" /></td>
                        <td class="report_date"><fmt:formatDate value='${report.report_date}' pattern='yyyy-MM-dd' /></td>
                        <td class="report_title">${report.title}</td>
                        <c:choose>
                            <%-- いいね数が0より大きい時はいいね数をURLにする --%>
                            <c:when test="${report.reaction_nice_cnt > 0}">
                                <td class="report_reaction_nice_cnt"><a href="<c:url value='/reactionnicetime/index?id=${report.id}&gamenn=1' />">${report.reaction_nice_cnt}</a></td>
                            </c:when>
                            <%-- いいね数が0より大きくない時はURLにしない --%>
                            <c:otherwise>
                                <td class="report_reaction_nice_cnt">${report.reaction_nice_cnt}</td>
                            </c:otherwise>
                        </c:choose>
                        <td class="report_action"><a href="<c:url value='/reports/show?id=${report.id}&gamenn=1' />">詳細を見る</a></td>
                        <c:choose>
                            <c:when test="${sessionScope.login_employee.id ==  report.employee.id}">
                                <td class="reprt_action">自分の日報</td>
                            </c:when>
                            <%-- status.indxで現在のループ回数を表示(最初は0からスタート) --%>
                            <c:when test="${followChecks[status.index] == 0}">
                               <td class="report_action2"><a href="<c:url value='/reports/tofollow?id=${report.id}&gamenn=1' />">フォローする</a></td>
                            </c:when>
                            <c:otherwise>
                               <td class="report_action2"><a href="<c:url value='/reports/unfollow?id=${report.id}&gamenn=1' />">フォロー解除</a></td>
                            </c:otherwise>
                        </c:choose>
                        <c:choose>
                                <c:when test="${report.approval > 0}">
                                    <td class="report_approval">承認済み</td>
                                </c:when>
                                <c:otherwise>
                                    <td class="report_approval">未承認</td>
                                </c:otherwise>
                            </c:choose>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <div id="pagination">
            （全 ${reports_count} 件）<br />
            <c:forEach var="i" begin="1" end="${((reports_count - 1) / 15) + 1}" step="1">
                <c:choose>
                    <c:when test="${i == page}">
                        <c:out value="${i}" />&nbsp;
                    </c:when>
                    <c:otherwise>
                        <a href="<c:url value='/reports/index?page=${i}' />"><c:out value="${i}" /></a>&nbsp;
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </div>
        <p><a href="<c:url value='/reports/new' />">新規日報の登録</a></p>

    </c:param>
</c:import>