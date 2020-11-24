package controllers.myfollower;

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
import utils.DBUtil;

/**
 * Servlet implementation class ToMyFollowerServlet
 */
@WebServlet("/tomyfollower")
public class ToMyFollowerServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ToMyFollowerServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EntityManager em = DBUtil.createEntityManager();

        Follow f = em.find(Follow.class, Integer.parseInt(request.getParameter("id")));

        if(f == null){
            em.close();
            request.getSession().setAttribute("flush", "そのような日報を登録した人はいません");
        } else {

            Employee employee_id = (Employee)f.getMy_id();

            Employee login_employee = (Employee)request.getSession().getAttribute("login_employee");

            Employee e_my_id = em.find(Employee.class, login_employee.getId());

            long employees_count = (long)em.createNamedQuery("FollowedCheck", Long.class)
                    .setParameter("my_id", e_my_id)
                    .setParameter("employee_id", employee_id)
                    .getSingleResult();

            if(f != null && login_employee.getId() == f.getMy_id().getId()) {
                em.close();
                request.getSession().setAttribute("flush", "自分です");
            } else if (f != null && login_employee.getId() != f.getMy_id().getId() && employees_count > 0){
                em.close();
                request.getSession().setAttribute("flush", "すでにフォローしています");
            } else if (f != null && login_employee.getId() != f.getMy_id().getId() && employees_count == 0){
                Follow follow = new Follow();

                follow.setMy_id((Employee)request.getSession().getAttribute("login_employee"));
                follow.setEmployee_id(f.getMy_id());

                Timestamp currentTime = new Timestamp(System.currentTimeMillis());
                follow.setCreated_at(currentTime);
                follow.setUpdated_at(currentTime);

                em.getTransaction().begin();
                em.persist(follow);
                em.getTransaction().commit();
                em.close();

                request.getSession().setAttribute("flush_name", f.getMy_id().getName());
                request.getSession().setAttribute("flush", "フォローしました");
            } else {
                em.close();
                request.getSession().setAttribute("flush", "まだ登録されていません");
            }
        }


        response.sendRedirect(request.getContextPath() + "/myfollower/index");
    }
}