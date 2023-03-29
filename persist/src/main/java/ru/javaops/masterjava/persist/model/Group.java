package ru.javaops.masterjava.persist.model;

import com.bertoncelj.jdbi.entitymapper.Column;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Group {
    private @NonNull String name;
    @Column("project_id")
    private @NonNull int projectId;
    private @NonNull GroupFlag flag;
}