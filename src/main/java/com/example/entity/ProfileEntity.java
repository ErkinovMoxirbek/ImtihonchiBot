package com.example.entity;

import com.example.enums.ProfileRole;
import com.example.enums.ProfileStep;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "profile")
public class ProfileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "profile_id")
    private Long profileId;
    @Column(name = "name", columnDefinition = "text")
    private String name;
    @Column(name = "surname", columnDefinition = "text")
    private String surname;
    @Column(name = "group_name")
    private String group;
    @Column(name = "student_count")
    private Integer studentCount = 0;
    @Column(name = "exam_id")
    private Integer examId = 0;
    @Column(name = "grade")
    private Double grade;
    @Enumerated(EnumType.STRING)
    @Column(name = "step")
    private ProfileStep step;
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private ProfileRole role;
    @Column(name = "finished_option_count")
    private Integer finishedOptionCount = 0;
    @Column(name = "random_test_id")
    private String randomTestId;
    @Column(name = "options")
    private String options = "test-";
    @Column(name = "last_message_id")
    private Integer lastMessageId = 0;
    @Column(name = "file_name")
    private String fileName;
    @Column(name = "exam_finished_student_count")
    private Integer examFinishedStudentCount = 0;
    @Column(name = "student_list_to_string")
    private String studentListToString = "Imtihon topshiruvchilar ro'yhati: /";
    @Column(name = "visible")
    private Boolean visible = Boolean.FALSE;
}
