package com.bitozen.hms.pm.common.dto.command.termination;

import com.bitozen.hms.common.dto.GenericAccessTokenDTO;
import com.bitozen.hms.common.dto.MetadataCreateDTO;
import com.bitozen.hms.pm.common.TerminationState;
import com.bitozen.hms.pm.common.TerminationStatus;
import com.bitozen.hms.pm.common.dto.query.termination.BAGPensionDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TerminationCreateCommandDTO implements Serializable {

    private String tmnID;
    private String tmnNotes;
    private String bpjsHTDocNumber;
    private String bpjsPensionDocNumber;
    private List<String> docCopies;
    private Boolean isCancelFinalApprove;
    private Boolean isExecuted;
    private Boolean isFinalApprove;
    private String memoDocNumber;
    private String employee;
    private String requestor;
    private String skDocNumber;
    private String skdtDocNumber;
    private List<TerminationDocumentCreateDTO> tmnDocs;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date tmnEffectiveDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date tmnReqDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date tmnPphEndDate;
    private String tmnReason;
    private TerminationState tmnState;
    private TerminationStatus tmnStatus;
    private BAGPensionDTO tmnPension;
    private MetadataCreateDTO metadata;
    private GenericAccessTokenDTO token;

    private String createdBy;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone = "GMT+7")
    private Date createdDate;
    private String recordID;

    @JsonIgnore
    public TerminationCreateCommandDTO getInstance() {
        List<String> listStrings = new ArrayList<>();
        listStrings.add("doc1");
        listStrings.add("doc2");
        return new TerminationCreateCommandDTO(
                UUID.randomUUID().toString().substring(0, 8).toUpperCase(),
                "notes",
                "no bpjs",
                "no pensiun bpjs",
                listStrings,
                Boolean.FALSE,
                Boolean.FALSE,
                Boolean.FALSE,
                "memo doc number",
                "emp id",
                "emp id",
                "sk doc number",
                "skdt doc number",
                new ArrayList<>(Arrays.asList(new TerminationDocumentCreateDTO().getInstance())),
                new Date(),
                new Date(),
                new Date(),
                "KEY001",
                TerminationState.INITIATE,
                TerminationStatus.INITIATE,
                new BAGPensionDTO(),
                new MetadataCreateDTO().getInstance(),
                new GenericAccessTokenDTO(),
                "SYSTEM",
                new Date(),
                UUID.randomUUID().toString()
        );

    }

}
