package com.example.conduit.shared.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ErrorObject {
    private List<String> body;
}
