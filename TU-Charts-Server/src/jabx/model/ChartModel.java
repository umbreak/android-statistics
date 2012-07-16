package jabx.model;

import hibernate.types.StringDoubleType;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;


import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Sort;
import org.hibernate.annotations.SortType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

@XmlRootElement
@Entity 
//@PrimaryKeyJoinColumn(name="chart_id")

@DiscriminatorValue("chartModel")
@TypeDef(name="StringDouble", typeClass = StringDoubleType.class)

public class ChartModel extends BaseChartModel{
	private UserModel user;
	private double[] xValues;
	private Set<SerieModel> yValues = new LinkedHashSet<>();
	private Set<CommentModel> comments = new HashSet<>();

	public ChartModel() {
		super();
	}

	public ChartModel(String name, String description) {
		super(name,  description);

	}
	public ChartModel(String name, String description, double[] xValues, Set<SerieModel> yValues) {
		this(name,description);
		this.xValues=xValues;
		this.yValues=yValues;
	}
	
	@ManyToOne (cascade = CascadeType.MERGE)
	@JoinColumn(name="user_email")
	public UserModel getUser() {
		return user;
	}
	public void setUser(UserModel user) {
		this.user = user;
	}



	//	@ElementCollection
	//	@CollectionTable(name="x_values", joinColumns=@JoinColumn(name="chart_id"))
	//	@Column(name="x_value")
	@Type(type="StringDouble")
	public double[] getxValues() {
		return xValues;
	}

	public void setxValues(double[] xValues) {
		this.xValues = xValues;
	}

	//	@ManyToMany(fetch=FetchType.EAGER)
	//	@JoinTable (name = "chart_series",
	//				joinColumns=@JoinColumn(name = "chart_id"),
	//				inverseJoinColumns=@JoinColumn(name = "series_id"))
	//	@LazyCollection(LazyCollectionOption.FALSE)
	@OneToMany(fetch=FetchType.EAGER)
	@JoinColumn(name="chart_id")
	@LazyCollection(LazyCollectionOption.FALSE)
	@OrderBy("id")
//	@Sort(type = SortType.NATURAL)
	public Set<SerieModel> getyValues() {
		return yValues;
	}
	public void setyValues(Set<SerieModel> yValues) {
		this.yValues = yValues;
	}
	public void setyValue(SerieModel yValue) {
		yValues.add(yValue);
	}

	//	@OneToMany(fetch=FetchType.EAGER)
	//	@JoinTable (name = "chart_comment",
	//				joinColumns=@JoinColumn(name = "chart_id"),
	//				inverseJoinColumns=@JoinColumn(name = "comment_id"))
	//	@LazyCollection(LazyCollectionOption.FALSE)

	//	@OneToMany(fetch=FetchType.EAGER)
//	@JoinColumn(name="chart_id")
//	@LazyCollection(LazyCollectionOption.FALSE)
	@OneToMany(mappedBy="chart", fetch=FetchType.EAGER)
	@LazyCollection(LazyCollectionOption.FALSE)
	@JsonIgnore public Set<CommentModel> getComments() {
		return comments;
	}
	public void setComments(Set<CommentModel> comments) {
		this.comments = comments;
	}

	public void addComment(CommentModel comment) {
		comment.setChart(this);
		comments.add(comment);
	}
}
