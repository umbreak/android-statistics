package jabx.model;

import hibernate.types.StringIntType;

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

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

@XmlRootElement
@Entity
@Table (name ="users")
@TypeDef(name="StringInt", typeClass = StringIntType.class)

public class UserModel {
	private String username;
	private String password;
	private String description;
	private String email;
	private Set<ChartModel> charts = new HashSet<>();
	private Set<CommentModel> comments = new HashSet<>();
	private int[] charts_denied;
	private int[] categories_denied;
	
	public UserModel() {
		super();
	}
	
	
	public UserModel(String username, String password, String email) {
		super();
		this.username = username;
		this.password = password;
		this.email = email;
	}
	public UserModel(String username, String password, String email, int cats[], int charts[]) {
		this(username,password,email);
		this.charts_denied=charts;
		this.categories_denied=cats;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@JsonIgnore public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@JsonIgnore public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	@Column(nullable = false, unique = true)
	@Id
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	@OneToMany(mappedBy="user", fetch=FetchType.EAGER)
	@LazyCollection(LazyCollectionOption.FALSE)
	@JsonIgnore public Set<ChartModel> getCharts() {
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
	@JsonIgnore public Set<CommentModel> getComments() {
		return comments;
	}

	public void setComments(Set<CommentModel> comments) {
		this.comments = comments;
	}
	public void addComment(CommentModel comment) {
		comment.setUser(this);
		comments.add(comment);
	}

	@Type(type="StringInt")
	@JsonIgnore
	public int[] getCharts_denied() {
		return charts_denied;
	}


	public void setCharts_denied(int[] charts_denied) {
		this.charts_denied = charts_denied;
	}

	@Type(type="StringInt")
	@JsonIgnore
	public int[] getCategories_denied() {
		return categories_denied;
	}


	public void setCategories_denied(int[] categories_denied) {
		this.categories_denied = categories_denied;
	}
	
	
}
