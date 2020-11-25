package controllers.unapproved;

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
import models.Report;
import utils.DBUtil;

/**
 * Servlet implementation class UnapprovedIndexServlet
 */
@WebServlet("/unapproved/index")
public class UnapprovedIndexServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public UnapprovedIndexServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EntityManager em = DBUtil.createEntityManager();

        Employee login_employee = (Employee)request.getSession().getAttribute("login_employee");

        if(login_employee.getPosition_flag() == 1 || login_employee.getPosition_flag() == 2) {
            int page;

            try{
                page = Integer.parseInt(request.getParameter("page"));
            } catch(Exception e) {
                page = 1;
            }

            List<Report> unapproveds = em.createNamedQuery("getAllUnapproved", Report.class)
                    .setFirstResult(15 * (page - 1))
                    .setMaxResults(15)
                    .getResultList();

            long unapproveds_count = (long)em.createNamedQuery("getUnapprovedCount", Long.class)
                    .getSingleResult();

            Long[] followChecks = new Long [(int)unapproveds_count];
            // 配列指定に使う数字をint型の変数iで作成
            int i = 0;
            /*
            for(データ型 変数名 : 配列名またはコレクション)
            Report型のrを作成しList<Report>型の変数reportの数だけ拡張for文を実行する*/
            for(Report r : unapproveds){
                // 日報を登録した人物をフォローしているかのチェック
                long followCheck = em.createNamedQuery("FollowedCheck", Long.class)
                        .setParameter("my_id", login_employee)
                        // 日報作成者のid
                        .setParameter("employee_id", r.getEmployee())
                        .getSingleResult();
                // i番目のfollowCheckの結果をi配列目に入れる
                followChecks[i] = followCheck;
                // 配列の数字を１増やす
                i++;
            }

            em.close();

            request.setAttribute("unapproveds", unapproveds);
            request.setAttribute("unapproveds_count", unapproveds_count);
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

            RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/unapproved/index.jsp");
            rd.forward(request, response);
        } else {
            em.close();
            request.getSession().setAttribute("flush", "その機能は今の役職では使えません");

            RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/topPage/index.jsp");
            rd.forward(request, response);
        }

    }
}