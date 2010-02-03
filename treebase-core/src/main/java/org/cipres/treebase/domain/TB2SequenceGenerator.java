package org.cipres.treebase.domain;

import java.util.Properties;

import org.hibernate.MappingException;
import org.hibernate.type.Type;
import org.hibernate.dialect.Dialect;
import org.hibernate.id.PersistentIdentifierGenerator;
import org.hibernate.id.SequenceGenerator;



/**  Custom id generator that translates table name to the name of an appropriate sequence.
 *   Code taken from https://www.hibernate.org/296.html 2010-02-01, where it was attributed to  Mr. Rob Hasselbaum. 
 *   Usage is in AbstractPersistedObject.java.
 *   --VG
 * */ 
public class TB2SequenceGenerator extends SequenceGenerator {

    /**
     * If the parameters do not contain a {@link SequenceGenerator#SEQUENCE} name, we
     * assign one based on the table name.
     */
    public void configure(Type type, Properties params, Dialect dialect) throws MappingException {
        if(params.getProperty(SEQUENCE) == null || params.getProperty(SEQUENCE).length() == 0) {
            String tableName = params.getProperty(PersistentIdentifierGenerator.TABLE);
            if(tableName != null) {
                String seqName = tableName + "_id_sequence";
                params.setProperty(SEQUENCE, seqName);               
            }
        }
        super.configure(type, params, dialect);
    }
}
