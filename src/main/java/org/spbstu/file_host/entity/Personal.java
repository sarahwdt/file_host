package org.spbstu.file_host.entity;

import lombok.*;
import org.spbstu.file_host.entity.abstraction.AbstractEntity;

import javax.persistence.Entity;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Personal extends AbstractEntity {
    private String name;
    private String surName;
    private String patronymicName;
    //TODO: to entity
    private String company;
    private String position;
}
