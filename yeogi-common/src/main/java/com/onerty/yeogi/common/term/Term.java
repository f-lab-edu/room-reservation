package com.onerty.yeogi.common.term;


import com.onerty.yeogi.common.util.BaseEntity;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "terms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Term extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long termId;

    private String title;
    private boolean isRequired;

    @Column(columnDefinition = "TEXT")
    private String content;
    private Integer version;
    
    @Version
    private Integer jpaVersion;
}
