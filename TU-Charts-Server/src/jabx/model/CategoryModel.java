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

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@XmlRootElement
@Entity
@Table(name = "categories")
public class CategoryModel implements Comparable<CategoryModel>{
	
	private int id;
	private String name;
	private String description;
	private Set<BaseChartModel> charts = new HashSet<>();
	
	public CategoryModel() {
		super();
	}
	public CategoryModel(String name, String description) {
		super();
		this.name=name;
		this.description=description;
	}
	@Id @GeneratedValue @Column(name = "category_id")
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
	
	@OneToMany(mappedBy="category", fetch=FetchType.EAGER)
	@LazyCollection(LazyCollectionOption.FALSE)
	@JsonIgnore public Set<BaseChartModel> getCharts() {
		return charts;
	}
	
	public void setCharts(Set<BaseChartModel> charts) {
		this.charts = charts;
	}
	public void addChart(BaseChartModel chart){
		chart.setCategory(this);
		charts.add(chart);
	}
	@Override
	public int compareTo(CategoryModel arg0) {
		return this.getName().compareTo(arg0.getName());
	}
}
