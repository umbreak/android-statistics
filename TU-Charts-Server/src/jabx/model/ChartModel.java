package jabx.model;

import hibernate.types.StringLongType;
import hibernate.types.StringtoArray;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

@XmlRootElement
@Entity @DiscriminatorValue("chartModel")
@TypeDef(name="StringArray", typeClass = StringtoArray.class)

public class ChartModel extends BaseChartModel{
	private UserModel user;

	private String[] xValues;
	private Set<SerieModel> yValues = new HashSet<>();
	private Set<CommentModel> comments = new HashSet<>();

	public ChartModel() {
		super();
	}

	public ChartModel(String name, String description) {
		super(name,  description);

	}

	public ChartModel(String name, String description, String[] xValues, Set<SerieModel> yValues) {
		this(name,description);
		this.xValues=xValues;
		this.yValues=yValues;
	}

	@ManyToOne (cascade = CascadeType.MERGE)
	@JoinColumn(name="user_id")
	public UserModel getUser() {
		return user;
	}
	public void setUser(UserModel user) {
		this.user = user;
	}



	//	@ElementCollection
	//	@CollectionTable(name="x_values", joinColumns=@JoinColumn(name="chart_id"))
	//	@Column(name="x_value")
	@Type(type="StringArray")
	public String[] getxValues() {
		return xValues;
	}

	public void setxValues(String[] xValues) {
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
	@OneToMany(fetch=FetchType.EAGER)
	@JoinColumn(name="chart_id")
	@LazyCollection(LazyCollectionOption.FALSE)
	@XmlTransient public Set<CommentModel> getComments() {
		return comments;
	}
	public void setComments(Set<CommentModel> comments) {
		this.comments = comments;
	}

	public void addComment(CommentModel comment) {
		comments.add(comment);
	}





}
