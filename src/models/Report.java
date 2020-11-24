package models;

import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Table(name = "reports")
@NamedQueries({
    @NamedQuery(
            name = "getAllReports",
            // 全ての日報情報を取得
            query = "SELECT r FROM Report AS r ORDER BY r.id DESC"
            ),
    @NamedQuery(
            name = "getReportsCount",
            // 日報の前件数を取得
            query = "SELECT COUNT(r) FROM Report AS r"
            ),
    @NamedQuery(
            name = "getMyAllReports",
            // 自分の日報を先に登録した順番に並べ替える
            query = "SELECT r FROM Report AS r WHERE r.employee = :employee ORDER BY r.id DESC"
            ),
    @NamedQuery(
            name = "getMyReportsCount",
            // 指定された日報がすでにデータベースに存在しているかを調べる
            query = "SELECT COUNT(r) FROM Report AS r WHERE r.employee = :employee"
            ),
    @NamedQuery(
            name = "getAllUnapproved",
            // 未承認の日報を取得する
            query = "SELECT r FROM Report AS r WHERE r.approval = 0 ORDER BY r.id DESC"
            ),
    @NamedQuery(
            name = "getUnapprovedCount",
            // 未承認の日報が何件あるか調べる
            query = "SELECT COUNT(r) FROM Report AS r WHERE r.approval = 0"
            ),
})
@Entity
public class Report {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // @ManyToOneで多対一関連を定義
    @ManyToOne
    // @JoinColumnで統合に用いるカラムを指定
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    // date型は年月日のみ
    @Column(name = "report_date", nullable = false)
    private Date report_date;

    @Column(name = "title", length = 255, nullable = false)
    private String title;

    // @Lobとすることで改行もデータベースに保存される
    @Lob
    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "created_at", nullable = false)
    private Timestamp created_at;

    @Column(name = "updated_at", nullable = false)
    private Timestamp updated_at;

    @Column(name = "reaction_nice_cnt", nullable = false)
    private Integer reaction_nice_cnt;

    // 0なら未承認1なら承認済みを表すカラム
    @Column(name = "approval", nullable = false)
    private Integer approval;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Date getReport_date() {
        return report_date;
    }

    public void setReport_date(Date report_date) {
        this.report_date = report_date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

    public Timestamp getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Timestamp updated_at) {
        this.updated_at = updated_at;
    }

    public Integer getReaction_nice_cnt() {
        return reaction_nice_cnt;
    }

    public void setReaction_nice_cnt(Integer reactio_nice_cnt) {
        this.reaction_nice_cnt = reactio_nice_cnt;
    }

    public Integer getApproval() {
        return approval;
    }

    public void setApproval(Integer approval) {
        this.approval = approval;
    }

}