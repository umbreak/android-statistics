package hibernate.types;

import jabx.model.ChartModel;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Arrays;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.usertype.UserType;

import com.google.common.base.Strings;

public class StringtoArray implements UserType{

	@Override
	public Object assemble(Serializable cached, Object owner)
			throws HibernateException {
		return (String[])cached;
	}

	@Override
	public Object deepCopy(Object arg0) throws HibernateException {
		return (String[]) arg0;
	}

	@Override
	public Serializable disassemble(Object arg0) throws HibernateException {
		return (String[])arg0;
	}

	@Override
	public boolean equals(Object x, Object y) throws HibernateException {
		if (x == null) {
			return y == null;
		}
		return x.equals(y);
	}

	@Override
	public int hashCode(Object arg0) throws HibernateException {
		return arg0 == null ? 0 : arg0.hashCode();
	}

	@Override
	public boolean isMutable() {
		return false;
	}

	@Override
	public Object nullSafeGet(ResultSet rs, String[] names,
			SessionImplementor arg2, Object owner) throws HibernateException,
			SQLException {
		String[] resul=null;
		if (!rs.wasNull()) {
			String value=rs.getString(names[0]);
			if (!Strings.isNullOrEmpty(value)){
				resul=value.split(",");
			}
		}
		return resul;
	}

	@Override
	public void nullSafeSet(PreparedStatement ps, Object value, int index,
			SessionImplementor arg3) throws HibernateException, SQLException {
		if (value == null) {
			ps.setNull(index, Types.LONGVARCHAR);

		} else {  
			String res=Arrays.toString((String[])value);
			res=res.replace("[", "");
			res=res.replace("]", "");
			ps.setString(index, res);
		}
	}

	@Override
	public Object replace(Object original, Object target, Object chart)
			throws HibernateException {
		ChartModel c=(ChartModel)chart;
		c.setxValues((String[])target);
		return original;
	}

	@Override
	public Class returnedClass() {
		return String[].class;
	}

	@Override
	public int[] sqlTypes() {
		return new int[] {Types.LONGVARCHAR};
	}
}
