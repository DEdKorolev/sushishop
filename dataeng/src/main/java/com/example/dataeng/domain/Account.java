package com.example.dataeng.domain;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "accounts")

public class Account {

    private static final String SEQ_NAME = "account_seq";

    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ_NAME)
//    @SequenceGenerator(name = SEQ_NAME, sequenceName = SEQ_NAME, allocationSize = 1)
    private String account_num;
    private Timestamp valid_to;
    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;
}
