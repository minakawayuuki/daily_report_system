<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%-- c:importを使うことでurlに指定したファイルの内容をその位置で読み込める --%>
<c:import url="../layout/app.jsp">
    <%-- param name="content" と書くことでapp.jspの${param.content}のところに当てはまる --%>
    <c:param name="content">
        <%-- flushがnullでなければflushに入った内容を表示する --%>
        <c:if test="${flush != null}">
            <div id="flush_success">
                <c:out value="${flush}"></c:out>
            </div>
        </c:if>
        <h2>従業員 一覧</h2>
        <table id="employee_list">
            <tbody>
                <tr>
                    <th>社員番号</th>
                    <th>氏名</th>
                    <th>操作</th>
                </tr>
                <%-- employees(Employee.java)をemployeeとして扱えるようにする。
                varStatusで繰り返しの状態をstatusと言う変数を作り格納する --%>
                <c:forEach var="employee" items="${employees}" varStatus="status">
                    <%-- countで何回目の繰り返しかを計り２で割りあまりを求める（色を分ける際に活用する） --%>
                    <tr class="row${status.count % 2}">
                        <%-- employeeのcode(社員番号)を表示する --%>
                        <td><c:out value="${employee.code}" /></td>
                        <%-- employeeのname(社員名)を表示 --%>
                        <td><c:out value="${employee.name}" /></td>
                        <td>
                            <c:choose>
                                <%-- もしdelete_flag(削除されたことにする機能)が1(削除)だったら表示 --%>
                                <c:when test="${employee.delete_flag == 1}">
                                （削除済み）
                                </c:when>
                                <c:otherwise>
                                    <%-- 削除されていなかったら社員の詳細を表示する --%>
                                    <a href="<c:url value='/employees/show?id=${employee.id}' />">詳細を表示</a>
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </c:forEach>

            </tbody>
        </table>

        <div id="pagination">
            <%-- 全部で何件登録されているかを表示 --%>
            （全 ${employees_count} 件）<br />
                <%-- ページネーションを作るための繰り返し処理 --%>
                <%-- 初期値１の変数iを作成しループ一回記し終わったら変数iが１ずつ増えるループの終わりをendに記述する --%>
                <c:forEach var="i" begin="1" end="${((employees_count - 1) / 15) + 1}" step="1">
                    <c:choose>
                        <%-- 現在いるページを表す --%>
                        <c:when test="${i == page}">
                            <c:out value="${i}" />&nbsp;
                        </c:when>
                        <%-- 他のページにうつりたい時のurlを指定 --%>
                        <c:otherwise>
                            <a href="<c:url value='/employees/index?page=${i}' />"><c:out value="${i}" /></a>&nbsp;
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
        </div>
        <p><a href="<c:url value='/employees/new' />">新規従業員の登録</a></p>

    </c:param>
</c:import>