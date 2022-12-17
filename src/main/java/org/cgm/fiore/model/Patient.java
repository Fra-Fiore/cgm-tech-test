package org.cgm.fiore.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@ToString
@Entity
@Table(name = "patient")
public class Patient extends PanacheEntity {

    @Column(length = 20)
    public String name;
    @Column(length = 20)
    public String surname;
    public Date birthDate;
    @Column(length = 20)
    public String ssn;
    @Column(name = "last_modified")
    public Date lastModified = new Date();

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @JsonIgnore
    public List<Visit> visits;

    public void addVisit(Visit visit) {
        visits.add(visit);
        visit.patient = this;
    }

    public void removeVisit(Visit visit) {
        visits.remove(visit);
        visit.patient = null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Patient patient = (Patient) o;
        return id != null && Objects.equals(id, patient.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
