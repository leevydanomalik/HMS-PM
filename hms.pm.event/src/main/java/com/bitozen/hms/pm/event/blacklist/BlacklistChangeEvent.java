package com.bitozen.hms.pm.event.blacklist;

import com.bitozen.hms.pm.common.BlacklistState;
import com.bitozen.hms.pm.common.BlacklistStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class BlacklistChangeEvent {

    private String updatedBy;
    private Date updatedDate;

    private String blacklistID;
    private String blacklistSPKNumber;
    private Date blacklistStartDate;
    private Date blacklistEndDate;
    private String blacklistNotes;
    private String blacklistType;
    private Boolean isPermanent;
    private String employee;
    private String requestor;
    private String blacklistDocURL;
    private Boolean isFinalApprove;
    private BlacklistStatus blacklistStatus;
    private BlacklistState blacklistState;
    private String compRegulationChapter;
    private String compRegulationChapterDesc;
    private String metadata;
    private String token;
}
