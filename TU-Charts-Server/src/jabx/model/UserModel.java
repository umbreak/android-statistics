package jabx.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@XmlRootElement
@Entity
@Table (name ="users")
public class UserModel {
	private int id;
	private String username;
	private String password;
	private String surname;
	private String description;
	private String email;
	private Set<ChartModel> charts = new HashSet<>();
	private Set<CommentModel> comments = new HashSet<>();
	
	public UserModel() {
		super();
	}
	
	
	public UserModel(String username, String password, String email) {
		super();
		this.username = username;
		this.password = password;
		this.email = email;
	}


	@Id @GeneratedValue @Column(name = "user_id")
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	@OneToMany(mappedBy="user", fetch=FetchType.EAGER)
	@LazyCollection(LazyCollectionOption.FALSE)
	@XmlTransient public Set<ChartModel> getCharts() {
		return charts;
	}

	public void setCharts(Set<ChartModel> charts) {
		this.charts = charts;
	}
	public void setChart(ChartModel chart) {
		chart.setUser(this);
		charts.add(chart);
	}
	
	@OneToMany(mappedBy="user", fetch=FetchType.EAGER)
	@LazyCollection(LazyCollectionOption.FALSE)
	@XmlTransient public Set<CommentModel> getComments() {
		return comments;
	}

	public void setComments(Set<CommentModel> comments) {
		this.comments = comments;
	}
	public void addComment(CommentModel comment) {
		comment.setUser(this);
		comments.add(comment);
	}
	
}
