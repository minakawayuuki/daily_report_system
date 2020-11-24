package controllers.reports;

import java.io.IOException;
import java.sql.Timestamp;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Employee;
import models.Follow;
import models.Report;
import utils.DBUtil;

/**
 * Servlet implementation class ReportsToFollowServlet
 */
@WebServlet("/reports/tofollow")
public class ReportsToFollowServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReportsToFollowServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EntityManager em = DBUtil.createEntityManager();

        Report r = em.find(Report.class, Integer.parseInt(request.getParameter("id")));

        if(r == null){
            em.close();
            request.getSession().setAttribute("flush", "そのような日報を登録した人はいません");
        } else {
            Employee employee_id = r.getEmployee();

            Employee login_employee = (Employee)request.getSession().getAttribute("login_employee");

            Employee e_my_id = em.find(Employee.class, login_employee.getId());

            long employees_count = (long)em.createNamedQuery("FollowedCheck", Long.class)
                    .setParameter("my_id", e_my_id)
                    .setParameter("employee_id", employee_id)
                    .getSingleResult();

            if(r != null && login_employee.getId() == r.getEmployee().getId()) {
                em.close();
                request.getSession().setAttribute("flush", "自分です");
            } else if (r != null && login_employee.getId() != r.getEmployee().getId() && employees_count > 0){
                em.close();
                request.getSession().setAttribute("flush", "すでにフォローしています");
            } else if (r != null && login_employee.getId() != r.getEmployee().getId() && employees_count == 0){
                Follow follow = new Follow();

                follow.setMy_id((Employee)request.getSession().getAttribute("login_employee"));
                follow.setEmployee_id(r.getEmployee());

                Timestamp currentTime = new Timestamp(System.currentTimeMillis());
                follow.setCreated_at(currentTime);
                follow.setUpdated_at(currentTime);

                em.getTransaction().begin();
                em.persist(follow);
                em.getTransaction().commit();
                em.close();

                request.getSession().setAttribute("flush_name", r.getEmployee().getName());
                request.getSession().setAttribute("flush", "フォローしました");
            } else {
                em.close();
                request.getSession().setAttribute("flush", "まだ登録されていません");
            }
        }

        response.sendRedirect(request.getContextPath() + "/reports/index");
    }

}