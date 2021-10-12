package com.bitozen.hms.pm.common.dto.command.blacklist;

import com.bitozen.hms.common.dto.GenericAccessTokenDTO;
import com.bitozen.hms.common.dto.MetadataCreateDTO;
import com.bitozen.hms.pm.common.BlacklistState;
import com.bitozen.hms.pm.common.BlacklistStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BlacklistCreateCommandDTO implements Serializable {

    private String blacklistID;
    private String blacklistSPKNumber;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date blacklistStartDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
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
    private MetadataCreateDTO metadata;
    private GenericAccessTokenDTO token;

    private String createdBy;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone = "GMT+7")
    private Date createdDate;
    private String recordID;

    @JsonIgnore
    public BlacklistCreateCommandDTO getInstance() {
        return new BlacklistCreateCommandDTO(
                UUID.randomUUID().toString().substring(0, 8).toUpperCase(),
                UUID.randomUUID().toString().substring(0, 8).toUpperCase(),
                new Date(),
                new Date(),
                "NOTES",
                "KEY001",
                Boolean.TRUE,
                "EMPNEW666",
                "EMPNEW666",
                "Dokumen URL",
                Boolean.TRUE,
                BlacklistStatus.INITIATE,
                BlacklistState.BLACKLIST_INITIATE,
                "Regulation Chapter",
                "Regulation Chapter Description",
                new MetadataCreateDTO().getInstance(),
                new GenericAccessTokenDTO(),
                "ADMIN",
                new Date(),
                UUID.randomUUID().toString()
        );

    }
}
