package controllers.employees;

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
import utils.DBUtil;

/**
 * Servlet implementation class EmployeesindexServlet
 */
@WebServlet("/employees/index")
public class EmployeesIndexServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public EmployeesIndexServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        /*
        DBUtilのcreateEntityManager()メソッドを実行しEntityManagerを作成し
        EntityManager型の変数emに入れる
        */
        EntityManager em = DBUtil.createEntityManager();
        // １をint型の変数pageに入れる
        int page = 1;
        try{
            // Integer.parseInt()は、文字列から数値に変更する
            /*
             request.getParameter()でえられるのはString型になるので
             request.getParameter()でゲットした変数をInteger型にキャストする
             */
            page = Integer.parseInt(request.getParameter("page"));
          /*
          NumberFormatExceptionはアプリケーションが文字列を数値型に変換しようとしたとき、
          文字列の形式が正しくない場合にスローされる
          */
          // e(Employees)の詳細メッセージを持つNumberFormatExceptionを構築します。
        } catch(NumberFormatException e) { }
        /*
        EmployeeクラスのgetAllEmployees（全ての従業員情報を取得）を実行した結果を
        Employeeクラス型の変数employeesリストに入れる
         */
        List<Employee> employees = em.createNamedQuery("getAllEmployees", Employee.class)
                // setFirstResult()は検索結果を取得する最初のポジションを設定する
                .setFirstResult(15 * (page - 1))
                // setMaxResults()は取得する結果の最大件数を設定します。
                .setMaxResults(15)
                // SELECTクエリーを実行し、問合せ結果を型のないリストとして返す
                .getResultList();
        // long型のemployees_countにemをlong型にキャストしgetEmployeesCountを実行した結果を入れる
        long employees_count = (long)em.createNamedQuery("getEmployeesCount", Long.class)
                // 一つの型のない結果を返すSELECTクエリーを実行します
                .getSingleResult();
        // emを閉じます
        em.close();

        // request.setAttribute("jspで扱えるようにする変数名", どの変数を入れるか)
        // 変数employeesをjspで使えるようにemployeesと言う変数にいれる
        request.setAttribute("employees", employees);
        request.setAttribute("employees_count", employees_count);
        request.setAttribute("page", page);
        // flushがnull(何もない)でない時に実行する
        if(request.getSession().getAttribute("flush") != null) {
            // jspで使える変数flusにセッションスコープのflushを入れる
            request.setAttribute("flush", request.getSession().getAttribute("flush"));
            // セッションスコープのflushを削除する
            request.getSession().removeAttribute("flush");
        }

        // getRequestDispatcher("ビューにするjsp")をRequestDispatcher型の変数rdに入れる
        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/employees/index.jsp");
        // forward()メソッドでレスポンス画面としてJSPファイルを呼び出します
        rd.forward(request, response);
    }

}