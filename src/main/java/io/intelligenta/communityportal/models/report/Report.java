package io.intelligenta.communityportal.models.report;


import io.intelligenta.communityportal.models.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@Data
public class Report extends BaseEntity {

    //site podatoci sto treba da gi sodrzi eden izvestaj

    @Column(length = 1000)
    private String podatociSluzbLice;

    private Integer brPrimeniBaranja;

    private Integer brPozitivnoOdgBaranja;

    @Column(length = 1000)
    private String odbieniIOtfrleniZalbi;

    private Integer brNeodogovoreniBaranja;

    @Column(length = 1000)
    private String vlozeniZalbi;

    private Integer brUsvoeniZalbi;

    private Integer brPreinaceniOdluki;

    @Column(length = 1000)
    private String odbieniZalbi;

    @Column(length = 1000)
    private String otfrelniZalbi;

}
