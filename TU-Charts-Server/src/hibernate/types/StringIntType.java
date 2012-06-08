package hibernate.types;

import jabx.model.UserModel;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.usertype.UserType;

import com.google.common.base.Strings;
import com.google.common.primitives.Ints;

public class StringIntType implements UserType{

	@Override
	public Object assemble(Serializable cached, Object owner)
			throws HibernateException {
		return (int[])cached;
	}

	@Override
	public Object deepCopy(Object arg0) throws HibernateException {
		return (int[]) arg0;
	}

	@Override
	public Serializable disassemble(Object arg0) throws HibernateException {
		return (int[])arg0;
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
		int[] resul=null;
		if (!rs.wasNull()) {
			String value=rs.getString(names[0]);
			if (!Strings.isNullOrEmpty(value)){
				String [] res_string=value.split(",");
				resul = new int[res_string.length];
				for (int j = 0; j < resul.length; j++) 
					resul[j]=Integer.valueOf(res_string[j]);
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
			String res=Ints.join(",", (int[])value);
//			res=res.replace("[", "");
//			res=res.replace("]", "");			
			ps.setString(index, res);
		}
	}

	@Override
	public Object replace(Object original, Object target, Object user)
			throws HibernateException {
		return original;
	}

	@Override
	public Class returnedClass() {
		return Long[].class;
	}

	@Override
	public int[] sqlTypes() {
		return new int[] {Types.LONGVARCHAR};
	}
}
