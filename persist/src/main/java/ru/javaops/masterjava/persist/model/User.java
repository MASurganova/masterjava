package ru.javaops.masterjava.persist.model;

import com.bertoncelj.jdbi.entitymapper.Column;
import lombok.*;

@Data
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseEntity {
    @Column("full_name")
    private @NonNull String fullName;
    private @NonNull String email;
    private @NonNull UserFlag flag;
    @Column("city_id")
    private String cityId;

    public User(Integer id, String fullName, String email, UserFlag flag, String cityId) {
        this(fullName, email, flag, cityId);
        this.id=id;
    }

    public User(Integer id, String fullName, String email, UserFlag flag) {
        this(fullName, email, flag);
        this.id=id;
    }
}