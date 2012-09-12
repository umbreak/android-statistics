package jabx.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.Polymorphism;
import org.hibernate.annotations.PolymorphismType;

import com.sun.istack.NotNull;

@XmlRootElement
@Entity
//@Inheritance(strategy=InheritanceType.JOINED)
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@Table(name = "chart")

//@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Polymorphism(type = PolymorphismType.EXPLICIT)

@DiscriminatorValue("BaseChartModel") @DiscriminatorColumn(name="discriminator", discriminatorType=DiscriminatorType.STRING)
public class BaseChartModel {
	private int id;
	private String name;
	private String description;
	private String xLegend;
	private String yLegend;
	private int votes;
	private int firstYear;
	private int lastYear;
	private Date date;
	private CategoryModel category;
	

	public BaseChartModel(int id, String name, String description,
			String xLegend, String yLegend, int votes, int type, int firstYear,
			int lastYear, Date date, CategoryModel category) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.xLegend = xLegend;
		this.yLegend = yLegend;
		this.votes = votes;
		this.firstYear = firstYear;
		this.lastYear = lastYear;
		this.date = date;
		this.category = category;
	}


	public BaseChartModel(String name, String description, int firstYear, int lastYear) {
		this(name,description);
		this.firstYear=firstYear;
		this.lastYear=lastYear;
	}
	
	public BaseChartModel(String name, String description) {
		this();
		this.name=name;
		this.description=description;
	}
	
	public BaseChartModel() {
		super();
		votes=0;
		xLegend="Leyenda X";
		yLegend="Leyenda Y";
		date= new Date();

	}

	
	@Id @GeneratedValue @Column(name = "chart_id")
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getxLegend() {
		return xLegend;
	}
	public void setxLegend(String xLegend) {
		this.xLegend = xLegend;
	}
	public String getyLegend() {
		return yLegend;
	}
	public void setyLegend(String yLegend) {
		this.yLegend = yLegend;
	}
	public int getVotes() {
		return votes;
	}
	
	public void setVotes(int votes) {
		this.votes = votes;
	}
	
	public int getFirstYear() {
		return firstYear;
	}
	public void setFirstYear(int firstYear) {
		this.firstYear = firstYear;
	}
	public int getLastYear() {
		return lastYear;
	}
	public void setLastYear(int lastYear) {
		this.lastYear = lastYear;
	}
	@Temporal(TemporalType.TIMESTAMP) @NotNull @Column(updatable=false)
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	@ManyToOne (cascade = CascadeType.MERGE)
	@JoinColumn(name="category_id")
	 public CategoryModel getCategory() {
		return category;
	}
	public void setCategory(CategoryModel category) {
		this.category = category;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + id;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BaseChartModel other = (BaseChartModel) obj;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (id != other.id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "BaseChartModel [id=" + id + ", name=" + name + ", description="
				+ description + ", xLegend=" + xLegend + ", yLegend=" + yLegend
				+ ", votes=" + votes + ", date=" + date
				+ ", category=" + category + "]";
	}
	
	
}
