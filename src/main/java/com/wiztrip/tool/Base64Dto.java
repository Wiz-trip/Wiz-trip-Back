package com.wiztrip.tool;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Base64Dto {
    private String fileName;
    private String content;
}