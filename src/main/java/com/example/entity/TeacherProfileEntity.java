package com.example.entity;

import com.example.enums.ProfileStep;
import lombok.*;

import javax.persistence.*;

@Getter @Setter @ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "teacher")
public class TeacherProfileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "profile_id")
    private Long profileId;
    @Column(name = "group_name")
    private String group;
    @Column(name = "student_count")
    private Integer studentCount = 0;
    @Column(name = "exam_id")
    private Integer examId = 0;
    @Column(name = "step")
    @Enumerated(EnumType.STRING)
    private ProfileStep step;
    @Column(name = "file_name")
    private String fileName;
    @Column(name = "last_message_id")
    private Integer lastMessageId = 0;
    @Column(name = "exam_finished_student_count")
    private Integer examFinishedStudentCount = 0;
    @Column(name = "student_list_to_string")
    private String studentListToString = "Imtihon topshiruvchilar ro'yhati: /";
    @Column(name = "visible")
    private Boolean visible = Boolean.FALSE;
}
