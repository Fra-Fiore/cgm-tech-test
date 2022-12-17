package org.cgm.fiore.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@ToString
@Entity
@Table(name = "visit")
public class Visit extends PanacheEntity {

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "patient_id", referencedColumnName = "id")
    public Patient patient;
    public Date date;
    @Column(name = "visit_type")
    public VisitType type;
    @Column(name = "visit_reason")
    public VisitReason reason;
    @Column(name = "family_history", length = 2000)
    public String familyHistory;
    @Column(name = "last_modified")
    public Date lastModified = new Date();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Visit visit = (Visit) o;
        return id != null && Objects.equals(id, visit.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
