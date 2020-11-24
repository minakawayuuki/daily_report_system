package controllers.reports;

import java.io.IOException;
import java.util.List;

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
 * Servlet implementation class ReportsUnFollowServlet
 */
@WebServlet("/reports/unfollow")
public class ReportsUnFollowServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReportsUnFollowServlet() {
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
            request.getSession().setAttribute("flush", "そのような人はフォローしていません");
        } else {
            Employee f_employee_id = r.getEmployee();

            Employee login_employee = (Employee)request.getSession().getAttribute("login_employee");

            Employee e_my_id = em.find(Employee.class, login_employee.getId());

            List<Follow> fd = em.createNamedQuery("FollowerDelete", Follow.class)
                    .setParameter("my_id", e_my_id)
                    .setParameter("employee_id", f_employee_id)
                    .getResultList();

            if(e_my_id == f_employee_id){
                em.close();
                request.getSession().setAttribute("flush", "自分です");
            } else if (fd.size() == 0){
                    em.close();
                    request.getSession().setAttribute("flush", "フォローしていません");
            } else {
                Follow my_id = fd.get(0);
                em.getTransaction().begin();
                em.remove(my_id);
                em.getTransaction().commit();
                em.close();
                request.getSession().setAttribute("flush_name", r.getEmployee().getName());
                request.getSession().setAttribute("flush", "フォロー解除しました");
            }
        }
        response.sendRedirect(request.getContextPath() + "/reports/index");
    }

}