package controllers.login;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Employee;
import utils.DBUtil;
import utils.EncryptUtil;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    // ログイン画面を表示
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // ビューでセッションIdを＿tokenで使えるようにする
        request.setAttribute("_token", request.getSession().getId());
        // まだログインしていないのでエラーメッセージが出ないようにfalseを入れておく
        // ビューでfalseをhasErrorで使えるようにする
        request.setAttribute("hasError", false);
        // セッションスコープがnullでない時に実行
        if(request.getSession().getAttribute("flush") != null) {
            // ビューでセッションスコープのflushをflushとして使えるようにする
            request.setAttribute("flush", request.getSession().getAttribute("flush"));
            // flushをセッションスコープから削除
            request.getSession().removeAttribute("flush");
        }
        // ビューの指定
        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/login/login.jsp");
        // forward()メソッドでレスポンス画面としてビューを呼び出す
        rd.forward(request, response);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    // ログイン処理を実行
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 認証結果を格納する変数
        // Boolean型の変数check_resultにfalseを入れ作成
        Boolean check_result = false;

        // フォームから入手した"code"をString型の変数codeに入れる
        String code = request.getParameter("code");
        // フォームから入手した"password"をString型の変数plain_passに入れる
        String plain_pass = request.getParameter("password");
        // nullをEmployee型の変数に入れ作成
        Employee e = null;

        /*
         codeがnullでない時かつcodeが空文字じゃない時かつ
         plain_passがnullでない時かつplain＿passが空文字じゃない時に実行 */
        if(code != null && !code.equals("") && plain_pass != null && !plain_pass.equals("")) {
            EntityManager em = DBUtil.createEntityManager();

            /*
            EncryptUtil.javaのgetPasswordEncrypt()メソッドの第一引数にplain_passを
            第二引数に"pepper"を指定しメソッドを実行してString型の変数password入れる */
            // ハッシュ化する
            String password = EncryptUtil.getPasswordEncrypt(
                    plain_pass,
                    (String)this.getServletContext().getAttribute("pepper")
                    );
            // 社員番号とパスワードが正しいかチェックする
            try {
                /*
                em(データベース)の中を戻り値がEmployeeのNamedQueryをcheckLoginCodeAndPasswordを実行し
                変数eを上書き*/
                e = em.createNamedQuery("checkLoginCodeAndPassword", Employee.class)
                      // :codeにcodeを入れる
                      .setParameter("code", code)
                      // :passにpasswordを入れる
                      .setParameter("pass", password)
                      // getSingleResult()メソッドは一件のみ取得する時に使用
                      .getSingleResult();
              // 例外処理NoResultExceptionは検索結果が０県である場合にスローされる
            } catch(NoResultException ex){}
            // em(データベース)を閉じる
            em.close();

            // eがnullでない時eをtureに上書き
            if(e != null) {
                check_result = true;
            }
        }

        // check_resultがtrueでなければ実行
        if(!check_result) {
            // 認証できなかったらログイン画面に戻る
            // ビューで使えるようにセッションIdを_tokenに入れる
            request.setAttribute("_token", request.getSession().getId());
            request.setAttribute("hasError", true);
            // ビューで使えるようにcodeをcodeに入れる
            request.setAttribute("code", code);
            // ビューを指定
            RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/login/login.jsp");
            // forward()メソッドでレスポンス画面としてjspファイルを呼び出します
            rd.forward(request, response);
        } else {
            // 認証できたらログイン状態にしてトップページへリダイレクト
            // セッションスコープにeというEmployeeが入った変数login_employeeを登録
            request.getSession().setAttribute("login_employee", e);
            //セッションスコープに"ログインしました。"という文字列が入った変数flushを登録
            request.getSession().setAttribute("flush", "ログインしました。");
            // topPage/index.jspにリダイレクトする
            response.sendRedirect(request.getContextPath() + "/");
        }
    }

}
