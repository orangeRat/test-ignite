package su.dru.ignite.domain;

import org.apache.ignite.cache.query.annotations.QuerySqlField;

import java.time.LocalDateTime;

/**
 * @author Andrey Vorobyov
 */
public class ClientProfile {
    public static final String CACHE_NAME = "clientProfile";

    @QuerySqlField(index = true)
    private long ctn;
    @QuerySqlField
    private String name;
    @QuerySqlField
    private String email;
    @QuerySqlField
    private LocalDateTime activateDate;

    public ClientProfile() {
    }

    public ClientProfile(long ctn, String name, String email, LocalDateTime activateDate) {
        this.ctn = ctn;
        this.name = name;
        this.email = email;
        this.activateDate = activateDate;
    }

    public long getCtn() {
        return ctn;
    }

    public void setCtn(long ctn) {
        this.ctn = ctn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getActivateDate() {
        return activateDate;
    }

    public void setActivateDate(LocalDateTime activateDate) {
        this.activateDate = activateDate;
    }
}
