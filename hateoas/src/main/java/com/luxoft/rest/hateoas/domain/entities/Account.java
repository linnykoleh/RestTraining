package com.luxoft.rest.hateoas.domain.entities;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Anton German
 * @since 09 September 2016
 */
@Entity
@XmlRootElement
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private double balance = 0;
    @Transient
    private List<ActionLink> actionLinks = new ArrayList<>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public List<ActionLink> getActionLinks() {
        return actionLinks;
    }

    public void setActionLinks(List<ActionLink> actionLinks) {
        this.actionLinks = actionLinks;
    }
}
