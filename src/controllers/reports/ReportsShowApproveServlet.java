package controllers.reports;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Employee;
import models.Report;
import utils.DBUtil;

/**
 * Servlet implementation class ReportsShowApproveServlet
 */
@WebServlet("/reports/show/approve")
public class ReportsShowApproveServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReportsShowApproveServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EntityManager em = DBUtil.createEntityManager();

        Report r = em.find(Report.class, Integer.parseInt(request.getParameter("id")));

        Employee login_employee = (Employee)request.getSession().getAttribute("login_employee");

        if(login_employee.getPosition_flag() == 0){
            em.close();
            request.getSession().setAttribute("flush", "今の役職では承認機能は使えません");
        } else if(login_employee.getPosition_flag() >= r.getEmployee().getPosition_flag()){
            if(r != null && login_employee.getId() == r.getEmployee().getId()) {
                em.close();
                request.getSession().setAttribute("flush", "自分の日報です");
            } else if(r != null && login_employee.getId() != r.getEmployee().getId()) {
                if(r.getApproval() == 0) {
                    Integer al = r.getApproval();

                    r.setApproval(al + 1);

                    em.getTransaction().begin();
                    em.persist(r);
                    em.getTransaction().commit();
                    em.close();

                    request.getSession().setAttribute("flush", "承認しました");
                } else {
                    em.close();
                    request.getSession().setAttribute("flush", "承認済みです");
                }
            } else {
                em.close();
                request.getSession().setAttribute("flush", "日報が見つかりません");
            }
        } else {
            em.close();
            request.getSession().setAttribute("flush", "自分より役職が上の人の日報は承認できません");
        }
        request.getSession().removeAttribute("id");

        response.sendRedirect(request.getContextPath() + "/reports/index");
    }
}