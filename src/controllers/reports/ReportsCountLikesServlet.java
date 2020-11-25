package controllers.reports;

import java.io.IOException;
import java.sql.Timestamp;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Employee;
import models.ReactionNiceTime;
import models.Report;
import utils.DBUtil;

/**
 * Servlet implementation class ReportsCountLikes
 */
@WebServlet("/reports/count/likes")
public class ReportsCountLikesServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReportsCountLikesServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        /*
        DBUtilのcreateEntityManager()メソッドを実行してEntityManagerを作成し
        EntityManager型の変数emに入れる*/
        EntityManager em = DBUtil.createEntityManager();

        Integer gamenn = Integer.parseInt(request.getParameter("gamenn"));
        /*
        フォームから入手した"id"をInteger型にキャストしてem(データベース)の
        Reportクラスから探したのをReport型の変数rに入れ作成 */
        Report r = em.find(Report.class, Integer.parseInt(request.getParameter("id")));
        /*
        セッションスコープから"login_employee"を取り出したのをEmployee型にキャストし
        Employee型の変数login_employeeに入れ作成*/
        Employee login_employee = (Employee)request.getSession().getAttribute("login_employee");

     // rがnullでない時かつlogin_employee(ログインしている人のId)とr(自分が今見ている日報のId)が等しい時実行
        if(r != null && login_employee.getId() == r.getEmployee().getId()) {
            em.close();
            // セッションスコープに"自分の日報です"という文字列が入った変数flushを登録
            request.getSession().setAttribute("flush", "自分の日報です");
  // rがnullでない時かつlogin_employee(ログインしている人のId)とr(自分が今見ている日報のId)が等しくない時実行
        } else if(r != null && login_employee.getId() != r.getEmployee().getId()) {
         // r.getReaction_nice_cnt()で現在のいいね数を取得している
            // 自分が見ている日報のreaction_nice_cnt(いいね数)をInteger型の変数rnctに入れ作成
            Integer rnct = r.getReaction_nice_cnt();
            // いいね数を一足す
            r.setReaction_nice_cnt(rnct + 1);

            // ReactionNiceTime()クラスをインスタンスかしてReactionNiceTime型の変数rntにいれる
            ReactionNiceTime rnt = new ReactionNiceTime();

            // セッションスコープのEmployee型のlogin_employeeのidをEmployeeにセットする
            rnt.setEmployee((Employee)request.getSession().getAttribute("login_employee"));
            // 現在見ている日報のidをReportにセットする
            rnt.setReport(r);
            /*
            Timestamp(Sysetem.currentTimeMillis())をインスタンス化して
            Timestmp型の変数currentTimeを作成*/
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            // currentTimeの日時情報をCreated_atにセットする
            rnt.setCreated_at(currentTime);
            // currentTimeの日時情報をUpdated_atにセットする
            rnt.setUpdated_at(currentTime);

            // トランザクション処理の開始
            em.getTransaction().begin();
            // persist()メソッドを使用してデータベースにセーブします
            em.persist(rnt);
            // データの更新を確定
            em.getTransaction().commit();
            // データベースを閉じる
            em.close();

            // セッションスコープに"いいねしました"という文字列が入った変数flushを登録
            request.getSession().setAttribute("flush", "いいねしました");
  // 日報が見つからない時に実行
        } else {
            em.close();
            // セッションスコープに"日報が見つかりません"という文字列が入った変数flushを登録
            request.getSession().setAttribute("flush", "日報が見つかりません");
        }
        // セッションスコープのidを削除
        request.getSession().removeAttribute("id");

        if(gamenn == 2){
            response.sendRedirect(request.getContextPath() + "/unapproved/index");
        } else{
            response.sendRedirect(request.getContextPath() + "/reports/index");
        }
    }
}