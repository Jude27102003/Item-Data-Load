package org.example;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class Student {
    private int studentId;
    private String studentName;
    private String stream;
    private int totalMarks;
}
