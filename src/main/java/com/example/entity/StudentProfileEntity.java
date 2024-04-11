package com.example.entity;


import com.example.enums.ProfileStep;
import lombok.*;

import javax.persistence.*;

@Getter @Setter @ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "student")
public class StudentProfileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "profile_id")
    private Long profileId;
    @Column(name = "name", columnDefinition = "text")
    private String name;
    @Column(name = "surname", columnDefinition = "text")
    private String surname;
    @Column(name = "grade")
    private Double grade;
    @Column(name = "exam_id")
    private Integer examId = 0;
    @Enumerated(EnumType.STRING)
    @Column(name = "step")
    private ProfileStep step;
    @Column(name = "finished_option_count")
    private Integer finishedOptionCount = 0;
    @Column(name = "random_test_id")
    private String randomTestId;
    @Column(name = "options")
    private String options = "test-";
    @Column(name = "last_message_id")
    private Integer lastMessageId = 0;
    @Column(name = "visible")
    private Boolean visible = Boolean.FALSE;
}
