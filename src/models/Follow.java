package models;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Table(name="follow")
@NamedQueries({
    @NamedQuery(
            name = "getMyAllFollow",
            // 自分がフォローした人を表示する
            query = "SELECT f FROM Follow AS f WHERE f.my_id = :my_id ORDER BY f.id DESC"
            ),
    @NamedQuery(
            name = "getMyFollowCount",
            // 自分がフォローした人の人数を数える
            query = "SELECT COUNT(f) FROM Follow AS f WHERE f.my_id = :my_id"
            ),
    @NamedQuery(
            name = "FollowedCheck",
            // フォローしているかを調べる
            query = "SELECT COUNT(f) FROM Follow AS f WHERE f.my_id = :my_id AND f.employee_id = :employee_id"
            ),
    @NamedQuery(
            name = "getMyAllFollower",
            // 自分をフォローしてくれた人を調べる
            query ="SELECT f FROM Follow AS f WHERE f.employee_id = :employee_id ORDER BY f.id DESC"
            ),
    @NamedQuery(
            name = "getMyFollowerCount",
            // 自分をフォローしてくれた人の人数を調べる
            query = "SELECT COUNT(f) FROM Follow AS f WHERE f.employee_id = :employee_id"
            ),
    @NamedQuery(
            name = "FollowerDelete",
            query = "SELECT f FROM Follow AS f WHERE f.my_id = :my_id AND f.employee_id = :employee_id"
            ),
})
@Entity
public class Follow {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    // 自分のid
    @JoinColumn(name = "my_id", nullable = false)
    private Employee my_id;

    @ManyToOne
    // フォローした人のid
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee_id;

    @Column(name = "created_at", nullable = false)
    private Timestamp created_at;

    @Column(name = "updated_at", nullable = false)
    private Timestamp updated_at;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Employee getMy_id() {
        return my_id;
    }

    public void setMy_id(Employee my_id) {
        this.my_id = my_id;
    }

    public Employee getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(Employee employee_id) {
        this.employee_id = employee_id;
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
}
