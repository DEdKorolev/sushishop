package com.example.dataeng.domain;

import lombok.*;
import javax.persistence.*;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "clients")

public class Client {

//    private static final String SEQ_NAME = "client_seq";

    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ_NAME)
//    @SequenceGenerator(name = SEQ_NAME, sequenceName = SEQ_NAME, allocationSize = 1)
    private String client_id;
    private String last_name;
    private String first_name;
    private String patrinymic;
    private Timestamp date_of_birth;
    private String passport_num;
    private Timestamp passport_valid_to_date;
    private String phone;
}