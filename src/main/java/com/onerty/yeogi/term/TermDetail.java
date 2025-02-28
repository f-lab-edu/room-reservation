package com.onerty.yeogi.term;

import com.onerty.yeogi.util.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class TermDetail extends BaseEntity {

    @Id
    @GeneratedValue
    private Long termDetailId;

    @ManyToOne
    private Term term;

    @Column(columnDefinition = "TEXT")
    private String content;
    private Integer version;
    
}
