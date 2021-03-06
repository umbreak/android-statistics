package models;

import hibernate.types.StringDoubleType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import com.google.common.base.Strings;
import com.google.common.primitives.Doubles;
import com.google.common.primitives.Ints;

@XmlRootElement
@Entity
@Table (name ="series")
@TypeDef(name="StringDouble", typeClass = StringDoubleType.class)
public class SerieModel implements Comparable<SerieModel>{
	private int id;
	private int max;
	private int min;
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
		this.max=Ints.saturatedCast((long)Doubles.max(yvalues));
		this.min=Ints.saturatedCast((long)Doubles.min(yvalues));
	}

	public SerieModel(String name, double[] values, int minmax[]){
		super();
		this.name=name;
		this.yvalues=values;
		this.min=minmax[0];
		this.max=minmax[1];
	}
	

	@Id @GeneratedValue @Column(name = "series_id")
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public int getMax() {
		return max;
	}
	public void setMax(int max) {
		this.max = max;
	}
	public int getMin() {
		return min;
	}
	public void setMin(int min) {
		this.min = min;
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

	@Type(type="StringDouble")
	public double[] getYvalues() {
		return yvalues;
	}
	public void setYvalues(double[] yvalues) {
		this.yvalues = yvalues;
	}
	public void updateSeries(SerieModel s){
		if (max < s.max) max=s.max;
		if (min > s.min) min=s.min;
		if (!Strings.isNullOrEmpty(s.name)) name=s.name;
		if (!Strings.isNullOrEmpty(s.description)) description=s.description;
		yvalues=Doubles.concat(yvalues,s.getYvalues());
	}
	@Override
	public int compareTo(SerieModel arg0) {
		return this.id - arg0.id;
	}	
}
