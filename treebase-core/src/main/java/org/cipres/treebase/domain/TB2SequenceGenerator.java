package org.cipres.treebase.domain;

import java.util.Properties;

import org.hibernate.MappingException;
import org.hibernate.boot.model.relational.Database;
import org.hibernate.boot.model.relational.SqlStringGenerationContext;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;
import org.hibernate.id.enhanced.SequenceStyleGenerator;
import org.hibernate.id.PersistentIdentifierGenerator;


/**  Custom id generator that translates table name to the name of an appropriate sequence.
 *   Code taken from https://www.hibernate.org/296.html 2010-02-01, where it was attributed to  Mr. Rob Hasselbaum. 
 *   Usage is in AbstractPersistedObject.java.
 *   Updated for Hibernate 5.x compatibility.
 *   --VG
 * */ 
public class TB2SequenceGenerator extends SequenceStyleGenerator {

    /**
     * If the parameters do not contain a SEQUENCE name, we
     * assign one based on the table name.
     */
    @Override
    public void configure(Type type, Properties params, ServiceRegistry serviceRegistry) throws MappingException {
        if(params.getProperty(SEQUENCE_PARAM) == null || params.getProperty(SEQUENCE_PARAM).length() == 0) {
            String tableName = params.getProperty(PersistentIdentifierGenerator.TABLE);
            if(tableName != null) {
                String seqName = tableName + "_id_sequence";
                params.setProperty(SEQUENCE_PARAM, seqName);               
            }
        }
        super.configure(type, params, serviceRegistry);
    }
}
