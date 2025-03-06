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

    @Column(columnDefinition = "TEXT")
    private String content;
    private Integer version;

}
