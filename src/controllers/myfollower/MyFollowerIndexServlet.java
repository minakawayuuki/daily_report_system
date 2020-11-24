package controllers.myfollower;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Employee;
import models.Follow;
import utils.DBUtil;

/**
 * Servlet implementation class MyFollowerIndexServlet
 */
@WebServlet("/myfollower/index")
public class MyFollowerIndexServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public MyFollowerIndexServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EntityManager em = DBUtil.createEntityManager();

        Employee login_employees = (Employee)request.getSession().getAttribute("login_employee");

        Employee e_my_id = em.find(Employee.class, login_employees.getId());

        int page;
        try{
            page = Integer.parseInt(request.getParameter("page"));
        } catch(Exception e) {
            page = 1;
        }

        // 自分をフォローしてくれた人を調べる
        List<Follow> followers = em.createNamedQuery("getMyAllFollower", Follow.class)
                .setParameter("employee_id", e_my_id)
                .setFirstResult(15 * (page - 1))
                .setMaxResults(15)
                .getResultList();

        // 自分をフォローしてくれた人の人数を調べる
        long followers_count = (long)em.createNamedQuery("getMyFollowerCount", Long.class)
                .setParameter("employee_id", e_my_id)
                .getSingleResult();

        Employee login_employee = (Employee)request.getSession().getAttribute("login_employee");

        Long[] followChecks = new Long[(int)followers_count];

        int i = 0;
        for(Follow f : followers){
            // 取得した日報をフォローしているかのチェック
            long followCheck = em.createNamedQuery("FollowedCheck", Long.class)
                    .setParameter("my_id", login_employee)
                    .setParameter("employee_id", f.getMy_id())
                    .getSingleResult();

            followChecks[i] = followCheck;
            i++;
        }

        em.close();

        request.setAttribute("followers", followers);
        request.setAttribute("followers_count", followers_count);
        request.setAttribute("followChecks", followChecks);
        request.setAttribute("page", page);

        if(request.getSession().getAttribute("flush") != null) {
            request.setAttribute("flush", request.getSession().getAttribute("flush"));
            request.getSession().removeAttribute("flush");
        }

        if(request.getSession().getAttribute("flush_name") != null) {
            request.setAttribute("flush_name", request.getSession().getAttribute("flush_name"));
            request.getSession().removeAttribute("flush_name");
        }

        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/myfollower/index.jsp");
        rd.forward(request, response);
    }

}
