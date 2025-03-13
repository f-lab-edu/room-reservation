package com.onerty.yeogi.common.term;


import com.onerty.yeogi.common.util.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


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

    @Column(columnDefinition = "TEXT")
    private String content;
    private Integer version;

}
