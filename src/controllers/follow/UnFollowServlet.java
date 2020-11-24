package controllers.follow;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Follow;
import utils.DBUtil;

/**
 * Servlet implementation class UnFollowServlet
 */
@WebServlet("/unfollow")
public class UnFollowServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public UnFollowServlet() {
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
            request.getSession().setAttribute("flush", "そのような人はフォローしていません");
        } else {
            em.getTransaction().begin();
            em.remove(f);
            em.getTransaction().commit();
            em.close();
            request.getSession().setAttribute("flush_name", f.getEmployee_id().getName());
            request.getSession().setAttribute("flush", "フォロー解除しました");
        }
        response.sendRedirect(request.getContextPath() + "/follow/index");
    }

}