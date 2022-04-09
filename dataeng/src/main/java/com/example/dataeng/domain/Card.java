package com.example.dataeng.domain;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "cards")
public class Card {
    private static final String SEQ_NAME = "card_seq";

    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ_NAME)
//    @SequenceGenerator(name = SEQ_NAME, sequenceName = SEQ_NAME, allocationSize = 1)
    private String card_num;
    @ManyToOne
    @JoinColumn(name = "account_num")
    private Account account;
    @ManyToMany
    @JoinTable(name = "transactions",
            joinColumns = @JoinColumn(name = "card_num"),
            inverseJoinColumns = @JoinColumn(name = "terminal"))
    private List<Terminal> terminals;
}
