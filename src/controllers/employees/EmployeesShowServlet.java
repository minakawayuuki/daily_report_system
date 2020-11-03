package controllers.employees;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Employee;
import utils.DBUtil;

/**
 * Servlet implementation class EmployeesShowServlet
 */
@WebServlet("/employees/show")
public class EmployeesShowServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public EmployeesShowServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        /*
        DBUtilのcreateEntityManager()を実行しEntityManagerを作成し
        EntityManager型のemに入れる*/
        EntityManager em = DBUtil.createEntityManager();

        /*
        フォームから入手したid(社員id)をint型にキャストしEmployee.class(Employee.java)から探して
        Employee型の変数eに入れる*/
        Employee e = em.find(Employee.class, Integer.parseInt(request.getParameter("id")));
        // emを閉じる
        em.close();

        // eをjspで使えるようにemployeeに入れる
        request.setAttribute("employee", e);

        // ビューにするjspをしてい
        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/employees/show.jsp");
        // レスポンス画面としてjspファイルを呼び出します
        rd.forward(request, response);
    }

}
