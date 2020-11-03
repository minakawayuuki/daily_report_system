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
 * Servlet implementation class EmployeesCreateServlet
 */
@WebServlet("/employees/create")
public class EmployeesCreateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // _tokenをString型にキャストしたのをString型の_tokenに入れる
        String _token = (String)request.getParameter("_token");
        // _tokenがnullでない時かつ_token(送られてきたセッションId)がセッションidと同じ時実行
        if(_token != null && _token.equals(request.getSession().getId())) {
            /*
            DBUtilのcreateEntityManager()を実行しEntityManagerを作成し
            EntityManager型のemに入れる
             */
            EntityManager em = DBUtil.createEntityManager();
            // Employee()クラスをインスタンス化してEmployee型の変数eに入れる
            Employee e = new Employee();

            // フォームからパラメータを受け取り変数codeに入れe（Employee）のCodeにセットする
            e.setCode(request.getParameter("code"));
            e.setName(request.getParameter("name"));
            e.setPassword(
                    /*
                    EncryptUtilのgetPasswordEncryptメソッドの第一引数にフォームから受け取った
                    passwordを指定
                     */
                    EncryptUtil.getPasswordEncrypt(request.getParameter("password"),
                            // 第二引数にString型にキャストしたpepperを指定
                            (String)this.getServletContext().getAttribute("pepper")
                            )
                    );
            e.setAdmin_flag(Integer.parseInt(request.getParameter("admin_flag")));

            // Timestampは日時情報を小数点以下の秒数値まで保持することができるクラス
            // System.currentTimeMillis()メソッドはミリ秒で表される現在の時間を返す
            /*
            Timestamp(System.currentTimeMillis())をインスタンス化して
            Timestamp型の変数currentTimeとして使えるようにする       */
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            // currentTime(Timestamp())で取得した現在の日時情報をe(Employee)のCreated_atにセットする
            e.setCreated_at(currentTime);
            e.setUpdated_at(currentTime);
            // Delete_flagに0（削除していない状態）と入れる
            e.setDelete_flag(0);

            // EmployeeValidatorのvalidate()メソッドの引数を指定してList<String>型の変数errorsを作成
            List<String> errors = EmployeeValidator.validate(e, true, true);
            // errorsリストに一つでも登録されていたら（エラーが１つでもあったら）実行
            if(errors.size() > 0) {
                // em(Entitymanager)を閉じる
                em.close();

                // セッションIdを変数_tokenに入れる
                request.setAttribute("_token", request.getSession().getId());
                request.setAttribute("employee", e);
                request.setAttribute("errors", errors);

                // ビューにするjapをRequestDispatcher型のrdに入れる
                RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/employees/new.jsp");
                // forward()メソッドでresponse画面にレスポンス画面としてjaspファイルを呼び出す
                rd.forward(request, response);
            } else {
                // getTransaction().begin()はトランザクション処理の開始を指示
                em.getTransaction().begin();
                // e(EntityManager)のインスタンスを管理および永続化する
                em.persist(e);
                // commit()は現在のリソーストランザクションをコミットし、フラッシュされていない変更をデータベースに書き込む
                // Transaction()をcommitする
                em.getTransaction().commit();
                em.close();
                // セッションスコープに"登録が完了しました。"が入って変数flushを登録
                request.getSession().setAttribute("flush", "登録が完了しました。");

                // request.getContextPath()は自動的にコンテキストパスの文字列に置き換わる
                // sendRedirect()はあるサーブレットから別のサーブレットに自動でうつり変える際に使用
                response.sendRedirect(request.getContextPath() + "/employees/index");
            }
        }
    }

}