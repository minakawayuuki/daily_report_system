package models;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

// @TableはEntityにマッピングされるテーブル名を指定
@Table(name = "employees")
// @NamedQueriesは更新などには使わないで、探したりする際に使用する
// @NamedQueriesは複数のNamedQueryをまとめたものNamedQueryを間まで区切り指定
@NamedQueries({
    // 主キー以外の項目などで検索し、複数件の結果を習得したい場合に定義する
    @NamedQuery(
            // query文にgetAllEmployeesと言う名前をつけた
            name = "getAllEmployees",
            // 全ての従業員情報を取得
            query = "SELECT e FROM Employee AS e ORDER BY e.id DESC"
            ),

    @NamedQuery(
            name = "getEmployeesCount",
            // 従業員情報の全件数を取得
            query = "SELECT COUNT(e) FROM Employee AS e"
            ),

    @NamedQuery(
            name = "checkRegisteredCode",
            // 指定された社員番号がすでにデータベースに存在しているか調べる
            query = "SELECT COUNT(e) FROM Employee AS e WHERE e.code = :code"
            ),

    @NamedQuery(
            name = "checkLoginCodeAndPassword",
            // 従業員がログインするときに社員番号とパスワードが正しいかチェックする
            query = "SELECT e FROM Employee AS e WHERE e.delete_flag = 0 AND e.code = :code AND e.password = :pass"
            ),
    @NamedQuery(
            name = "checkPositionFlag",
            // 自分より役職が上の人の情報を調べる
            query = "SELECT e From Employee AS e WHERE e.position_flag >= :position_flag"
            ),
})
// クラスがEntityクラスであることを指定
@Entity
public class Employee {
    // @Idは主キーのフィールドに指定
    @Id
    // @ColumnはEntityの各フィールドにマッピングされるテーブルのカラム名を指定
    @Column(name = "id")
    // @GeneratedValueは主キー値を自動採番すること
    // strategy属性に、その項目がどのようにして生成されるかを指定する
    /*
     GenerationType.IDENTITYはMySQLのAUTO_INCREMENTと同じ意味
     (１から始まり次に登録されたら自動的に値が１ずつ増えていく設定)
     */
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // @Column(nullable = true / false)はデフォルトの値にnullを許容 / 許容しない
    // @Column(unique = true)はすでに登録されているものは登録できない旨をデータベースに教えてあげるための設定
    @Column(name = "code", nullable = false, unique = true)
    // String型の変数codeを定義
    private String code;

    @Column(name = "name", nullable = false)
    private String name;

    // @Column(length = "最大桁の指定")
    @Column(name = "password", length = 64, nullable = false)
    private String password;

    @Column(name = "admin_flag", nullable = false)
    private Integer admin_flag;

    @Column(name = "created_at", nullable = false)
    private Timestamp created_at;

    @Column(name = "updated_at", nullable = false)
    private Timestamp updated_at;

    @Column(name = "delete_flag", nullable = false)
    private Integer delete_flag;

    // 役職(0,一般、1,課長、2,部長)
    @Column(name = "position_flag", nullable = false)
    private Integer position_flag;

    // Integer型のgetId()メソッド作成
    public Integer getId() {
        // idを呼び出し元に返す
        return id;
    }

    // 引数ありのsetId()メソッドを作成
    public void setId(Integer id) {
        // 引数で受け取ったidをプロパティ(this.id)に代入する
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getAdmin_flag() {
        return admin_flag;
    }

    public void setAdmin_flag(Integer admin_flag) {
        this.admin_flag = admin_flag;
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

    public Integer getDelete_flag() {
        return delete_flag;
    }

    public void setDelete_flag(Integer delete_flag) {
        this.delete_flag = delete_flag;
    }

    public Integer getPosition_flag() {
        return position_flag;
    }

    public void setPosition_flag(Integer position_flag) {
        this.position_flag = position_flag;
    }

}