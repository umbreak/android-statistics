package models;

import hibernate.types.StringLongType;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
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
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import com.google.common.base.Strings;
import com.google.common.primitives.Longs;

@XmlRootElement
@Entity 
//@PrimaryKeyJoinColumn(name="chart_id")

@DiscriminatorValue("chartModel")
@TypeDef(name="StringLong", typeClass = StringLongType.class)

public class ChartModel extends BaseChartModel{
	private UserModel user;
	private long[] xValues;
	private Set<SerieModel> yValues = new LinkedHashSet<>();
	private Set<CommentModel> comments = new HashSet<>();

	public ChartModel() {
		super();
	}
	public ChartModel(String name, String description, long[] xValues, Set<SerieModel> yValues) {
		super(name,description);
		this.xValues=xValues;
		this.yValues=yValues;
	}
	public ChartModel(String name, String description, long[] xValues, Set<SerieModel> yValues, int firstYear, int lastYear) {
		super(name,description,firstYear,lastYear);
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
	@Type(type="StringLong")
	public long[] getxValues() {
		return xValues;
	}

	public void setxValues(long[] xValues) {
		this.xValues = xValues;
	}
	private void addXValues(long[] newXValues){
		xValues=Longs.concat(xValues,newXValues);
	}
	public void updateChart(ChartModel c){
		if (!Strings.isNullOrEmpty(c.getName())) setName(c.getName());
		if (!Strings.isNullOrEmpty(c.getDescription())) setDescription(c.getDescription());
		if (!Strings.isNullOrEmpty(c.getxLegend())) setxLegend(c.getxLegend());
		if (!!Strings.isNullOrEmpty(c.getyLegend())) setyLegend(c.getyLegend());
		c.setLastYear(c.getLastYear());
		setDate(new Date(System.currentTimeMillis()));

		addXValues(c.getxValues());
		addyValues(c.getyValues());	
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
	private void addyValues(Set<SerieModel> newYValues){
		for (SerieModel series: yValues) {
			Iterator<SerieModel> newSeries= newYValues.iterator();
			series.updateSeries(newSeries.next());
		}
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
