package com.onerty.yeogi.term;


import com.onerty.yeogi.util.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Entity
@Table(name = "terms")
@Getter
@Setter
public class Term extends BaseEntity {

    @Id
    @GeneratedValue
    private Long termId;

    private String title;
    private boolean isRequired;

    @OneToMany(mappedBy = "term")
    private List<TermDetail> termDetails;

    @Override
    public String toString() {
        return "Term{" +
                "termId=" + termId +
                ", title='" + title + '\'' +
                ", isRequired=" + isRequired +
                ", termDetails=" + termDetails +
                '}';
    }
}
