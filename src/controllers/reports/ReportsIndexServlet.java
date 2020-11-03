package controllers.reports;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Report;
import utils.DBUtil;

/**
 * Servlet implementation class ReportsIndexServlet
 */
@WebServlet("/reports/index")
public class ReportsIndexServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReportsIndexServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        /*
        DBUtilのcreateEntityManager()を実行しEntityManagerを作成し
        EntityManager型の変数emを作成する*/
        EntityManager em = DBUtil.createEntityManager();

        // int型の変数pageを定義
        int page;
        try{
            /*
            request.getParameterでpageをフォームから入手すしint型にキャストしたものを
            先ほどの変数pageに入れる                                              */
            page = Integer.parseInt(request.getParameter("page"));
          // 例外処理(ErrorやRuntimeException以外の例外、別名検査例外とも呼ばれる)
        } catch(Exception e) {
            // 変数pageに１を入れる
            page = 1;
        }
        /*
        ReportクラスのNamedQueryのgetAllReportsをおこなったのを
        List<Report>型の変数reportsに入れ作成*/
        List<Report> reports = em.createNamedQuery("getAllReports", Report.class)
                                  // 検索結果を習得する最初のポジションを指定する
                                  .setFirstResult(15 * (page - 1))
                                  // 習得する結果の最大件数を設定
                                  .setMaxResults(15)
                                  // SELECTクエリーを実行し問合せ結果を型のないリストとして返す
                                  // getResultList()メソッドは一件以上取得する際に使用
                                  .getResultList();

        /*
        ReportクラスのNamedQueryのgetReportsCountをおこなったのを
        long型のreports_countに入れ作成*/
        long reports_count = (long)em.createNamedQuery("getReportsCount", Long.class)
                                  // SELECTクエリーを実行し問合せ結果を型のないリストとして返す
                                  .getSingleResult();
        // em(EntityManager)を閉じる
        em.close();

        // 変数reportsをjspでreportsとして使えるようにする
        request.setAttribute("reports", reports);
        request.setAttribute("reports_count", reports_count);
        request.setAttribute("page", page);
        // ただ戻っただけなのに更新したflushなどが表示されないようにするため
        // flushがnullでない時に実行
        if(request.getSession().getAttribute("flush") != null) {
            // flushをjspでflushで使えるようにする
            request.setAttribute("flush", request.getSession().getAttribute("flush"));
            // flusheをセッションスコープから削除
            request.getSession().removeAttribute("flush");
        }

        // /reports/index.jspをビューに指定する
        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/reports/index.jsp");
        // レスポンス画面としてjspファイルを呼び出す
        rd.forward(request, response);
    }

}
