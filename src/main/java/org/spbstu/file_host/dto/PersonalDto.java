package org.spbstu.file_host.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.spbstu.file_host.dto.abstraction.AbstractDto;

@EqualsAndHashCode(callSuper = true)
@Data
public class PersonalDto extends AbstractDto {
    private String name;
    private String surName;
    private String patronymicName;
    private String company;
    private String position;
}
