package ru.javaops.masterjava.persist.model;

import com.bertoncelj.jdbi.entitymapper.Column;
import lombok.*;

@Data
@AllArgsConstructor
public class Group {
    private @NonNull String name;
    private @NonNull Project project;
    private @NonNull GroupFlag flag;
}