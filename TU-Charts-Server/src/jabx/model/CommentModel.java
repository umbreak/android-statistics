package jabx.model;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.sun.istack.NotNull;

@XmlRootElement
@Entity
@Table (name ="comments")
public class CommentModel implements Comparable<CommentModel>{
	private int id;
	private String text;
	private Date date;
	private UserModel user;
	private ChartModel chart;
	
	public CommentModel() {
		super();
	}
	@Id @GeneratedValue @Column(name = "comment_id")
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
	@Temporal(TemporalType.TIMESTAMP) @NotNull @Column(updatable=false)
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	@ManyToOne (cascade = CascadeType.MERGE)
	@JoinColumn(name="user_id")
	public UserModel getUser() {
		return user;
	}
	public void setUser(UserModel user) {
		this.user = user;
	}
	@ManyToOne (cascade = CascadeType.MERGE)
	@JoinColumn(name="chart_id")
	@JsonIgnore
	public ChartModel getChart() {
		return chart;
	}
	public void setChart(ChartModel chart) {
		this.chart = chart;
	}
	@Override
	public int compareTo(CommentModel arg0) {
		return this.id - arg0.id;
	}
	@Override
	public String toString() {
		return "CommentModel [id=" + id + ", text="
				+ text + ", date=" + date + ", user=" + user + ", chart="
				+ chart + "]";
	}
	
	
	
	
}
