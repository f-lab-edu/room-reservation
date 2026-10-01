package com.onerty.yeogi.common.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "host")
@Getter
@Setter
@NoArgsConstructor
public class Host {

    @Id
    @Column(name = "host_id")
    private Long id;

    @Column(name = "business_name")
    private String businessName;

    @Column(name = "business_contact")
    private String businessContact;

}
