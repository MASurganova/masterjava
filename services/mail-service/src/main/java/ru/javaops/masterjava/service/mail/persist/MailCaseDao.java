package ru.javaops.masterjava.service.mail.persist;

import com.bertoncelj.jdbi.entitymapper.EntityMapperFactory;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapperFactory;
import ru.javaops.masterjava.persist.dao.AbstractDao;
import ru.javaops.masterjava.persist.model.Group;

import java.util.List;

@RegisterMapperFactory(EntityMapperFactory.class)
public abstract class MailCaseDao implements AbstractDao {

    @SqlUpdate("TRUNCATE mail_hist")
    @Override
    public abstract void clean();

    @SqlUpdate("INSERT INTO mail_hist (list_to, list_cc, subject, state, datetime)  VALUES (:listTo, :listCc, :subject, :state, :datetime)")
    @GetGeneratedKeys
    public abstract int insert(@BindBean MailCase mailCase);

    @SqlQuery("SELECT * FROM mail_hist ORDER BY datetime DESC")
    public abstract List<MailCase> getAll();
}
