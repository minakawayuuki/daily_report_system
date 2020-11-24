package controllers.employees;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Employee;
import models.validators.EmployeeValidator;
import utils.DBUtil;
import utils.EncryptUtil;

/**
 * Servlet implementation class EmployeesUpdateServlet
 */
@WebServlet("/employees/update")
public class EmployeesUpdateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public EmployeesUpdateServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // フォームから入手したものを"_teken"に入れString型にキャストしたものをString型の変数_tokenに入れる
        String _token = (String)request.getParameter("_token");
        // _tokenがnullでない時かつ_tokenのセッションidがリクエストスコープのセッションidと同じ時
        if(_token != null && _token.equals(request.getSession().getId())) {
            /*
            DBUtilのcreateEntityManager()メソッドを実行しEntityManagerを作成し
            EntityManager型の変数emに入れる */
            EntityManager em = DBUtil.createEntityManager();

            /*
            セッションスコープからInteger型にキャストしたemployee_id（社員id）を取り出し
            Employeeクラスから探してEmployee型の変数eに入れる */
            Employee e = em.find(Employee.class, (Integer)(request.getSession().getAttribute("employee_id")));

            // 現在の値と異なる社員番号が入力されていたら
            // 重複チェックを行う指定をする
            // Boolean型の変数code_duplicate_checkにtrueを入れる
            Boolean code_duplicate_check = true;
            // e(社員)のCodeとフォームから入手した"code"が等しい時実行
            if(e.getCode().equals(request.getParameter("code"))) {
                code_duplicate_check = false;
            // 等しくない時実行
            } else {
               e.setCode(request.getParameter("code"));
            }

            // パスワード欄に入力があったら
            // パスワードの入力値チェックを行う指定をする
            Boolean password_check_flag = true;
            String password = request.getParameter("password");
            if(password == null || password.equals("")) {
                password_check_flag = false;
            } else {
                e.setPassword(
                        EncryptUtil.getPasswordEncrypt(password,
                                (String)this.getServletContext().getAttribute("pepper")
                                )
                        );
            }

            e.setName(request.getParameter("name"));
            e.setAdmin_flag(Integer.parseInt(request.getParameter("admin_flag")));
            e.setPosition_flag(Integer.parseInt(request.getParameter("position_flag")));
            e.setUpdated_at(new Timestamp(System.currentTimeMillis()));
            e.setDelete_flag(0);

            List<String> errors = EmployeeValidator.validate(e, code_duplicate_check, password_check_flag);
            if(errors.size() > 0) {
                em.close();

                request.setAttribute("_token", request.getSession().getId());
                request.setAttribute("employee", e);
                request.setAttribute("errors", errors);

                RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/employees/edit.jsp");
                rd.forward(request, response);
            } else {
                em.getTransaction().begin();
                em.getTransaction().commit();
                em.close();
                request.getSession().setAttribute("flush", "更新が完了しました");

                request.getSession().removeAttribute("employee_id");

                response.sendRedirect(request.getContextPath() + "/employees/index");
            }
        }
    }

}
