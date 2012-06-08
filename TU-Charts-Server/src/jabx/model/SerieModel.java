package jabx.model;

import hibernate.types.StringLongType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

@XmlRootElement
@Entity
@Table (name ="series")
@TypeDef(name="StringLong", typeClass = StringLongType.class)
public class SerieModel {
	private int id;
	private String name;
	private String description;
	private double[] yvalues;
	
	public SerieModel() {
		super();
	}
	public SerieModel(String name, double[] values){
		super();
		this.name=name;
		this.yvalues=values;
	}
	
	@Id @GeneratedValue @Column(name = "series_id")
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
	@Type(type="StringLong")
	public double[] getYvalues() {
		return yvalues;
	}
	public void setYvalues(double[] yvalues) {
		this.yvalues = yvalues;
	}
	
}
