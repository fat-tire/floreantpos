package com.floreantpos.model;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.usertype.ParameterizedType;
import org.hibernate.usertype.UserType;

import com.floreantpos.Messages;

public class EnumUserType implements UserType, ParameterizedType {  
     
   private Class clazz = null;  
     
   public void setParameterValues(Properties params) {  
      String enumClassName = params.getProperty("enumClassName");   //$NON-NLS-1$
      if (enumClassName == null) {  
         throw new MappingException(Messages.getString("EnumUserType.0"));   //$NON-NLS-1$
      }  
        
      try {  
            this.clazz = Class.forName(enumClassName);  
        } catch (ClassNotFoundException e) {  
         throw new MappingException("enumClass " + enumClassName + " not found", e);   //$NON-NLS-1$ //$NON-NLS-2$
        }  
   }  
     
    private static final int[] SQL_TYPES = {Types.VARCHAR};  
    public int[] sqlTypes() {  
        return SQL_TYPES;  
    }  

    public Class returnedClass() {  
        return clazz;  
    }  

    public Object nullSafeGet(ResultSet resultSet, String[] names, Object owner)  
                             throws HibernateException, SQLException {  
        String name = resultSet.getString(names[0]);  
        Object result = null;  
        if (!resultSet.wasNull()) {  
            result = Enum.valueOf(clazz, name);  
        }  
        return result;  
    }  

   public void nullSafeSet(PreparedStatement preparedStatement, Object value, int index)   
                          throws HibernateException, SQLException {  
        if (null == value) {  
            preparedStatement.setNull(index, Types.VARCHAR);  
        } else {  
            preparedStatement.setString(index, value.toString());  
        }  
    }  

    public Object deepCopy(Object value) throws HibernateException{  
        return value;  
    }  

    public boolean isMutable() {  
        return false;  
    }  

    public Object assemble(Serializable cached, Object owner) throws HibernateException {  
        return cached;  
    }  

    public Serializable disassemble(Object value) throws HibernateException {  
        return (Serializable)value;  
    }  

    public Object replace(Object original, Object target, Object owner) throws HibernateException {  
        return original;  
    }  
    public int hashCode(Object x) throws HibernateException {  
        return x.hashCode();  
    }  
    public boolean equals(Object x, Object y) throws HibernateException {  
        if (x == y)  
            return true;  
        if (null == x || null == y)  
            return false;  
        return x.equals(y);  
    }  
}  