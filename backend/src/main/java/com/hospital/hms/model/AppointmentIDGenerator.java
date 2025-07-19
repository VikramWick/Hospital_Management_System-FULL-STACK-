package com.hospital.hms.model;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import java.io.Serializable;

public class AppointmentIDGenerator implements IdentifierGenerator {
    
    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        String prefix = "APID";
        
        String query = "SELECT COALESCE(MAX(CAST(SUBSTRING(a.id, 5) AS integer)), 0) FROM Appointment a WHERE a.id LIKE :prefix";
        Integer maxId = (Integer) session.createQuery(query)
            .setParameter("prefix", prefix + "%")
            .uniqueResult();
        
        int nextId = maxId + 1;
        return prefix + String.format("%04d", nextId); // Adds padding zeros e.g., APID0001
    }
}