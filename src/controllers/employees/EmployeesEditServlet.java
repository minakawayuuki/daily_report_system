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
 * Servlet implementation class EmployeesEditServlet
 */
@WebServlet("/employees/edit")
public class EmployeesEditServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public EmployeesEditServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        /*
        DBUtilのcreateEntityManager()を実行してEntityManagerを作成し
        EntityManager型の変数emに入れる*/
        EntityManager em = DBUtil.createEntityManager();

        /*
        request.getParameterで入手したidをint型にキャストしEmployee.class(Employee.java)に
        一致するものがないかを探して一致したものをEmployee型の変数eに入れる*/
        Employee e = em.find(Employee.class, Integer.parseInt(request.getParameter("id")));

        // emを閉じる
        em.close();

        // eをjspで使えるようにemployeeに入れる
        request.setAttribute("employee", e);
        request.setAttribute("_token", request.getSession().getId());
        request.getSession().setAttribute("employee_id", e.getId());

        // ビューにするjspを指定する
        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/employees/edit.jsp");
        // レスポンス画面としてjspファイルを呼び出します
        rd.forward(request, response);
    }

}
