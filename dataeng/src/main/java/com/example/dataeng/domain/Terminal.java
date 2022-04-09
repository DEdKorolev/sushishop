package com.example.dataeng.domain;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "terminals")
public class Terminal {
    private static final String SEQ_NAME = "terminal_seq";

    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ_NAME)
//    @SequenceGenerator(name = SEQ_NAME, sequenceName = SEQ_NAME, allocationSize = 1)
    private String terminal_id;
    private String terminal_type;
    private String terminal_city;
    private String terminal_address;
//    @ManyToMany
//    @JoinTable(name = "transactions",
//            joinColumns = @JoinColumn(name = "terminal"),
//            inverseJoinColumns = @JoinColumn(name = "card_num"))
//    private List<Terminal> terminals;
}
